package com.book.mybook


import android.app.Application
import com.book.mybook.api.SessionManager

class MyBookApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialiser le gestionnaire de session
        SessionManager.init(applicationContext)
    }
}