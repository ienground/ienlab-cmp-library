package zone.ien.utils.ui.interactive

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.screen.IenTop
import zone.ien.utils.ui.screen.IenTopSubtitleParagraph
import zone.ien.utils.ui.screen.IenTopTitleParagraph
import zone.ien.utils.ui.screen.IenTopTitleSize

/** 인증 폼에서 현재 편집 중인 인증 흐름입니다. */
enum class IenAuthFormMode {
    /** 기존 계정으로 로그인합니다. */
    Login,

    /** 새 계정을 등록합니다. */
    SignUp,
}

/** 로그인/회원가입 모드에 따라 표시할 제목과 설명입니다. */
@Immutable
data class IenAuthFormModeCopy(
    val title: String,
    val description: String? = null,
    val submitLabel: String,
    val modePrompt: String? = null,
    val modeActionLabel: String? = null,
)

/** 인증 폼에서 사용하는 모든 앱 제공 문구입니다. */
@Immutable
data class IenAuthFormCopy(
    val login: IenAuthFormModeCopy,
    val signUp: IenAuthFormModeCopy,
    val emailLabel: String,
    val emailPlaceholder: String? = null,
    val passwordLabel: String,
    val passwordPlaceholder: String? = null,
    val confirmPasswordLabel: String,
    val confirmPasswordPlaceholder: String? = null,
    val passwordRulesTitle: String? = null,
    val socialLoginTitle: String? = null,
)

/** 호출자가 전달하는 비밀번호 보안 조건과 현재 충족 여부입니다. */
@Immutable
data class IenPasswordRule(
    val label: String,
    val satisfied: Boolean,
    val statusDescription: String? = null,
)

/** 인증 폼 아래에 선택적으로 표시하는 게스트 진입 동작입니다. */
data class IenAuthGuestAction(
    val label: String,
    val onClick: () -> Unit,
)

/** 인증 폼의 제출 가능 여부, 필드 상태, 서버 결과를 호출자가 소유하도록 묶은 상태입니다. */
@Immutable
data class IenAuthFormState(
    val email: IenTextFieldState = IenTextFieldState(),
    val password: IenTextFieldState = IenTextFieldState(),
    val confirmPassword: IenTextFieldState = IenTextFieldState(),
    val submit: IenButtonState = IenButtonState(),
    val status: IenAuthFormStatus = IenAuthFormStatus.Idle,
)

/** 인증 제출 결과를 표시하기 위한 폼 수준 상태입니다. */
@Immutable
sealed interface IenAuthFormStatus {
    /** 표시할 결과가 없는 기본 상태입니다. */
    data object Idle : IenAuthFormStatus

    /** 인증 요청이 실패했음을 나타냅니다. */
    data class Error(val message: String) : IenAuthFormStatus

    /** 인증 요청이 성공했음을 나타냅니다. */
    data class Success(val message: String) : IenAuthFormStatus
}

/**
 * 여러 앱에서 재사용할 수 있는 단일 열 인증 폼입니다.
 *
 * 입력값과 검증·제출 상태는 호출자가 소유하고, 이 컴포저블은 전달받은 상태를 렌더링하며
 * 사용자 동작을 콜백으로 전달합니다. Firebase, ViewModel, Navigation에는 의존하지 않습니다.
 *
 * @param mode 현재 로그인 또는 회원가입 모드
 * @param email 이메일 입력값
 * @param password 비밀번호 입력값
 * @param copy 앱에서 공급하는 제목·라벨·버튼 문구
 * @param onEmailChange 이메일 변경 콜백
 * @param onPasswordChange 비밀번호 변경 콜백
 * @param onSubmit 제출 콜백
 * @param onModeChange 모드 전환 콜백
 * @param modifier 루트 레이아웃에 적용할 [Modifier]
 * @param confirmPassword 회원가입 모드의 비밀번호 확인 입력값
 * @param passwordRules 회원가입 모드에 표시할 비밀번호 조건 목록
 * @param providers 소셜 로그인 버튼·아이콘·클릭을 호출자가 구성하는 슬롯. null이면 표시하지 않습니다.
 * @param state 필드·제출·결과 상태
 * @param onConfirmPasswordChange 비밀번호 확인 변경 콜백
 * @param guestAction 선택적 게스트 진입 동작. null이면 표시하지 않습니다.
 */
