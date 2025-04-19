package com.capstone.warungpintar.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.capstone.warungpintar.R
import com.capstone.warungpintar.ui.dashboard.DashboardProduct
import com.capstone.warungpintar.ui.welcoming.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            viewModel.isUserLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    val intent = Intent(this@SplashScreen, DashboardProduct::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashScreen, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            delay(2000)
        }

//        val userPref = UserPreferences.getInstance(this)
//        lifecycleScope.launch {
//            val token = userPref.getToken().first()
//            if (token.isNullOrEmpty()) {
//                startActivity(Intent(this@SplashScreen, WelcomeActivity::class.java))
//                finish()
//            } else {
//                val intent = Intent(this@SplashScreen, MainActivity::class.java)
//                intent.putExtra("TOKEN", token)
//                startActivity(intent)
//                finish()
//            }
//        }
    }
}