package com.example.itexblog.ui

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.itexblog.R
import junit.framework.Assert.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private var mActivity:MainActivity?= null

    @Before
    fun setUp() {
        mActivity = activityTestRule.activity
    }

    @Test
    fun testLaunch(){
        val view = mActivity?.findViewById<View>(R.id.fragment)
        assertNotNull(view)
    }

    @After
    fun tearDown() {
        mActivity = null
    }
}


