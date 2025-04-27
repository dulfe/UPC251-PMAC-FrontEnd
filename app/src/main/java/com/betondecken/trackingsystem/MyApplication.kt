package com.betondecken.trackingsystem

import android.app.Application
import com.betondecken.trackingsystem.datasources.UserDataSource
import com.betondecken.trackingsystem.repositories.UserRepository

class MyApplication : Application() {
    private var sessionManager = SessionManager()

    val userRepository: UserRepository by lazy {
        UserRepository(
            userDataSource = UserDataSource(),
            sessionManager = sessionManager
        )
    }
}