package com.zywczas.letsshare.fragments.settings.domain

import com.zywczas.letsshare.models.local.UserLocal

interface SettingsRepository {

    suspend fun getUser(): UserLocal

}