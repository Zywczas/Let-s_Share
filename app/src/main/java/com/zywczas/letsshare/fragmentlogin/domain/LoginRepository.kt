package com.zywczas.letsshare.fragmentlogin.domain

import com.zywczas.letsshare.model.expenses.User

interface LoginRepository {

    suspend fun loginToFirebase(email: String, password: String, onSuccessAction: (User?, Int?) -> Unit)

    suspend fun saveUserLocally(user: User)

}