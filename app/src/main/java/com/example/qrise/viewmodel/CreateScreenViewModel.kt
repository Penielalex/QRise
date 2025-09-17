package com.example.qrise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrise.data.db.entity.Alarm
import com.example.qrise.domain.repository.AlarmDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateScreenViewModel @Inject constructor(
    private val alarmDBRepository: AlarmDBRepository
) : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> get() = _message

    /**
     * Save a new alarm to Room DB
     */
    fun saveAlarm(
        hour: Int,
        minute: Int,
        name: String,
        daysOfWeek: List<String>,
        soundName: String
    ) {
        val daysAsString = daysOfWeek.joinToString(separator = ",")
        viewModelScope.launch {
            val alarm = Alarm(
                hour = hour,
                minute = minute,
                name = name,
                daysOfWeek = daysAsString,
                soundName = soundName,
                isEnabled = true
            )

            val result = alarmDBRepository.insertAlarm(alarm)

            result.onSuccess {
                _message.value = "yes"
            }.onFailure { error ->
                _message.value = "no"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
