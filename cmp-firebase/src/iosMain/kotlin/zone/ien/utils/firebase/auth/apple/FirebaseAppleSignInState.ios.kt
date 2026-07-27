package zone.ien.utils.firebase.auth.apple

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.ASAuthorization
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import platform.AuthenticationServices.ASAuthorizationAppleIDProvider
import platform.AuthenticationServices.ASAuthorizationController
import platform.AuthenticationServices.ASAuthorizationControllerDelegateProtocol
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol
import platform.AuthenticationServices.ASAuthorizationScopeEmail
import platform.AuthenticationServices.ASAuthorizationScopeFullName
import platform.AuthenticationServices.ASPresentationAnchor
import platform.CoreCrypto.CC_SHA256
import platform.CoreCrypto.CC_SHA256_DIGEST_LENGTH
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuth
import swiftPMImport.zone.ien.firebase.firebase.auth.FIRAuthDataResult
import swiftPMImport.zone.ien.firebase.firebase.auth.FIROAuthProvider
import zone.ien.firebase.Firebase
import zone.ien.firebase.auth.FirebaseUser
import zone.ien.firebase.auth.auth
import zone.ien.utils.firebase.auth.AuthCredential
import zone.ien.utils.firebase.auth.AuthProviderIds
import zone.ien.utils.firebase.auth.FirebaseAuthGateway
import zone.ien.utils.firebase.auth.LaunchingSignInState
import zone.ien.utils.firebase.auth.NoOpSignInState
import zone.ien.utils.firebase.auth.SignInState
import kotlin.coroutines.resume

@Composable
actual fun rememberFirebaseAppleSignInState(
    requestScopes: List<AppleSignInRequestScope>,
    linkAccount: Boolean,
    gateway: FirebaseAuthGateway,
    onResult: (Result<FirebaseUser?>) -> Unit,
): SignInState {
    if (LocalInspectionMode.current) return NoOpSignInState

    val scope = rememberCoroutineScope()
    val currentScopes by rememberUpdatedState(requestScopes)
    val currentLinkAccount by rememberUpdatedState(linkAccount)
    val currentOnResult by rememberUpdatedState(onResult)

    return remember(scope, gateway) {
        LaunchingSignInState(scope) {
            try {
                val appleCred = performNativeAppleSignIn(currentScopes)
                val userResult = signInToFirebaseApple(
                    idToken = appleCred.idToken,
                    rawNonce = appleCred.rawNonce,
                    fullName = appleCred.fullNameComponents,
                    linkAccount = currentLinkAccount,
                )
                currentOnResult(userResult)
            } catch (e: Throwable) {
                currentOnResult(Result.failure(e))
            }
        }
    }
}

private class NativeAppleCredential(
    val idToken: String,
    val rawNonce: String,
    val fullNameComponents: platform.Foundation.NSPersonNameComponents? = null,
)

private suspend fun performNativeAppleSignIn(
    requestScopes: List<AppleSignInRequestScope>,
): NativeAppleCredential = suspendCancellableCoroutine { continuation ->
    val rawNonce = generateNonceString()
    val presentationProvider = ApplePresentationContextProvider()
    val delegate = AppleAuthorizationDelegate(rawNonce = rawNonce) { result ->
        inFlightAppleDelegate = null
        inFlightPresentationProvider = null
        if (continuation.isActive) {
            result.fold(
                onSuccess = { continuation.resume(it) },
                onFailure = { continuation.resumeWith(Result.failure(it)) }
            )
        }
    }

    inFlightAppleDelegate = delegate
    inFlightPresentationProvider = presentationProvider

    val request = ASAuthorizationAppleIDProvider().createRequest()
    request.requestedScopes = requestScopes.map {
        when (it) {
            AppleSignInRequestScope.Email -> ASAuthorizationScopeEmail
            AppleSignInRequestScope.FullName -> ASAuthorizationScopeFullName
        }
    }
    request.nonce = sha256Hex(rawNonce)

    val controller = ASAuthorizationController(listOf(request))
    controller.delegate = delegate
    controller.presentationContextProvider = presentationProvider
    controller.performRequests()
}
@OptIn(ExperimentalForeignApi::class)
private suspend fun signInToFirebaseApple(
    idToken: String,
    rawNonce: String,
    fullName: platform.Foundation.NSPersonNameComponents?,
    linkAccount: Boolean,
): Result<FirebaseUser?> = suspendCancellableCoroutine { continuation ->
    val firCredential = FIROAuthProvider.appleCredentialWithIDToken(idToken, rawNonce, fullName)
    val iosAuth = FIRAuth.auth()
    val currentUser = iosAuth.currentUser()

    val handleResult: (FIRAuthDataResult?, NSError?) -> Unit = { result, error ->
        if (continuation.isActive) {
            if (error != null || result == null) {
                val message = buildString {
                    append("Apple Sign-In failed")
                    error?.let { e ->
                        append(" [domain=${e.domain}:code=${e.code}]")
                        e.localizedDescription.let { append(": $it") }
                    }
                }
                continuation.resume(Result.failure(IllegalStateException(message)))
            } else {
                val currentUser = Firebase.auth.currentUser
                if (currentUser != null) {
                    continuation.resume(Result.success(currentUser))
                } else {
                    continuation.resume(Result.failure(IllegalStateException(
                        "Apple Sign-In succeeded but currentUser is null"
                    )))
                }
            }
        }
    }

    if (linkAccount && currentUser != null) {
        currentUser.linkWithCredential(firCredential, handleResult)
    } else {
        iosAuth.signInWithCredential(firCredential, handleResult)
    }
}

