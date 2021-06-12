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

@Suppress("HasPlatformType")
class GroupDetailsFragmentRobot {

    fun isLayoutDisplayed(){
        isMainLayoutDisplayed()
//        isToolbarDisplayed()
//        isRecyclerDisplayed()
    }

    private fun isMainLayoutDisplayed() = onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))

    private fun isToolbarDisplayed() = onView(withId(R.id.toolbar)).check(matches(isDisplayed()))

    private fun isRecyclerDisplayed() = onView(withId(R.id.expensesRecycler)).check(matches(isDisplayed()))


}