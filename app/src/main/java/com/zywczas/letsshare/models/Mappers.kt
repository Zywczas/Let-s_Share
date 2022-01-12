package com.zywczas.letsshare.models

import com.zywczas.letsshare.models.firestore.UserFire
import com.zywczas.letsshare.models.local.UserLocal

fun UserFire.toLocal() = UserLocal(
    id =id,
    name = name,
    email = email,
    groupsIds = groupsIds,
    messagingToken = messagingToken
)