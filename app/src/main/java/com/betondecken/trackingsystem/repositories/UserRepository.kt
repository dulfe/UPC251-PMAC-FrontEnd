package com.betondecken.trackingsystem.repositories

import com.betondecken.trackingsystem.datasources.UserDataSource
import com.betondecken.trackingsystem.datasources.entities.AccessToken

class UserRepository(val userDataSource: UserDataSource) {


    suspend fun login(username: String, password: String): AccessToken {
        return userDataSource.login(username, password)
    }
}