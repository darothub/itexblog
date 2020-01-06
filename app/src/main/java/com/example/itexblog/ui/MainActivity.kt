package com.example.itexblog.ui

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.itexblog.R


class MainActivity : AppCompatActivity() {

    var changeTheme = false


    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        changeTheme = sharedPref.getBoolean("NewTheme", false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = Navigation.findNavController(
            this,
            R.id.fragment
        )


//        if(bvalue){
//            setTheme(R.style.OtherTheme)
//        }
        Toast.makeText(this, "$changeTheme", Toast.LENGTH_LONG).show()

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
        // you could also use a switch if you have many themes that could apply
        // you could also use a switch if you have many themes that could apply
        return theme

    }

    fun setNewTheme(){

        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
//        setTheme(R.style.OtherTheme);
    }



}
