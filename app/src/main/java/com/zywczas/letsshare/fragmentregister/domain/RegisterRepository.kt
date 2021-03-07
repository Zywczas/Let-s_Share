package com.zywczas.letsshare.fragmentregister.domain

interface RegisterRepository {

    suspend fun isEmailFreeToUse(email: String, onIsEmailFreeToUseAction: (Boolean) -> Unit)

    suspend fun registerToFirebase(name: String, email: String, password: String, onSuccessAction: (Boolean) -> Unit)

    suspend fun addNewUserToFirestore(name: String, email: String, onSuccessAction: (Boolean) -> Unit)

    suspend fun sendEmailVerification(onSuccessAction: (Boolean) -> Unit)

    suspend fun logoutFromFirebase()

}