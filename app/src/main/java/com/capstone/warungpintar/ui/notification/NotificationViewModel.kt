package com.capstone.warungpintar.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.warungpintar.data.ResultState
import com.capstone.warungpintar.data.local.entities.NotificationEntity
import com.capstone.warungpintar.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    var requestResult: MutableLiveData<ResultState<List<NotificationEntity>>> =
        MutableLiveData()

    init {
        getListNotification()
    }

    private fun getListNotification() {
        viewModelScope.launch {
            try {
                requestResult.value = ResultState.Loading

                notificationRepository.getNotifications().collect {
                    requestResult.value = ResultState.Success(it)
                }
            } catch (e: Exception) {
                requestResult.value = ResultState.Error(e.message.toString())
            }
        }
    }
}