package com.zywczas.letsshare.fragments.settings.domain

import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.User
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val sharedPrefs: SharedPrefsWrapper
) : SettingsRepository {

    override suspend fun getUser(): User = sharedPrefs.getLocalUser()

}