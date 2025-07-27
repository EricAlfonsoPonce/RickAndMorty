package es.ericalfonsoponce.presentation.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.text.LinkAnnotation

inline fun Modifier.debounceClickable(
    debounceInterval: Long = 400,
    crossinline onClick: () -> Unit,
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    clickable {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastClickTime) < debounceInterval) return@clickable
        lastClickTime = currentTime
        onClick()
    }
}

@Composable
fun debounceOnClick(
    debounceInterval: Long = 400L,
    onClick: () -> Unit
): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return remember(onClick) {
        {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastClickTime) >= debounceInterval) {
                lastClickTime = currentTime
                onClick()
            }
        }
    }
}

@Composable
fun debounceOnClickLinkAnnotation(
    debounceInterval: Long = 400L,
    onClick: () -> Unit
): ((LinkAnnotation) -> Unit)? {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return remember(onClick) {
        {
            val currentTime = System.currentTimeMillis()
            if ((currentTime - lastClickTime) >= debounceInterval) {
                lastClickTime = currentTime
                onClick()
            }
        }
    }
}