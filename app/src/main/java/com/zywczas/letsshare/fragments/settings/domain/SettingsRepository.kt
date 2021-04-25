package com.zywczas.letsshare.fragments.settings.domain

import com.zywczas.letsshare.model.User

interface SettingsRepository {

    suspend fun getUser(): User

}