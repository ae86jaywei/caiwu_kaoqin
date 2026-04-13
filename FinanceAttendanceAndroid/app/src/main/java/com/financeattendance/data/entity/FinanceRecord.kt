package com.financeattendance.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "finance_record")
@Serializable
data class FinanceRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "record_type") val recordType: String, // 工程收支、交通费用、办公用品、生活食材、工资发放
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "project_id") val projectId: String? = null,
    @ColumnInfo(name = "project_name") val projectName: String? = null,
    @ColumnInfo(name = "remark") val remark: String? = null,
    @ColumnInfo(name = "create_time") val createTime: String
)
