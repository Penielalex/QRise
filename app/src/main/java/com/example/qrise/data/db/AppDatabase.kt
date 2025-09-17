package com.example.qrise.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.qrise.data.db.dao.AlarmDao
import com.example.qrise.data.db.entity.Alarm

@Database(entities = [Alarm::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}