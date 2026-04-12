package com.financeattendance.data.repository

import com.financeattendance.data.dao.AttendanceDao
import com.financeattendance.data.entity.AttendanceRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    suspend fun addRecord(record: AttendanceRecord): Long = attendanceDao.insert(record)
    suspend fun updateRecord(record: AttendanceRecord): Int = attendanceDao.update(record)
    suspend fun deleteRecord(record: AttendanceRecord): Int = attendanceDao.delete(record)
    
    fun queryRecords(startDate: String, endDate: String, personId: String, projectId: String): Flow<List<AttendanceRecord>> =
        attendanceDao.queryRecords(startDate, endDate, personId, projectId)
    
    fun getRecordById(id: String): Flow<AttendanceRecord?> = attendanceDao.getRecordById(id)
    
    fun calculateWorkHours(record: AttendanceRecord): Double {
        return record.morningHours + record.afternoonHours + record.overtimeHours
    }
}
