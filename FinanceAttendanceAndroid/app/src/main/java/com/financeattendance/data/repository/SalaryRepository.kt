package com.financeattendance.data.repository

import com.financeattendance.data.dao.SalaryDao
import com.financeattendance.data.entity.SalaryRecord
import javax.inject.Inject

class SalaryRepository @Inject constructor(
    private val salaryDao: SalaryDao
) {
    suspend fun addRecord(record: SalaryRecord) = salaryDao.insert(record)
    suspend fun updateRecord(record: SalaryRecord) = salaryDao.update(record)
    suspend fun deleteRecord(record: SalaryRecord) = salaryDao.delete(record)
    suspend fun queryRecords(personId: String?) =
        salaryDao.queryRecords(personId ?: "")
    suspend fun getRecordById(id: String) = salaryDao.getRecordById(id)
}
