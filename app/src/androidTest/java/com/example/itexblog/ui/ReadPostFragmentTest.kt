package com.example.itexblog.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReadPostFragmentTest{

    @Test
    fun recreateEventFragment() {
        val factory = FragmentFactory()
        val scenario = launchFragmentInContainer<Fragment>()

        scenario.recreate()


    }

    @Test
    fun changeStateCreated() {
        val factory = FragmentFactory()
        val scenario = launchFragmentInContainer<Fragment>()

        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.moveToState(Lifecycle.State.RESUMED)

    }

    @Test
    fun changeStateResumed() {
        val factory = FragmentFactory()
        val scenario = launchFragmentInContainer<Fragment>()


        scenario.moveToState(Lifecycle.State.RESUMED)

    }
}