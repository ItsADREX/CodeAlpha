package com.adam.localnotifications.model

/**
 * Data class for holding notification details.
 *
 * @property id Unique identifier for the notification.
 * @property title Title of the notification.
 * @property message Message body of the notification.
 * @property timestamp Time when the notification is scheduled (optional).
 */
data class PendingNotification(
    val id: Int,
    val title: String,
    val message: String,
    val timestamp: Long? = null  // Optional: Include if you want to store the scheduled time.
)

