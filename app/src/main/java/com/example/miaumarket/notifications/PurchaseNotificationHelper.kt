package com.example.miaumarket.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.miaumarket.domain.model.CartItem

class PurchaseNotificationHelper(private val context: Context) {

    fun showPurchaseCompleted(items: List<CartItem>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        createChannelIfNeeded()

        val productSummary = items
            .joinToString(separator = ", ") { item ->
                if (item.quantity > 1) {
                    "${item.name} x${item.quantity}"
                } else {
                    item.name
                }
            }
            .ifBlank { "productos seleccionados" }

        val totalItems = items.sumOf { it.quantity }
        val contentText = "Has comprado $totalItems producto(s): $productSummary"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Compra realizada")
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        } catch (_: SecurityException) {
            // If the runtime permission is revoked between the check and the post, we fail silently.
        }
    }

    private fun createChannelIfNeeded() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificaciones de compras completadas"
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private companion object {
        const val CHANNEL_ID = "purchase_notifications"
        const val CHANNEL_NAME = "Compras"
        const val NOTIFICATION_ID = 1001
    }
}


