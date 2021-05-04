package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.GroupMemberDomain

interface GroupSettingsRepository {

    suspend fun getMembers(monthId: String): List<GroupMemberDomain>?

    suspend fun getFriends(): List<Friend>

    suspend fun isFriendIn5GroupsAlready(newMemberId: String): Boolean?

    suspend fun addMemberIfBelow7PeopleInGroup(monthId: String, friend: Friend): Int?

    suspend fun removeMember(monthId: String, memberId: String): Int?

    suspend fun saveSplits(monthId: String, members: List<GroupMemberDomain>): Int?

}