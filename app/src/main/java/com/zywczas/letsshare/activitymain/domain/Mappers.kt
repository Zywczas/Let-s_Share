package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.model.GroupMember
import com.zywczas.letsshare.model.GroupMemberDomain

fun GroupMember.toDomain() =
    GroupMemberDomain(name, email, expenses.toBigDecimal(), percentage_share.toBigDecimal())