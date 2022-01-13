package com.zywczas.letsshare.models

import com.zywczas.letsshare.extentions.dayFormat
import com.zywczas.letsshare.models.firestore.ExpenseFire
import com.zywczas.letsshare.models.firestore.GroupMemberFire
import com.zywczas.letsshare.models.firestore.GroupMonthFire

fun GroupMemberFire.toDomain() = GroupMember(
    id = id,
    name = name,
    email = email,
    expenses = expenses.toBigDecimal(),
    share = share.toBigDecimal()
)

fun ExpenseFire.toDomain() = Expense(
    id = id,
    name = name,
    payeeId = payeeId,
    payeeName = payeeName,
    value = value.toBigDecimal(),
    dateCreated = dateCreated.dayFormat()
)

fun GroupMonthFire.toDomain() = GroupMonth(
    id = id,
    totalExpenses = totalExpenses.toBigDecimal(),
    isSettledUp = isSettledUp
)