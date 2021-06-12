package com.zywczas.letsshare.fragments.groupsettings.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleObserver
import com.nhaarman.mockitokotlin2.*
import com.zywczas.letsshare.R
import com.zywczas.letsshare.SessionManager
import com.zywczas.letsshare.fragments.groupsettings.domain.GroupSettingsRepository
import com.zywczas.letsshare.mockdata.GroupMemberDomainMocks
import com.zywczas.letsshare.models.Friend
import com.zywczas.letsshare.models.GroupMonthDomain
import com.zywczas.letsshare.testrules.TestCoroutineRule
import com.zywczas.letsshare.utils.LiveDataTestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.lang.System.out
import kotlin.reflect.KClass

@ExperimentalCoroutinesApi
class GroupSettingsViewModelTest {

    @get:Rule
    val coroutineTest = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val repository: GroupSettingsRepository = mock()
    private val sessionManager: SessionManager = mock()
    private val tested = GroupSettingsViewModel(TestCoroutineRule.testDispatcher, repository, sessionManager)

    private val groupMemberDomainMocks = GroupMemberDomainMocks()

    @Before
    fun setUp() = coroutineTest.runBlockingTest {
        whenever(repository.getMembers(any())).thenReturn(listOf(groupMemberDomainMocks.groupMemberDomain1, groupMemberDomainMocks.groupMemberDomain2))
    }

    @Test
    fun getMonthSettings_shouldGetTotalPercentage() = coroutineTest.runBlockingTest {
        tested.getMonthSettings(GroupMonthDomain("3.21"))
        val actual = LiveDataTestUtil.getValue(tested.totalPercentage)

        verify(repository).getMembers("3.21")
        assertThat(actual).isEqualTo("80.44%")
    }

    @Test
    fun getMonthSettings_shouldGetMember() = coroutineTest.runBlockingTest {
        val expected = listOf(groupMemberDomainMocks.groupMemberDomain1, groupMemberDomainMocks.groupMemberDomain2)


        tested.getMonthSettings(GroupMonthDomain("3.21"))
        val actual = LiveDataTestUtil.getValue(tested.members)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getMonthSettings_shouldGetMessage() = coroutineTest.runBlockingTest {
        whenever(repository.getMembers(any())).thenReturn(null)

        tested.getMonthSettings(GroupMonthDomain("3.21"))
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.cant_get_group_members)
    }

    @Test
    fun getFriends_shouldGetFriends() = coroutineTest.runBlockingTest {
        val expected = listOf(Friend("id", "email", "name"))
        whenever(repository.getFriends()).thenReturn(expected)

        tested.getFriends()
        val actual = LiveDataTestUtil.getValue(tested.friends)

        verify(repository).getFriends()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun addNewMember_shouldGetNewMembersList() = coroutineTest.runBlockingTest {
        val expected = listOf(groupMemberDomainMocks.groupMemberDomain1, groupMemberDomainMocks.groupMemberDomain2)
        val newFriend = Friend("123", "123@o2.pl", "No name")
        whenever(repository.isFriendIn5GroupsAlready(any())).thenReturn(false)
        whenever(repository.addMemberIfBelow7PeopleInGroup(any(), any())).thenReturn(null)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(newFriend)
        val actual = LiveDataTestUtil.getValue(tested.members)

        verify(repository).isFriendIn5GroupsAlready("123")
        verify(repository).addMemberIfBelow7PeopleInGroup("", newFriend)
        verify(repository, times(2)).getMembers("")
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun addNewMember_shouldBeSettingsChanged() = coroutineTest.runBlockingTest {
        whenever(repository.isFriendIn5GroupsAlready(any())).thenReturn(false)
        whenever(repository.addMemberIfBelow7PeopleInGroup(any(), any())).thenReturn(null)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend())
        val actual = LiveDataTestUtil.getValue(tested.areSettingsChanged)

        assertThat(actual).isTrue
    }

    @Test
    fun addNewMember_shouldGetMessage_whenAddMemberIfBelow7PeopleInGroupIsNull() = coroutineTest.runBlockingTest {
        val expected = R.string.something_wrong
        whenever(repository.isFriendIn5GroupsAlready(any())).thenReturn(false)
        whenever(repository.addMemberIfBelow7PeopleInGroup(any(), any())).thenReturn(expected)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend())
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun addNewMember_shouldGetMessage_whenIsFriendIn5GroupsAlreadyIsNull() = coroutineTest.runBlockingTest {
        whenever(repository.isFriendIn5GroupsAlready(any())).thenReturn(null)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend())
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.something_wrong)
    }

    @Test
    fun addNewMember_shouldGetMessage_whenIsFriendIn5GroupsAlreadyIsTrue() = coroutineTest.runBlockingTest {
        whenever(repository.isFriendIn5GroupsAlready(any())).thenReturn(true)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend())
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.friend_in_too_many_groups)
    }

    @Test
    fun addNewMember_shouldGetMessage_whenIsFriendInTheGroupAlreadyIsTrue() = coroutineTest.runBlockingTest {
        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend(id = "memberId1"))
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.member_exists)
    }

    @Test
    fun addNewMember_shouldGetMessage_whenIsFriendInTheGroupAlreadyIsNull() = coroutineTest.runBlockingTest {
        Mockito.reset(repository)

        tested.getMonthSettings(GroupMonthDomain(isSettledUp = false))
        tested.addNewMember(Friend(id = "memberId1"))
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.something_wrong)
    }

    @Test
    fun addNewMember_shouldGetMessage_whenMonthIsSettledUp() = coroutineTest.runBlockingTest {
        tested.getMonthSettings(GroupMonthDomain(isSettledUp = true))
        tested.addNewMember(Friend(id = "memberId1"))
        val actual = LiveDataTestUtil.getValue(tested.message)

        assertThat(actual).isEqualTo(R.string.cant_operate_on_settled_up_month)
    }

}