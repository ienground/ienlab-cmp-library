package zone.ien.utils.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.icons.CupertinoIcons
import zone.ien.hig.icons.outlined.AppleLogo
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.ui.interactive.IenAuthForm
import zone.ien.utils.ui.interactive.IenAuthFormCopy
import zone.ien.utils.ui.interactive.IenAuthFormMode
import zone.ien.utils.ui.interactive.IenAuthFormModeCopy
import zone.ien.utils.ui.interactive.IenAuthFormState
import zone.ien.utils.ui.interactive.IenAuthFormStatus
import zone.ien.utils.ui.interactive.IenAuthGuestAction
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenFieldStatus
import zone.ien.utils.ui.interactive.IenPasswordRule
import zone.ien.utils.ui.interactive.IenTextFieldState
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.TopBarMode

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AuthFormScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    val backdrop = rememberDefaultBackdrop()
    var mode by remember { mutableStateOf(IenAuthFormMode.Login) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<IenAuthFormStatus>(IenAuthFormStatus.Idle) }

    val copy = remember {
        IenAuthFormCopy(
            login = IenAuthFormModeCopy(
                title = "다시 만나요",
                description = "이메일과 비밀번호로 로그인하세요.",
                submitLabel = "로그인",
                modePrompt = "계정이 없나요?",
                modeActionLabel = "회원가입",
            ),
            signUp = IenAuthFormModeCopy(
                title = "계정을 만들어 보세요",
                description = "비밀번호 조건을 확인하며 가입할 수 있습니다.",
                submitLabel = "회원가입",
                modePrompt = "이미 계정이 있나요?",
                modeActionLabel = "로그인",
            ),
            emailLabel = "이메일",
            emailPlaceholder = "name@example.com",
            passwordLabel = "비밀번호",
            passwordPlaceholder = "비밀번호를 입력하세요",
            confirmPasswordLabel = "비밀번호 확인",
            confirmPasswordPlaceholder = "비밀번호를 한 번 더 입력하세요",
            passwordRulesTitle = "비밀번호 보안",
            socialLoginTitle = "다른 방법으로 계속하기",
        )
    }
    val passwordRules = listOf(
        IenPasswordRule(
            label = "8자 이상",
            satisfied = password.length >= 8,
            statusDescription = if (password.length >= 8) "충족" else "미충족",
        ),
        IenPasswordRule(
            label = "영문 포함",
            satisfied = password.any(Char::isLetter),
            statusDescription = if (password.any(Char::isLetter)) "충족" else "미충족",
        ),
        IenPasswordRule(
            label = "숫자 포함",
            satisfied = password.any(Char::isDigit),
            statusDescription = if (password.any(Char::isDigit)) "충족" else "미충족",
        ),
    )
    val confirmState = when {
        confirmPassword.isBlank() -> IenTextFieldState()
        confirmPassword == password -> IenTextFieldState(
            status = IenFieldStatus.Success("비밀번호가 일치합니다"),
        )
        else -> IenTextFieldState(
            status = IenFieldStatus.Error("비밀번호가 일치하지 않습니다"),
        )
    }
    val passwordReady = passwordRules.all(IenPasswordRule::satisfied)
    val formReady = email.contains("@") && password.isNotBlank() &&
        (mode == IenAuthFormMode.Login || (passwordReady && confirmPassword == password))
    val formState = IenAuthFormState(
        confirmPassword = confirmState,
        submit = IenButtonState(enabled = formReady),
        status = status,
    )

    IenAdaptiveTheme(target = Theme.Material3) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            title = { IenText("Auth Form") },
            adaptation = {
                material { this.mode = TopBarMode.Expanded }
                cupertino { this.backdrop = backdrop }
            },
            modifier = modifier,
        ) { paddingValues, title ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = IenTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xl),
            ) {
                title()
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .heightIn(min = maxHeight),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        IenAuthForm(
                            mode = mode,
                            email = email,
                            password = password,
                            confirmPassword = confirmPassword,
                            copy = copy,
                            passwordRules = passwordRules,
                            providers = {
                                Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
                                    AuthProviderButton(
                                        label = "Google",
                                        enabled = !formState.submit.loading,
                                        onClick = {
                                            status = IenAuthFormStatus.Success("Google 로그인을 준비했습니다")
                                        },
                                    ) {
                                        AuthProviderMark("G")
                                    }
                                    AuthProviderButton(
                                        label = "Apple",
                                        enabled = !formState.submit.loading,
                                        onClick = {
                                            status = IenAuthFormStatus.Success("Apple 로그인을 준비했습니다")
                                        },
                                    ) {
                                        IenIcon(
                                            imageVector = CupertinoIcons.Outlined.AppleLogo,
                                            contentDescription = null,
                                            tint = IenTheme.colors.textPrimary,
                                        )
                                    }
                                    AuthProviderButton(
                                        label = "네이버",
                                        enabled = !formState.submit.loading,
                                        onClick = {
                                            status = IenAuthFormStatus.Success("네이버 로그인을 준비했습니다")
                                        },
                                    ) {
                                        AuthProviderMark("N")
                                    }
                                    AuthProviderButton(
                                        label = "카카오",
                                        enabled = !formState.submit.loading,
                                        onClick = {
                                            status = IenAuthFormStatus.Success("카카오 로그인을 준비했습니다")
                                        },
                                    ) {
                                        AuthProviderMark("K")
                                    }
                                }
                            },
                            state = formState,
                            guestAction = IenAuthGuestAction("게스트로 계속하기") {
                                status = IenAuthFormStatus.Success("게스트 모드로 시작합니다")
                            },
                            onEmailChange = {
                                email = it
                                status = IenAuthFormStatus.Idle
                            },
                            onPasswordChange = {
                                password = it
                                status = IenAuthFormStatus.Idle
                            },
                            onConfirmPasswordChange = {
                                confirmPassword = it
                                status = IenAuthFormStatus.Idle
                            },
                            onSubmit = {
                                status = IenAuthFormStatus.Success(
                                    if (mode == IenAuthFormMode.Login) "로그인 요청을 준비했습니다"
                                    else "회원가입 요청을 준비했습니다",
                                )
                            },
                            onModeChange = {
                                mode = it
                                status = IenAuthFormStatus.Idle
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthFormPreview() {
    AuthFormScreen(navigateBack = {})
}

@Composable
private fun AuthProviderButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    IenButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        size = IenButtonSize.Medium,
        variant = IenButtonVariant.Line,
        state = IenButtonState(enabled = enabled),
        display = IenButtonDisplay.Block,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(IenTheme.icon.md),
                contentAlignment = Alignment.Center,
            ) {
                icon()
            }
            IenText(text = label)
        }
    }
}

@Composable
private fun AuthProviderMark(mark: String) {
    Box(
        modifier = Modifier
            .size(IenTheme.icon.md)
            .background(IenTheme.colors.brand, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        IenText(
            text = mark,
            style = IenTheme.typography.label1,
            color = IenTheme.colors.onBrand,
        )
    }
}
