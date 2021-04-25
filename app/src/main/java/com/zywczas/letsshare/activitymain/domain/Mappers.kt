package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.utils.dayFormat

fun GroupMember.toDomain() = GroupMemberDomain(
    id = id,
    name = name,
    email = email,
    expenses = expenses.toBigDecimal(),
    share = share.toBigDecimal()
)

fun Expense.toDomain() = ExpenseDomain(
    id = id,
    name = name,
    payeeEmail = payeeEmail,
    payeeName = payeeName,
    value = value.toBigDecimal(),
    dateCreated = dateCreated.dayFormat()
)

fun GroupMonth.toDomain() = GroupMonthDomain(
    id = id,
    totalExpenses = totalExpenses.toBigDecimal(),
    isSettledUp = isSettledUp
)