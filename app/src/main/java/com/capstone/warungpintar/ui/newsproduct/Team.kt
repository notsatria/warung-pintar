package com.capstone.warungpintar.ui.newsproduct

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    val name: String,
    val description: String,
    val photo: Int
): Parcelable
