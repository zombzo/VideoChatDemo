package com.edwin.videochatdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edwin.videochatdemo.ui.theme.VideoChatDemoTheme
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoChatDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VideoCallScreen()
                }
            }
        }
    }
}

@Composable
fun VideoCallScreen() {
    val coroutineScope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0x55, 0x55, 0xFF, 0xaa))
    ) {
        BoxWithConstraints(
            Modifier
                .fillMaxWidth()
                .weight(.1f)
                .padding(8.dp)
        ) {
            var animationX by remember { mutableStateOf(Animatable(0f)) }
            var animationY by remember { mutableStateOf(Animatable(0f)) }
            val videoSize = Size(140f, 180f)
            Box(
                Modifier
                    .size(videoSize.width.dp, videoSize.height.dp)
                    .offset(
                        animationX.value.dp,
                        animationY.value.dp
                    )
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .background(Color.Red)
                    .pointerInput(Unit) {
                        detectDragGestures(onDragEnd = {
                            coroutineScope.launch {
                                animationX.animateTo(
                                    (animationX.value / (maxWidth.value - videoSize.width)).roundToInt() * (maxWidth.value - videoSize.width),
                                    tween(200)
                                )
                            }
                            coroutineScope.launch {
                                animationY.animateTo(
                                    (animationY.value / (maxHeight.value - videoSize.height)).roundToInt() * (maxHeight.value - videoSize.height),
                                    tween(200)
                                )
                            }
                        }) { change, dragAmount ->
                            change.consume()
                            animationX = Animatable(
                                min(
                                    max(animationX.value + dragAmount.x / 2f, 0f),
                                    maxWidth.value - videoSize.width
                                )
                            )
                            animationY = Animatable(
                                min(
                                    max(animationY.value + dragAmount.y / 2f, 0f),
                                    maxHeight.value - videoSize.height
                                )
                            )

                        }
                    }
            ) {
                Text("Your Video", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
            Text("Their Video", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.Center
        ) {
            CircularButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.AccountCircle, null)
            }
            Spacer(Modifier.width(16.dp))
            CircularButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Call, null)
            }
            Spacer(Modifier.width(16.dp))
            CircularButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Send, null)
            }
        }
    }
}

@Composable
fun CircularButton(onClick: () -> Unit, content: @Composable (RowScope.() -> Unit)) {
    Button(onClick = onClick, content = content)
}

@Preview(showBackground = true)
@Composable
fun VideoCallScreenPreview() {
    VideoChatDemoTheme {
        VideoCallScreen()
    }
}
