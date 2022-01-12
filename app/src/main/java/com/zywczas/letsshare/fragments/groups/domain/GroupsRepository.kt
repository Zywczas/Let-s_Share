package com.zywczas.letsshare.fragments.groups.domain

import com.zywczas.letsshare.models.firestore.GroupFire
import com.zywczas.letsshare.models.firestore.UserFire
import kotlinx.coroutines.flow.Flow

interface GroupsRepository {

    suspend fun isUserIn5GroupsAlready(): Boolean?

    suspend fun addGroup(name: String, currency: String): Int?

    suspend fun getUser(): Flow<UserFire>

    suspend fun getGroups(groupsIds: List<String>): List<GroupFire>?

    suspend fun saveCurrentlyOpenGroupId(groupId: String)

}