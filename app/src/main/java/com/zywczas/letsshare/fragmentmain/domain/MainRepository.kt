package com.zywczas.letsshare.fragmentmain.domain

import android.provider.ContactsContract
import com.zywczas.letsshare.model.User

interface MainRepository {

    suspend fun logout()

    suspend fun addFriendByEmail(email: String, onFinishAction: (Int) -> Unit )

}