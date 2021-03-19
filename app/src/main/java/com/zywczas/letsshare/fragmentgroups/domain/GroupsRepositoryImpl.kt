package com.zywczas.letsshare.fragmentgroups.domain

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.zywczas.letsshare.R
import com.zywczas.letsshare.activitymain.domain.SharedPrefsWrapper
import com.zywczas.letsshare.model.Group
import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.User
import com.zywczas.letsshare.utils.*
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.Exception

class GroupsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPrefs: SharedPrefsWrapper
) : GroupsRepository {

    override suspend fun addGroup(name: String, currency: String): Int =
        try {
            val newGroupRef = firestore.collection(COLLECTION_GROUPS).document()
            val newGroup = Group(
                id = newGroupRef.id,
                founder_id = sharedPrefs.userId,
                name = name,
                date_created = Date().today(),
                currency = currency,
                members_num = 1)
            val userEmail = sharedPrefs.userEmail
            val newGroupMemberRef = firestore
                .collection(COLLECTION_GROUPS)
                .document(newGroup.id)
                .collection(COLLECTION_MEMBERS)
                .document(userEmail)
            val newMember = GroupMember(sharedPrefs.userName, userEmail)
            val userRef = firestore.collection(COLLECTION_USERS).document(userEmail) //todo sprobowac to rozbic na mniejsze funkcje, np dodaj jesli mniej niz 10 rgrup
            firestore.runTransaction { transaction ->
                transaction.get(userRef).toObject<User>()?.let { user ->
                    val newGroups: List<String> = when {
                        user.groupsIds.isEmpty() -> listOf(newGroup.id)
                        user.groupsIds.size < 10 -> user.groupsIds.plus(newGroup.id)
                        else -> return@runTransaction R.string.too_many_groups //todo sprawdzi czy to dobrze dziala, np dac ze moze byc tylko 2 grupy na chwile
                    }
                    transaction.set(newGroupRef, newGroup)
                    transaction.set(newGroupMemberRef, newMember)
                    transaction.update(userRef, FIELD_GROUPS_IDS, newGroups)
                }
            }.await()
            R.string.group_added
        } catch (e: Exception) {
            logD(e)
            R.string.cant_add_group
        }

    override suspend fun getGroups(): List<Group>? =        //todo pozniej dac jakos nasluchiwnanie czy nie pojawiaja sie nowe grupy
        try {
            val groups = mutableListOf<Group>()
            firestore.collection(COLLECTION_USERS).document(sharedPrefs.userEmail)
                .get().await().toObject<User>()?.let { user ->
                    user.groupsIds.forEach { id ->
                        firestore.collection(COLLECTION_GROUPS).document(id)
                            .get().await().toObject<Group>()?.let { group -> groups.add(group) }
                    }
                }
            groups.toList()
        } catch (e: Exception) {
            logD(e)
            null
        }

}