package com.example.tippy.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tippy.utilities.HelperUtilities

//Splash Screen activity for the app
/*
Starting from Android API 31 and above users should be shown a splash screen using the splash screen API
 */
class SplashActivity : AppCompatActivity() {
    private var actvityContext: Context? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (HelperUtilities.isOSSandAbove) {
            val splashScreen = installSplashScreen()
        }

        actvityContext = this@SplashActivity
        //Launch the MainActivity
        val intent = Intent(actvityContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}