private var inFlightAppleDelegate: AppleAuthorizationDelegate? = null
private var inFlightPresentationProvider: ApplePresentationContextProvider? = null

private class ApplePresentationContextProvider :
    ASAuthorizationControllerPresentationContextProvidingProtocol, NSObject() {
    override fun presentationAnchorForAuthorizationController(
        controller: ASAuthorizationController,
    ): ASPresentationAnchor {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        return rootViewController?.view?.window
    }
}

private class AppleAuthorizationDelegate(
    private val rawNonce: String,
    private val onResult: (Result<NativeAppleCredential>) -> Unit,
) : ASAuthorizationControllerDelegateProtocol, NSObject() {

    @OptIn(BetaInteropApi::class, ExperimentalForeignApi::class)
    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization: ASAuthorization,
    ) {
        val credential = didCompleteWithAuthorization.credential as? ASAuthorizationAppleIDCredential
        if (credential == null) {
            onResult(Result.failure(IllegalStateException("Unexpected Apple credential type")))
            return
        }

        val identityToken = credential.identityToken
        if (identityToken == null) {
            onResult(Result.failure(IllegalStateException("Unable to fetch Apple identity token")))
            return
        }

       val idTokenString = NSString.create(identityToken, NSUTF8StringEncoding)?.toString()
       if (idTokenString == null) {
           onResult(Result.failure(IllegalStateException("Unable to serialize Apple identity token")))
           return
       }

       onResult(
           Result.success(
               NativeAppleCredential(
                   idToken = idTokenString,
                   rawNonce = rawNonce,
                   fullNameComponents = credential.fullName,
               )
           )
       )
   }

   override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError: NSError,
    ) {
        onResult(
            Result.failure(
                IllegalStateException(
                    didCompleteWithError.localizedFailureReason
                        ?: didCompleteWithError.localizedDescription
                )
            )
        )
    }
}

private fun generateNonceString(length: Int = 32): String {
    val randomBytes = getSecureRandomBytes(length)
    val charset = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._"
    return randomBytes.map { byte -> charset[(byte.toInt() and 0xFF) % charset.length] }
        .joinToString("")
}

@OptIn(ExperimentalForeignApi::class)
private fun getSecureRandomBytes(length: Int): ByteArray = memScoped {
    val randomBytes = allocArray<UByteVar>(length)
    val errorCode = SecRandomCopyBytes(kSecRandomDefault, length.convert(), randomBytes)
    if (errorCode != errSecSuccess) {
        throw RuntimeException("SecRandomCopyBytes failed with OSStatus $errorCode")
    }
    randomBytes.readBytes(length)
}

@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
private fun sha256Hex(input: String): String {
    val hashedData = UByteArray(CC_SHA256_DIGEST_LENGTH)
    val inputData = input.encodeToByteArray()
    inputData.usePinned {
        CC_SHA256(it.addressOf(0), inputData.size.convert(), hashedData.refTo(0))
    }
    return hashedData.toByteArray().toHexString(HexFormat.Default)
}
