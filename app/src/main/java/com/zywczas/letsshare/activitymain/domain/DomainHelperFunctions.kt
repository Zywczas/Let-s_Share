package com.zywczas.letsshare.activitymain.domain

import com.zywczas.letsshare.R
import com.zywczas.letsshare.model.*
import com.zywczas.letsshare.utils.dayFormat
import java.math.BigDecimal

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

fun List<GroupMemberDomain>.withBalance(groupTotalExpense: BigDecimal): List<GroupMemberDomain> {
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