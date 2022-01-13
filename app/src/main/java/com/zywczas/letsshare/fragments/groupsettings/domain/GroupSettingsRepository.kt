package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.GroupMember

interface GroupSettingsRepository {

    suspend fun getMembers(monthId: String): List<GroupMember>?

    suspend fun getFriends(): List<Friend>

    suspend fun isFriendIn5GroupsAlready(newMemberId: String): Boolean?

    suspend fun addMemberIfBelow7PeopleInGroup(monthId: String, friend: Friend): Int?

    suspend fun removeMemberOrCloseGroup(monthId: String, memberId: String): Int?

    suspend fun userId(): String

    suspend fun saveSplits(monthId: String, members: List<GroupMember>): Int?

}