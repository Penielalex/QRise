package com.example.qrise.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.qrise.data.db.entity.Alarm

@Dao
interface AlarmDao {

    // Insert a new alarm
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm)

    // Update an existing alarm
    @Update
    suspend fun updateAlarm(alarm: Alarm)

    // Delete an alarm
    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    // Get all alarms as a Flow (auto-updates UI)
    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun getAllAlarms(): Flow<List<Alarm>>

    // Get a single alarm by ID
    @Query("SELECT * FROM alarms WHERE id = :id LIMIT 1")
    suspend fun getAlarmById(id: Int): Alarm?

    // Delete all alarms
    @Query("DELETE FROM alarms")
    suspend fun clearAlarms()
}
