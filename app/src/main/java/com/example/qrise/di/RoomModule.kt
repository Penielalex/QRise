package com.example.qrise.di



import android.app.Application
import androidx.room.Room
import com.example.qrise.data.db.AppDatabase
import com.example.qrise.data.db.dao.AlarmDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = AppDatabase::class.java,
            name = "alarm_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAlarmDao(appDatabase: AppDatabase): AlarmDao {
        return appDatabase.alarmDao()
    }



}