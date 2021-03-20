package racosta.samples.composetodo.commons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun CircleDialogWithTextFieldAndButton(
    editTextValue: String,
    onEditTextValueChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
    buttonContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    editTextLabel: @Composable (() -> Unit)? = null,
    buttonEnabled: Boolean = true,
) {
    SurfaceDialog(onDismissRequest = onDismissRequest, modifier = modifier) {
        Column(
            modifier.padding(16.dp)
        ) {

            Spacer(Modifier.height(90.dp))

            CircleDialogTextField(editTextValue, onEditTextValueChanged, editTextLabel)

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onButtonClick,
                enabled = buttonEnabled,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                border = BorderStroke(width = 2.dp, Color.White),
                elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    backgroundColor = Color.Transparent,
                    disabledBackgroundColor = Color.Transparent
                ),
            ) {
                buttonContent()
            }
        }
    }
}

@Composable
private fun SurfaceDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colors.secondary,
            modifier = modifier
        ) {
            content()
        }
    }
}

@Composable
private fun CircleDialogTextField(
    editTextValue: String,
    onEditTextValueChanged: (String) -> Unit,
    editTextLabel: @Composable (() -> Unit)? = null
) {
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = editTextValue,
        onValueChange = onEditTextValueChanged,
        label = editTextLabel,
        modifier = Modifier.focusRequester(focusRequester),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White
        )
    )

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun ExplodingFab(
    availableWidth: Dp,
    availableHeight: Dp,
    explodeState: Boolean,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    logger: Logger? = null,
    explodedSize: Dp = 300.dp,
    animationDuration: Int = 200,
    showFab: Boolean = true,
    onExplodingFinished: (() -> Unit)? = null,
    onShrinkingFinished: (() -> Unit)? = null,
    fabContent: @Composable () -> Unit
) = with(logger) {
    val animMultiplier = remember { Animatable(if (explodeState) 1f else 0f) }
    val hasNotified = remember(explodeState) { arrayOf(false) } //we just need a value holder, array is ok
    var canHideFab by remember(explodeState) { mutableStateOf(false) }

    LaunchedEffect(explodeState) {
        animMultiplier.animateTo(
            if (explodeState) 1f else 0f,
            animationSpec = tween(
                animationDuration,
            )
        )
    }

    val fabInitialSize = 56.dp
    val wantedSize = explodedSize.coerceAtMost(availableHeight).coerceAtMost(availableWidth)
    val size =
        wantedSize * animMultiplier.value - (fabInitialSize * animMultiplier.value) + fabInitialSize
    val x = (availableWidth / 2f - size / 2) * -animMultiplier.value
    val y = (availableHeight / 2f - size / 2) * -animMultiplier.value
    val padding = 16.dp - 16.dp * animMultiplier.value

    val animModifier = modifier
        .offset(x, y)
        .padding(padding)
        .size(size)
    info("available=${availableWidth to availableHeight}, size=$size, x=$x, y=$y")

    val isFinishingExploding = animMultiplier.targetValue == 1f && animMultiplier.value > 0.85f && animMultiplier.value != 1f
    warn("isFinishingExploding=$isFinishingExploding, currentVal=${animMultiplier.value}, target=${animMultiplier.targetValue}")

    if (isFinishingExploding && !hasNotified[0]) {
        debug("notifying finished exploding")
        hasNotified[0] = true
        onExplodingFinished?.invoke()
    }

    if (!showFab && !canHideFab) {
        LaunchedEffect(showFab) {
            // This delays the hiding of the fab to avoid flickers
            // when it hides just before the dialog is shown
            delay(100)
            canHideFab = true
        }
    }

    val isFinishingShrinking =
        animMultiplier.targetValue == 0f && animMultiplier.value < 0.15f && animMultiplier.value != 0f

    if (isFinishingShrinking && !hasNotified[0]) {
        debug("notifying finished shrinking")
        hasNotified[0] = true
        onShrinkingFinished?.invoke()
    }

    FloatingActionButton(
        onClick = onFabClick,
        modifier = animModifier.alpha(if (showFab || !canHideFab) 1f else 0f),
    ) {
        fabContent()
    }
}