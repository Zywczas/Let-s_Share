package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.R
import com.zywczas.letsshare.extentions.dayFormat
import com.zywczas.letsshare.models.*
import com.zywczas.letsshare.models.firestore.ExpenseFire
import com.zywczas.letsshare.models.firestore.GroupMemberFire
import com.zywczas.letsshare.models.firestore.GroupMonthFire
import java.math.BigDecimal

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
//todo pusuwac te rzeczy
fun GroupMonthFire.toDomain() = GroupMonthDomain(
    id = id,
    totalExpenses = totalExpenses.toBigDecimal(),
    isSettledUp = isSettledUp
)

fun List<GroupMember>.withBalance(groupTotalExpense: BigDecimal): List<GroupMember> {
    forEach { member ->
        val whatShouldPay = groupTotalExpense.multiply(member.share).divide(BigDecimal((100)))
        val balance = whatShouldPay.minus(member.expenses)
        if (balance > BigDecimal.ZERO) {
            member.owesOrOver = R.string.owes
        } else {
            member.owesOrOver = R.string.over
        }
        member.difference = balance.setScale(2, BigDecimal.ROUND_HALF_UP).abs()
    }
    return this
}