package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain

interface GroupSettingsRepository {

    suspend fun getMembers(): List<GroupMemberDomain>?

}