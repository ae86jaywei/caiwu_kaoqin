package com.financeattendance.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "salary_record")
data class SalaryRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "person_id") val personId: String,
    @ColumnInfo(name = "pay_date") val payDate: String,
    @ColumnInfo(name = "work_days") val workDays: Int,
    @ColumnInfo(name = "daily_salary") val dailySalary: Double,
    @ColumnInfo(name = "should_pay") val shouldPay: Double,
    @ColumnInfo(name = "actual_pay") val actualPay: Double = 0.0,
    @ColumnInfo(name = "remark") val remark: String? = null,
    @ColumnInfo(name = "create_time") val createTime: String
)
