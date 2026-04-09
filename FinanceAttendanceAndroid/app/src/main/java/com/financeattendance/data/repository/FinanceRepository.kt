package com.financeattendance.data.repository

import com.financeattendance.data.dao.FinanceDao
import com.financeattendance.data.entity.FinanceRecord
import javax.inject.Inject

class FinanceRepository @Inject constructor(
    private val financeDao: FinanceDao
) {
    suspend fun addRecord(record: FinanceRecord) = financeDao.insert(record)
    suspend fun updateRecord(record: FinanceRecord) = financeDao.update(record)
    suspend fun deleteRecord(record: FinanceRecord) = financeDao.delete(record)
    suspend fun queryRecords(startDate: String, endDate: String, type: String) =
        financeDao.queryRecords(startDate, endDate, type)
    suspend fun getRecordById(id: String) = financeDao.getRecordById(id)
}
