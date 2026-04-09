package com.financeattendance.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "attendance_record")
@Serializable
data class AttendanceRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "person_id") val personId: String,
    @ColumnInfo(name = "project_id") val projectId: String? = null,
    @ColumnInfo(name = "clock_date") val clockDate: String,
    @ColumnInfo(name = "morning_start") val morningStart: String? = null,
    @ColumnInfo(name = "morning_end") val morningEnd: String? = null,
    @ColumnInfo(name = "morning_hours") val morningHours: Double = 0.0,
    @ColumnInfo(name = "afternoon_start") val afternoonStart: String? = null,
    @ColumnInfo(name = "afternoon_end") val afternoonEnd: String? = null,
    @ColumnInfo(name = "afternoon_hours") val afternoonHours: Double = 0.0,
    @ColumnInfo(name = "overtime_start") val overtimeStart: String? = null,
    @ColumnInfo(name = "overtime_end") val overtimeEnd: String? = null,
    @ColumnInfo(name = "overtime_hours") val overtimeHours: Double = 0.0,
    @ColumnInfo(name = "custom_periods") val customPeriods: String? = null, // JSON字符串
    @ColumnInfo(name = "total_hours") val totalHours: Double = 0.0,
    @ColumnInfo(name = "create_time") val createTime: String
)

@Serializable
data class CustomPeriod(
    val start: String,
    val end: String,
    val hours: Double
)
