package com.alexydenkov.jellybutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.alexydenkov.jellybutton.jelly.JellyButton
import com.alexydenkov.jellybutton.ui.theme.JellyButtonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            JellyButtonTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val isEnabled = remember { mutableStateOf(false) }
                    val animateValue = animateDpAsState(
                        targetValue = if (isEnabled.value) 300.dp else 0.dp,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.jelly),
                        contentDescription = "mountain",
                        contentScale = ContentScale.Crop
                    )
                    JellyButton(
                        Modifier
                            .offset(y = animateValue.value)
                            .align(Alignment.Center)
                            .size(300.dp, 200.dp),
                        onDragStated = {
                            isEnabled.value = !isEnabled.value
                        }
                    ) {
                        Text(
                            color = MaterialTheme.colorScheme.tertiary,
                            text = "Let's cook!",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
