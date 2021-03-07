package com.zywczas.letsshare.fragmentlogin.domain

interface LoginRepository {

    suspend fun loginToFirebase(email: String, password: String, onSuccessAction: (Boolean, Int?) -> Unit)

}