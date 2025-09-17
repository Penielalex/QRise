package com.example.qrise.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hour: Int,                 // 0-23
    val minute: Int,               // 0-59
    val name: String,
    val daysOfWeek: String,        // e.g. "Su,M,T,W,Th,F,S" (comma-separated)
    val soundName: String,         // e.g. "Beep", "Rock n Role"
    val isEnabled: Boolean = true  // alarm on/off
)
