package com.financeattendance.data.repository

import com.financeattendance.data.dao.AttendanceDao
import com.financeattendance.data.entity.AttendanceRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    suspend fun addRecord(record: AttendanceRecord): Long = withContext(Dispatchers.IO) {
        attendanceDao.insert(record)
    }
    suspend fun updateRecord(record: AttendanceRecord): Int = withContext(Dispatchers.IO) {
        attendanceDao.update(record)
    }
    suspend fun deleteRecord(record: AttendanceRecord): Int = withContext(Dispatchers.IO) {
        attendanceDao.delete(record)
    }
    
    fun queryRecords(startDate: String, endDate: String, personId: String, projectId: String): Flow<List<AttendanceRecord>> =
        attendanceDao.queryRecords(startDate, endDate, personId, projectId)
    
    fun getRecordById(id: String): Flow<AttendanceRecord?> = attendanceDao.getRecordById(id)
    
    fun calculateWorkHours(record: AttendanceRecord): Double {
        return record.morningHours + record.afternoonHours + record.overtimeHours
    }
}