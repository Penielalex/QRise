package com.gebeya.eventnotifier.di


import com.example.qrise.data.db.dao.AlarmDao
import com.example.qrise.data.db.repository.AlarmDBRepositoryImpl
import com.example.qrise.domain.repository.AlarmDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {



    @Provides
    @Singleton
    fun provideAlarmDBRepository(alarmDao: AlarmDao): AlarmDBRepository {
        return AlarmDBRepositoryImpl(alarmDao)
    }

}