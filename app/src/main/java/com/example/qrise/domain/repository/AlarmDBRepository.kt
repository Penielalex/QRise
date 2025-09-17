package com.example.qrise.domain.repository



import com.example.qrise.data.db.entity.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmDBRepository {

    suspend fun insertAlarm(alarm: Alarm):Result<Unit>

    suspend fun updateAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    fun getAllAlarms(): Flow<List<Alarm>>

    suspend fun getAlarmById(id: Int): Alarm?

    suspend fun clearAlarms()
}
