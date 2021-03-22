package com.zywczas.letsshare.fragments.login.domain

import com.zywczas.letsshare.model.User

interface LoginRepository {

    suspend fun loginToFirebase(email: String, password: String, onSuccessAction: (User?, Int?) -> Unit)

    suspend fun saveUserLocally(user: User)

}