@Composable
fun IenAuthForm(
    mode: IenAuthFormMode,
    email: String,
    password: String,
    copy: IenAuthFormCopy,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onModeChange: (IenAuthFormMode) -> Unit,
    modifier: Modifier = Modifier,
    confirmPassword: String = "",
    passwordRules: List<IenPasswordRule> = emptyList(),
    providers: (@Composable () -> Unit)? = null,
    state: IenAuthFormState = IenAuthFormState(),
    onConfirmPasswordChange: (String) -> Unit = {},
    guestAction: IenAuthGuestAction? = null,
) {
    val modeCopy = when (mode) {
        IenAuthFormMode.Login -> copy.login
        IenAuthFormMode.SignUp -> copy.signUp
    }
    val showConfirmPassword = mode == IenAuthFormMode.SignUp
    val showRules = showConfirmPassword && passwordRules.isNotEmpty()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
    ) {
        IenTop(
            title = {
                AuthFormAnimatedContent(
                    targetState = modeCopy.title,
                    label = "auth_form_title",
                ) { title ->
                    IenTopTitleParagraph(
                        text = title,
                        size = IenTopTitleSize.Large,
                    )
                }
            },
            subtitleBottom = modeCopy.description
                ?.takeIf { it.isNotBlank() }
                ?.let { description ->
                    {
                        AuthFormAnimatedContent(
                            targetState = description,
                            label = "auth_form_description",
                        ) { subtitle ->
                            IenTopSubtitleParagraph(
                                text = subtitle,
                                style = IenTheme.typography.body2,
                                color = IenTheme.colors.textSecondary,
                                fontWeight = FontWeight.Normal,
                            )
                        }
                    }
                },
            upperGap = 0.dp,
            lowerGap = 0.dp,
            contentPadding = PaddingValues(0.dp),
        )

        Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
            AuthFormAnimatedContent(
                targetState = copy.emailLabel to copy.emailPlaceholder,
                modifier = Modifier.fillMaxWidth(),
                label = "auth_form_email_copy",
            ) { (label, placeholder) ->
                IenTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = label,
                    labelOption = IenTextFieldLabelOption.Sustain,
                    placeholder = placeholder,
                    state = state.email,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            AuthFormAnimatedContent(
                targetState = copy.passwordLabel to copy.passwordPlaceholder,
                modifier = Modifier.fillMaxWidth(),
                label = "auth_form_password_copy",
            ) { (label, placeholder) ->
                IenPasswordTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = label,
                    labelOption = IenTextFieldLabelOption.Sustain,
                    placeholder = placeholder,
                    state = state.password,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = if (showConfirmPassword) ImeAction.Next else ImeAction.Done,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        AnimatedVisibility(
            visible = showConfirmPassword,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = IenTheme.motion.normalMillis,
                    easing = IenTheme.motion.standardEasing,
                ),
            ) + expandVertically(
                animationSpec = tween(
                    durationMillis = IenTheme.motion.normalMillis,
                    easing = IenTheme.motion.standardEasing,
                ),
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = IenTheme.motion.fastMillis,
                    easing = IenTheme.motion.standardEasing,
                ),
            ) + shrinkVertically(
                animationSpec = tween(
                    durationMillis = IenTheme.motion.fastMillis,
                    easing = IenTheme.motion.standardEasing,
                ),
            ),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
                AuthFormAnimatedContent(
                    targetState = copy.confirmPasswordLabel to copy.confirmPasswordPlaceholder,
                    modifier = Modifier.fillMaxWidth(),
                    label = "auth_form_confirm_password_copy",
                ) { (label, placeholder) ->
                    IenPasswordTextField(
                        value = confirmPassword,
                        onValueChange = onConfirmPasswordChange,
                        label = label,
                        labelOption = IenTextFieldLabelOption.Sustain,
                        placeholder = placeholder,
                        state = state.confirmPassword,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                if (showRules) {
                    PasswordRules(
                        title = copy.passwordRulesTitle,
                        rules = passwordRules,
                    )
                }
            }
        }

        AuthFormStatusMessage(status = state.status)

        IenButton(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            size = IenButtonSize.Large,
            variant = IenButtonVariant.Fill,
            state = state.submit,
            display = IenButtonDisplay.Block,
        ) {
            AuthFormAnimatedContent(
                targetState = modeCopy.submitLabel,
                label = "auth_form_submit_label",
            ) { label ->
                IenText(text = label)
            }
        }

        modeCopy.modePrompt?.takeIf { it.isNotBlank() }?.let { prompt ->
            modeCopy.modeActionLabel?.takeIf { it.isNotBlank() }?.let { actionLabel ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AuthFormAnimatedContent(
                        targetState = prompt,
                        label = "auth_form_mode_prompt",
                    ) { text ->
                        IenText(
                            text = text,
                            style = IenTheme.typography.body2,
                            color = IenTheme.colors.textSecondary,
                        )
                    }
                    IenTextButton(
                        onClick = {
                            onModeChange(
                                when (mode) {
                                    IenAuthFormMode.Login -> IenAuthFormMode.SignUp
                                    IenAuthFormMode.SignUp -> IenAuthFormMode.Login
                                },
                            )
                        },
                        size = IenTextButtonSize.XLarge,
                        state = IenButtonState(enabled = !state.submit.loading),
                    ) {
                        AuthFormAnimatedContent(
                            targetState = actionLabel,
                            label = "auth_form_mode_action_label",
                        ) { label ->
                            IenText(text = label)
                        }
                    }
                }
            }
        }

        providers?.let { providerContent ->
            if (!copy.socialLoginTitle.isNullOrBlank()) {
                AuthFormAnimatedContent(
                    targetState = copy.socialLoginTitle,
                    modifier = Modifier.fillMaxWidth(),
                    label = "auth_form_social_login_title",
                ) { title ->
                    IenText(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        style = IenTheme.typography.label2,
                        color = IenTheme.colors.textSecondary,
                    )
                }
            }
            providerContent()
        }

        guestAction?.let { action ->
            IenTextButton(
                onClick = action.onClick,
                modifier = Modifier.fillMaxWidth(),
                size = IenTextButtonSize.XLarge,
                tone = zone.ien.utils.ui.foundation.IenSemanticTone.Neutral,
                state = IenButtonState(enabled = !state.submit.loading),
            ) {
                IenText(text = action.label)
            }
        }
    }
}

