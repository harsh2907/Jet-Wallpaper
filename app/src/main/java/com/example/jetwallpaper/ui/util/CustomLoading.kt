package com.example.jetwallpaper.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetwallpaper.ui.theme.UiColors

@Composable
fun CustomLoadingAnimation(
    color: Color,
    size: Size,
    modifier: Modifier = Modifier,
    duration:Int = 1500
) {
    val transition = rememberInfiniteTransition(
        label = "loading"
    )
    val rotation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                easing =  LinearEasing,
                durationMillis = duration
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "loading animation"
    )



    Canvas(
        modifier = modifier.size(48.dp) ){
            val angle = rotation.value
            withTransform({
                translate(size.width / 2f, size.height / 2f)
                rotate(angle, Offset.Zero)
            }) {
                drawCircle(color = color, radius = size.width/2, center = Offset(size.height/2,size.width/2))
            }
        }
}



@Preview
@Composable
fun CustomLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        CustomLoadingAnimation(color = UiColors.Pink, size = Size(50f,50f), duration = 1000)
        CustomLoadingAnimation(color = UiColors.HotPink, size = Size(50f,50f), duration = 1300)
        CustomLoadingAnimation(color = UiColors.DeepPink, size = Size(50f,50f))
     
    }
}