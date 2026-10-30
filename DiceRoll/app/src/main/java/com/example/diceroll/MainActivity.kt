package com.example.diceroll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.compose.ui.geometry.CornerRadius


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { DiceRoller() } }
    }
}

@Composable
fun DiceRoller() {
    var face by remember { mutableIntStateOf(1) }
    var rolling by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun roll() {
        if (rolling) return
        scope.launch {
            rolling = true
            val spin = launch { rotation.animateTo(rotation.value + 720f, tween(900)) }
            repeat(12) {
                face = Random.nextInt(1, 7)
                delay(65)
            }
            spin.join()
            face = Random.nextInt(1, 7)
            rolling = false
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF101827)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Dice Roller", color = Color.White, fontSize = 32.sp)
            Spacer(Modifier.height(48.dp))
            Canvas(modifier = Modifier.size(180.dp).clickable(enabled = !rolling) { roll() }) {
                rotate(rotation.value) {
                    drawRoundRect(Color.White)
                    val pips = when (face) {
                        1 -> listOf(4)
                        2 -> listOf(0, 8)
                        3 -> listOf(0, 4, 8)
                        4 -> listOf(0, 2, 6, 8)
                        5 -> listOf(0, 2, 4, 6, 8)
                        else -> listOf(0, 2, 3, 5, 6, 8)
                    }
                    val step = size.width / 4f
                    pips.forEach { index ->
                        drawCircle(
                            color = Color(0xFF172238),
                            radius = size.width * 0.065f,
                            center = Offset(step * (index % 3 + 1), step * (index / 3 + 1))
                        )
                    }
                }
            }
            Spacer(Modifier.height(36.dp))
            Text(if (rolling) "Rolling..." else "You rolled $face", color = Color.White, fontSize = 22.sp)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { roll() }, enabled = !rolling, shape = RoundedCornerShape(16.dp)) {
                Text("ROLL DICE", fontSize = 18.sp)
            }
        }
    }
}
