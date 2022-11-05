package com.alexydenkov.jellybutton.jelly

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class JellyState {
    PullBack,
    Resting,
}

@Composable
fun JellyButton(
    modifier: Modifier = Modifier,
    onDragStated: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val currentState = remember { mutableStateOf(JellyState.Resting) }

    val dragGesture = remember {
        mutableStateOf(0f)
    }

    val animateValue = animateFloatAsState(
        targetValue = when (
            currentState.value
        ) {
            JellyState.PullBack -> dragGesture.value
            JellyState.Resting -> 0f
        },
        animationSpec = spring(
            dampingRatio = 0.3f,
            stiffness = 200f
        )
    )

    Button(
        modifier = modifier
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { currentState.value = JellyState.Resting },
                    onDragStart = {
                        onDragStated()
                        currentState.value = JellyState.PullBack
                    }
                ) { _, dragAmount ->
                    dragGesture.value = dragAmount
                        .coerceAtMost(100f)
                        .coerceAtLeast(-100f)
                }
            }
            .graphicsLayer {
                shape = ButtonShape(60f, animateValue.value)
                clip = true
            }
        ,
        onClick = {
            dragGesture.value = 100f
            currentState.value = JellyState.PullBack
            coroutineScope.launch(Dispatchers.Main) {
                delay(100)
                currentState.value = JellyState.Resting
            }
        }
    ) {
        Row(
            modifier = Modifier
                .offset(y = with(LocalDensity.current) { (animateValue.value * 0.66f).toDp() })
        ) {
            content()
        }
    }
}