package com.financeattendance.data.repository

import com.financeattendance.data.dao.WorkDao
import com.financeattendance.data.entity.WorkRecord
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val workDao: WorkDao
) {
    suspend fun addRecord(record: WorkRecord) = workDao.insert(record)
    suspend fun updateRecord(record: WorkRecord) = workDao.update(record)
    suspend fun deleteRecord(record: WorkRecord) = workDao.delete(record)
    suspend fun queryRecords(personId: String?) =
        workDao.queryRecords(personId ?: "")
    suspend fun getRecordById(id: String) = workDao.getRecordById(id)
}
