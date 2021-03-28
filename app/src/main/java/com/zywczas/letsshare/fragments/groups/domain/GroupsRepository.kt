package com.zywczas.letsshare.fragments.groups.domain

import com.zywczas.letsshare.model.Group

interface GroupsRepository {

    suspend fun addGroupIfUserIsInLessThan10Groups(name: String, currency: String): Int

    suspend fun getGroups(): List<Group>?
}