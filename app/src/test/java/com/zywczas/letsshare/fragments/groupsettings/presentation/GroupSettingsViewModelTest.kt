package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.testrules.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GroupSettingsViewModelTest {

    @get:Rule
    val coroutineTest = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: GroupSettingsRepository = mock()
    private val sessionManager: SessionManager = mock()
    private val tested = GroupSettingsViewModel(TestCoroutineRule.testDispatcher, repository, sessionManager)

    @Test
    fun getFriendsShouldGetFriends() = coroutineTest.runBlockingTest {

    }

}