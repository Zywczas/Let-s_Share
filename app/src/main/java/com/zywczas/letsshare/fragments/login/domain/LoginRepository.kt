package com.zywczas.letsshare.fragments.login.domain

import com.zywczas.letsshare.model.User

interface LoginRepository {

    suspend fun getLastUsedEmail(): String

    suspend fun saveLastUsedEmail(email: String)

    suspend fun loginToFirebase(email: String, password: String): Int?

    suspend fun saveUserLocally(): Int?

}