package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockitokotlin2.*
import com.zywczas.letsshare.BaseApp
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.di.factories.UniversalViewModelFactory
import com.zywczas.letsshare.fragments.groupdetails.domain.GroupDetailsRepository
import com.zywczas.letsshare.fragments.groups.presentation.GroupsFragmentDirections
import com.zywczas.letsshare.models.Group
import com.zywczas.letsshare.testrules.TestCoroutineRule
import com.zywczas.letsshare.uirobots.GroupDetailsFragmentRobot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
class GroupDetailsFragmentTest {

    @get:Rule
    val coroutineTest = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val navController = TestNavHostController(ApplicationProvider.getApplicationContext<BaseApp>())
    private val sessionManager: SessionManager = mock()
    private val repository: GroupDetailsRepository = mock()

    private val uiRobot = GroupDetailsFragmentRobot()

    private fun launchGroupDetailsFragment() : FragmentScenario<GroupDetailsFragment>{
        val bundle = GroupsFragmentDirections.toGroupDetailsFragment(Group(
            id = "id1",
            name = "Dom",
            dateCreated = Date(0),
            currency = "zł",
            membersNum = 3
        )).arguments
        val viewModelFactory = spy(UniversalViewModelFactory(emptyMap()))
        val viewModel = GroupDetailsViewModel(TestCoroutineRule.testDispatcher, repository, sessionManager)

        doReturn(viewModel).`when`(viewModelFactory).create(GroupDetailsViewModel::class.java)
        navController.setGraph(R.navigation.main_nav_graph)
        navController.setCurrentDestination(R.id.groupDetailsFragment)

        return launchFragment(fragmentArgs = bundle, themeResId = R.style.Theme_LetsShare){
            GroupDetailsFragment(viewModelFactory).also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                    if (viewLifeCycleOwner != null) {
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }
    }

    @Test
    fun startFragment_shouldGetLayoutDisplayed() = coroutineTest.runBlockingTest {
        val scenario = launchGroupDetailsFragment()

        uiRobot.isLayoutDisplayed()
    }

}