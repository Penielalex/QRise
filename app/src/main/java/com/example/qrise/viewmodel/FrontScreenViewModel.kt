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
class FrontScreenViewModel @Inject constructor(
    private val alarmDBRepository: AlarmDBRepository
) : ViewModel() {

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms: StateFlow<List<Alarm>> get() = _alarms

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> get() = _message

    private val _isLoading = MutableStateFlow(false)   // Loading state
    val isLoading: StateFlow<Boolean> get() = _isLoading

    fun getAllAlarms() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                alarmDBRepository.getAllAlarms().collect { allAlarms ->
                    _alarms.value = allAlarms
                }
            } catch (e: Exception) {
                _message.value = "Failed to load alarms: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
