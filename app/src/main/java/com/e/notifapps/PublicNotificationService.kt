package com.e.notifapps

import com.google.firebase.messaging.FirebaseMessagingService

class PublicNotificationService:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}