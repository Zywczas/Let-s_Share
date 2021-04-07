package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain

interface GroupSettingsRepository {

    suspend fun getMembers(): List<GroupMemberDomain>?

    suspend fun getFriends(): List<Friend>

    suspend fun isFriendInTheGroupAlready(newMemberEmail: String): Boolean?

    suspend fun isFriendIn10GroupsAlready(newMemberEmail: String): Boolean?

    suspend fun addNewMemberIfBelow7InGroup(newMember: GroupMember): Int?

}