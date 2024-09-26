package tosbik.ao.parayonetimi.ui.components

import android.annotation.SuppressLint
import android.util.Size
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.BiasAlignment

@SuppressLint("UnrememberedMutableState")
@Composable
fun animateHorizontalAlignmentAsState(
    targetBiasValue: Float
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(targetBiasValue, label = "")
    return derivedStateOf { BiasAlignment.Horizontal(bias) }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun animateSizeAsState(
    targetWidth: Float,
): State<Float> {
    val width by animateFloatAsState(targetWidth, label = "")
    return derivedStateOf { width }
}