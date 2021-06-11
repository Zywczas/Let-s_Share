package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.testrules.TestCoroutineRule
import com.zywczas.letsshare.utils.LiveDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
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
        val expected = listOf(Friend("id", "email", "name"))
        whenever(repository.getFriends()).thenReturn(expected)

        tested.getFriends()
        val actual = LiveDataTestUtil.getValue(tested.friends)

        verify(repository).getFriends()
        assertThat(actual).isEqualTo(expected)
    }

}