package com.zywczas.letsshare.uirobots


import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.zywczas.letsshare.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

@Suppress("HasPlatformType")
class GroupDetailsFragmentRobot {

    fun isLayoutDisplayed(){
        isMainLayoutDisplayed()
        isToolbarDisplayed()
        isCollapsingToolbarDisplayed()
        isExpensesRecyclerDisplayed()
        isSpeedDialDisplayed()
        isMembersProgressBarNotDisplayed()
        isMainProgressBarNotDisplayed()
    }

    private fun isMainLayoutDisplayed() = onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))

    private fun isToolbarDisplayed() = onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

    private fun isCollapsingToolbarDisplayed() = onView(withId(R.id.collapsingToolbar)).check(matches(isDisplayed()))

    private fun isMembersRecyclerDisplayed() = onView(withId(R.id.membersRecycler)).check(matches(isDisplayed())) //todo

    private fun isExpensesRecyclerDisplayed() = onView(withId(R.id.expensesRecycler)).check(matches(isDisplayed()))

    private fun isSpeedDialDisplayed() = onView(withId(R.id.speedDial)).check(matches(isDisplayed()))

    private fun isMembersProgressBarNotDisplayed() = onView(withId(R.id.progressBarMembers)).check(matches(not(isDisplayed())))

    private fun isMainProgressBarNotDisplayed() = onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))

    fun isToolbarTitle(text: String) = onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(text))))

}