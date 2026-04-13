package com.financeattendance.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "work_record")
@Serializable
data class WorkRecord(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "person_id") val personId: String,
    @ColumnInfo(name = "work_date") val workDate: String,
    @ColumnInfo(name = "work_content") val workContent: String,
    @ColumnInfo(name = "project_id") val projectId: String? = null,
    @ColumnInfo(name = "create_time") val createTime: String
)
