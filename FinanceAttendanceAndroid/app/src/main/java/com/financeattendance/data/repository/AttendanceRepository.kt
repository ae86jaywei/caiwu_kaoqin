package com.financeattendance.data.repository

import com.financeattendance.data.dao.AttendanceDao
import com.financeattendance.data.entity.AttendanceRecord
import javax.inject.Inject

class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    suspend fun addRecord(record: AttendanceRecord) = attendanceDao.insert(record)
    suspend fun updateRecord(record: AttendanceRecord) = attendanceDao.update(record)
    suspend fun deleteRecord(record: AttendanceRecord) = attendanceDao.delete(record)
    suspend fun queryRecords(startDate: String, endDate: String, personId: String, projectId: String) =
        attendanceDao.queryRecords(startDate, endDate, personId, projectId)
    suspend fun getRecordById(id: String) = attendanceDao.getRecordById(id)
    fun calculateWorkHours(record: AttendanceRecord): Double {
        return record.morningHours + record.afternoonHours + record.overtimeHours
    }
}
