package com.zywczas.letsshare.fragments.groupdetails.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zywczas.letsshare.testrules.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class GroupDetailsFragmentTest {

    @get:Rule
    val testCoroutine = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

}