@Composable
private fun <T> AuthFormAnimatedContent(
    targetState: T,
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    val normalMillis = IenTheme.motion.normalMillis
    val fastMillis = IenTheme.motion.fastMillis
    val standardEasing = IenTheme.motion.standardEasing

    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = normalMillis,
                    easing = standardEasing,
                ),
            ) togetherWith fadeOut(
                animationSpec = tween(
                    durationMillis = fastMillis,
                    easing = standardEasing,
                ),
            )
        },
        label = label,
    ) { state ->
        content(state)
    }
}

@Composable
private fun PasswordRules(
    title: String?,
    rules: List<IenPasswordRule>,
) {
    IenSurface(
        modifier = Modifier.fillMaxWidth(),
        color = IenTheme.colors.surfaceWeak,
        shape = ContinuousRoundedRectangle(IenTheme.radius.default),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Column(
            modifier = Modifier.padding(IenTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            title?.takeIf { it.isNotBlank() }?.let {
                AuthFormAnimatedContent(
                    targetState = it,
                    label = "auth_form_password_rules_title",
                ) { titleText ->
                    IenText(
                        text = titleText,
                        style = IenTheme.typography.label1,
                        color = IenTheme.colors.textPrimary,
                    )
                }
            }
            rules.forEach { rule ->
                val color = if (rule.satisfied) IenTheme.colors.success else IenTheme.colors.textTertiary
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = listOfNotNull(rule.label, rule.statusDescription)
                                .joinToString(separator = ": ")
                        },
                    horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(IenTheme.spacing.xs)
                            .then(
                                if (rule.satisfied) {
                                    Modifier.background(color, CircleShape)
                                } else {
                                    Modifier.border(BorderStroke(IenTheme.stroke.thin, color), CircleShape)
                                },
                            ),
                    )
                    IenText(
                        text = rule.label,
                        style = IenTheme.typography.caption,
                        color = color,
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthFormStatusMessage(status: IenAuthFormStatus) {
    val message = when (status) {
        IenAuthFormStatus.Idle -> null
        is IenAuthFormStatus.Error -> status.message
        is IenAuthFormStatus.Success -> status.message
    }?.takeIf { it.isNotBlank() } ?: return
    val isError = status is IenAuthFormStatus.Error
    val color = if (isError) IenTheme.colors.danger else IenTheme.colors.success
    val background = if (isError) IenTheme.colors.dangerWeak else IenTheme.colors.successWeak

    IenSurface(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        color = background,
        contentColor = color,
        shape = ContinuousRoundedRectangle(IenTheme.radius.default),
        border = BorderStroke(IenTheme.stroke.thin, color),
    ) {
        IenText(
            text = message,
            modifier = Modifier.padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
            style = IenTheme.typography.body2,
            color = color,
        )
    }
}
