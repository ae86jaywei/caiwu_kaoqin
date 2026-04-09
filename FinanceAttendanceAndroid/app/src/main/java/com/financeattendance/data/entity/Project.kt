package com.financeattendance.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "project")
data class Project(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "customer_name") val customerName: String? = null,
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "contract_amount") val contractAmount: Double = 0.0,
    @ColumnInfo(name = "status") val status: String, // 进行中、已完成、已暂停
    @ColumnInfo(name = "remark") val remark: String? = null,
    @ColumnInfo(name = "create_time") val createTime: String
)

data class ProjectPersonStats(
    val personId: String,
    val personName: String,
    val workDays: Int,
    val totalHours: Double
)

data class ProjectExpenseStats(
    val materialCost: Double,
    val transportCost: Double,
    val officeCost: Double,
    val livingCost: Double,
    val totalCost: Double
)
