package com.example.qrise.data.db.repository




import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteFullException
import android.util.Log
import com.example.qrise.data.db.dao.AlarmDao
import com.example.qrise.data.db.entity.Alarm
import com.example.qrise.domain.repository.AlarmDBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlarmDBRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmDBRepository {

    override suspend fun insertAlarm(alarm: Alarm): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                alarmDao.insertAlarm(alarm)
                Result.success(Unit)
            } catch (e: SQLiteConstraintException) {
                Log.e("AlarmRepo", "Insert failed: Constraint violation - ${e.message}", e)
                Result.failure(e)
            } catch (e: SQLiteFullException) {
                Log.e("AlarmRepo", "Insert failed: Database full - ${e.message}", e)
                Result.failure(e)
            } catch (e: IllegalStateException) {
                Log.e("AlarmRepo", "Insert failed: Illegal state - ${e.message}", e)
                Result.failure(e)
            } catch (e: Exception) {
                Log.e("AlarmRepo", "Insert failed: Unexpected error - ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    override fun getAllAlarms(): Flow<List<Alarm>> {
        return alarmDao.getAllAlarms()
    }

    override suspend fun getAlarmById(id: Int): Alarm? {
        return alarmDao.getAlarmById(id)
    }

    override suspend fun clearAlarms() {
        alarmDao.clearAlarms()
    }
}
