package com.zywczas.letsshare.fragments.settings.domain

import com.zywczas.letsshare.models.User

interface SettingsRepository {

    suspend fun getUser(): User

}