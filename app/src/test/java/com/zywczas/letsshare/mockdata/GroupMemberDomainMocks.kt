package com.zywczas.letsshare.mockdata

import com.zywczas.letsshare.R
import com.zywczas.letsshare.models.GroupMemberDomain
import java.math.BigDecimal

class GroupMemberDomainMocks {

    private val defaultGroupMemberDomain = GroupMemberDomain(
        id = "",
        name = "",
        email = "",
        expenses = BigDecimal.ZERO,
        share = BigDecimal("100.00"),
        owesOrOver = android.R.string.unknownName,
        difference = BigDecimal.ZERO
    )

    val groupMemberDomain1 = GroupMemberDomain(
        id = "someIdNumber1",
        name = "Piotr",
        email = "piotr@gmail.com",
        expenses = BigDecimal("219.32"),
        share = BigDecimal("50.00"),
        owesOrOver = R.string.over,
        difference = BigDecimal("120.99")
    )

    val groupMemberDomain2 = GroupMemberDomain(
        id = "someIdNumber2",
        name = "Michał",
        email = "michal@gmail.com",
        expenses = BigDecimal("111.11"),
        share = BigDecimal("30.44"),
        owesOrOver = R.string.owes,
        difference = BigDecimal("37.64")
    )

}