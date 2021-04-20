package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMemberDomain

interface GroupSettingsRepository {

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getFriends(): List<Friend>

    suspend fun isFriendIn5GroupsAlready(newMemberId: String): Boolean?

    suspend fun addMemberIfBelow7PeopleInGroup(monthId: String, friend: Friend): Int?

    suspend fun saveSplits(monthId: String, members: List<GroupMemberDomain>): Int

}