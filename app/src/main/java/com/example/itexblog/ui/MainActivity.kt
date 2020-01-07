package com.example.itexblog.ui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.itexblog.R


class MainActivity : AppCompatActivity() {

    var changeTheme = false


    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = this.getSharedPreferences("secret", Context.MODE_PRIVATE)
        changeTheme = sharedPref.getBoolean("NewTheme", false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(
            this,
            R.id.fragment
        )

        if(changeTheme){
            sharedPref.edit()
                ?.apply {
                    putBoolean("NewTheme", changeTheme)
                    commit()

                }
        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment), null
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()

        findNavController(R.id.fragment).graph
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (changeTheme) {
            theme.applyStyle(R.style.OtherTheme, true)
        }

        return theme

    }




}
