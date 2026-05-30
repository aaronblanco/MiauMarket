package com.example.miaumarket.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.delay
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

private const val CELEBRATION_GIF_URL = "https://i.pinimg.com/originals/45/a5/de/45a5de66d476b5479ebadfb8dd76ffce.gif"

@Composable
fun AddToCartCelebrationDialog(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (!visible) return

    LaunchedEffect(visible) {
        delay(2200)
        onDismiss()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val context = LocalContext.current
        val gifImageLoader = remember(context) {
            ImageLoader.Builder(context.applicationContext)
                .components {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        add(ImageDecoderDecoder.Factory())
                    } else {
                        add(GifDecoder.Factory())
                    }
                }
                .build()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.78f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = CELEBRATION_GIF_URL,
                contentDescription = "Gato feliz",
                contentScale = ContentScale.Crop,
                imageLoader = gifImageLoader,
                modifier = Modifier.size(320.dp)
            )
        }
    }
}







