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

    var i = 1 //todo sprawdzic czy za kazdym razem jest nowa instancja klasy

    @get:Rule
    val testCoroutine = TestCoroutineRule()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule().also {
        println(i)
        i++
    }

    @Test
    fun test1(){

    }
    @Test
    fun test5(){

    }

    @Test
    fun test4(){

    }

    @Test
    fun test3(){

    }

    @Test
    fun test2(){

    }

}