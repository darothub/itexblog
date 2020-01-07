package com.example.itexblog.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.itexblog.R
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    var changeTheme:Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("secret", Context.MODE_PRIVATE)
        changeTheme = sharedPref.getBoolean("NewTheme", false)

//        Toast.makeText(this, "$changeTheme", Toast.LENGTH_LONG).show()
        if(changeTheme!!){
            setTheme(R.style.OtherTheme)
        }
        else{
            setTheme(R.style.AppTheme_NoActionBar)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        CoroutineScope(Main).launch {
            try{
                logo.visibility = View.VISIBLE
                delay(2000)
                logo.visibility = View.GONE
                splash_text.visibility = View.VISIBLE
                delay(1000)
                logo.visibility = View.VISIBLE
                splash_text.visibility = View.GONE
                delay(2000)
                logo.visibility = View.GONE
                splash_text.visibility = View.VISIBLE
                delay(1000)
                logo.visibility = View.VISIBLE
                splash_text.visibility = View.GONE
                delay(2000)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
            catch (e:Exception){

            }
        }

    }



//    override fun getTheme(): Resources.Theme {
//
//        val theme = super.getTheme()
//        if (changeTheme) {
//            theme.applyStyle(R.style.OtherTheme, true)
//        }
//
//        return theme
//
//    }
}
