package com.zywczas.letsshare.fragments.groups.domain

import com.zywczas.letsshare.model.Group

interface GroupsRepository {

    suspend fun isUserIn5GroupsAlready(): Boolean?

    suspend fun addGroup(name: String, currency: String): Int

    suspend fun getGroups(): List<Group>?

    suspend fun saveCurrentlyOpenGroupId(groupId: String)

}