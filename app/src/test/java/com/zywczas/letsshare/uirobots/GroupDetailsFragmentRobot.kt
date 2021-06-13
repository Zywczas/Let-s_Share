package com.zywczas.letsshare.uirobots


import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.utils.childAtRecyclerPosition
import com.zywczas.letsshare.utils.recyclerViewHasSize
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

    private fun isExpensesRecyclerDisplayed() = onView(withId(R.id.expensesRecycler)).check(matches(isDisplayed()))

    private fun isSpeedDialDisplayed() = onView(withId(R.id.speedDial)).check(matches(isDisplayed()))

    private fun isMembersProgressBarNotDisplayed() = onView(withId(R.id.progressBarMembers)).check(matches(not(isDisplayed())))

    private fun isMainProgressBarNotDisplayed() = onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())))

    fun isToolbarTitle(text: String) = onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(text))))

    fun isMembersRecyclerDisplayed() = onView(withId(R.id.membersRecycler)).check(matches(isDisplayed()))

    fun membersRecyclerHasItems(number: Int) = onView(withId(R.id.membersRecycler)).check(matches(recyclerViewHasSize(number)))

    fun isFirstMemberDataDisplayed(){
        isMemberNameDisplayed(0, "Piotr")
        isTotalViewDisplayed(0)
        isSplitViewDisplayed(0)
        isOwesViewDisplayed(0, R.string.over)
        areExpensesDisplayed(0, "219.32 zł")
        isPercentageShareDisplayed(0, "50.00%")
        isDifferenceDisplayed(0, "69.32 zł")
    }

    private fun isMemberNameDisplayed(position: Int, name: String) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(allOf(withText(name), withId(R.id.memberName))), isDisplayed()))))

    private fun isTotalViewDisplayed(position: Int) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(withId(R.id.total)), isDisplayed()))))

    private fun isSplitViewDisplayed(position: Int) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(withId(R.id.split)), isDisplayed()))))

    private fun isOwesViewDisplayed(position: Int, @StringRes text: Int) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(allOf(withText(text), withId(R.id.owesOrOver))), isDisplayed()))))

    private fun areExpensesDisplayed(position: Int, text: String) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(allOf(withText(text), withId(R.id.memberExpenses))), isDisplayed()))))

    private fun isPercentageShareDisplayed(position: Int, text: String) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(allOf(withText(text), withId(R.id.percentageShare))), isDisplayed()))))

    private fun isDifferenceDisplayed(position: Int, text: String) = onView(withId(R.id.membersRecycler))
        .perform(scrollToPosition<ViewHolder>(position))
        .check(matches(childAtRecyclerPosition(position, allOf(hasDescendant(allOf(withText(text), withId(R.id.difference))), isDisplayed()))))

    fun isSecondMemberDataDisplayed(){
        isMemberNameDisplayed(1, "Michał")
        isTotalViewDisplayed(1)
        isSplitViewDisplayed(1)
        isOwesViewDisplayed(1, R.string.owes)
        areExpensesDisplayed(1, "111.11 zł")
        isPercentageShareDisplayed(1, "50.00%")
        isDifferenceDisplayed(1, "38.89 zł")
    }

    fun isSnackbarDisplayed(@StringRes text: Int) = onView(allOf(withId(com.google.android.material.R.id.snackbar_text), withText(text))).check(matches(isDisplayed()))

}