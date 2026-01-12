package zone.ien.utils.firebase.auth

import androidx.compose.runtime.Composable
import ienlab_cmp_library.cmp_firebase.generated.resources.Res
import ienlab_cmp_library.cmp_firebase.generated.resources.account_does_not_exist
import ienlab_cmp_library.cmp_firebase.generated.resources.email_already_in_use
import ienlab_cmp_library.cmp_firebase.generated.resources.email_badly_formatted
import ienlab_cmp_library.cmp_firebase.generated.resources.email_not_verified
import ienlab_cmp_library.cmp_firebase.generated.resources.error_wrong_platform
import ienlab_cmp_library.cmp_firebase.generated.resources.format_empty
import ienlab_cmp_library.cmp_firebase.generated.resources.password_not_match
import ienlab_cmp_library.cmp_firebase.generated.resources.password_rule_failed
import ienlab_cmp_library.cmp_firebase.generated.resources.success_email_sent
import ienlab_cmp_library.cmp_firebase.generated.resources.user_canceled
import ienlab_cmp_library.cmp_firebase.generated.resources.user_is_null
import ienlab_cmp_library.cmp_firebase.generated.resources.wrong_password
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed class FbAuthCode {
    sealed class Success: FbAuthCode() {
        data object EmailSent: Success()
    }

    sealed class Error: FbAuthCode() {
        data object UserNull: Error()
        data object UserCanceled: Error()
        data object WrongPassword: Error()
        data object WrongPlatform: Error()
        data object UserNotFound: Error()
        data object EmailNotVerified: Error()
        data object EmailAlreadyInUse: Error()
        data object PasswordNotMatched: Error()
        data object PasswordRuleFailed: Error()
        data object EmailBadlyFormatted: Error()
        data object Empty: Error()
        data class Etc(val message: String): Error()
    }

    @Composable
    fun labelResource(): String {
        return when (this) {
            is Error.Etc -> message
            else -> getLabelBasic()?.let { stringResource(it) }.orEmpty()
        }
    }

    suspend fun getLabel(): String {
        return when (this) {
            is Error.Etc -> message
            else -> getLabelBasic()?.let { getString(it) }.orEmpty()
        }
    }

    private fun getLabelBasic(): StringResource? {
        return when (this) {
            Success.EmailSent -> Res.string.success_email_sent
            Error.UserNull -> Res.string.user_is_null
            Error.UserCanceled -> Res.string.user_canceled
            Error.WrongPassword -> Res.string.wrong_password
            Error.WrongPlatform -> Res.string.error_wrong_platform
            Error.UserNotFound -> Res.string.account_does_not_exist
            Error.EmailNotVerified -> Res.string.email_not_verified
            Error.EmailAlreadyInUse -> Res.string.email_already_in_use
            Error.PasswordNotMatched -> Res.string.password_not_match
            Error.PasswordRuleFailed -> Res.string.password_rule_failed
            Error.EmailBadlyFormatted -> Res.string.email_badly_formatted
            Error.Empty -> Res.string.format_empty
            is Error.Etc -> null
        }
    }
}