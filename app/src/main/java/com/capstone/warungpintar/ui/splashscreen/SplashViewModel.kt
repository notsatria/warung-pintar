package com.capstone.warungpintar.ui.splashscreen

import androidx.lifecycle.ViewModel
import com.capstone.warungpintar.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userPreferences: UserPreferences) : ViewModel()  {

    val isUserLoggedIn = userPreferences.isUserLoggedIn()
}