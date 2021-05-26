package com.zywczas.letsshare.fragments.settings.domain

import com.zywczas.letsshare.db.UserDao
import com.zywczas.letsshare.models.User
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : SettingsRepository {

    override suspend fun getUser(): User = userDao.getUser()

}