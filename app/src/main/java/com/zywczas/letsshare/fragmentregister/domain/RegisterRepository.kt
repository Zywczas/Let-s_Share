package com.zywczas.letsshare.fragmentregister.domain

interface RegisterRepository {

    suspend fun isEmailFreeToUse(email: String, onIsEmailFreeToUseAction: (Boolean) -> Unit)

    fun registerToFirebase(email: String, password: String, onSuccessAction: (Boolean) -> Unit)

}