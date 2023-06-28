package com.example.headlines

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle the received message and show the notification
        // You can customize the notification content here
        // Refer to the next step for creating a notification
    }
}
