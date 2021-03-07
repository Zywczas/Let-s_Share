package com.zywczas.letsshare.fragmentmain.domain

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): MainRepository {

    override fun logout() = firebaseAuth.signOut()

}