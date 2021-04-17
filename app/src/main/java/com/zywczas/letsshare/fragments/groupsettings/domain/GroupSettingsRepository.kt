package com.zywczas.letsshare.fragments.groupsettings.domain

import com.zywczas.letsshare.model.Friend
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain

interface GroupSettingsRepository {

    suspend fun getMembers(): List<GroupMemberDomain>?

    suspend fun getFriends(): List<Friend>

    suspend fun isFriendInTheGroupAlready(newMemberId: String): Boolean?

    suspend fun isFriendIn10GroupsAlready(newMemberId: String): Boolean?

    suspend fun addMemberIfBelow7InGroup(friend: Friend): Int?

    suspend fun saveSplits(members: List<GroupMemberDomain>): Int

}