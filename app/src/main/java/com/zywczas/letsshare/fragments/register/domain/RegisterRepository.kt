package com.zywczas.letsshare.fragments.register.domain

interface RegisterRepository {

    suspend fun saveLastUsedEmail(email: String)

    suspend fun isEmailFreeToUse(email: String): Boolean?

    suspend fun registerToFirebase(name: String, email: String, password: String): Int?

    suspend fun addUserToFirestore(name: String, email: String): Int?

    suspend fun sendVerificationEmail(): Int?

    suspend fun logoutFromFirebase()

}