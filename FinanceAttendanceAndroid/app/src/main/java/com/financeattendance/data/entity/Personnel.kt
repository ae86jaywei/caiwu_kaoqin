package com.financeattendance.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity(tableName = "personnel")
@Serializable
data class Personnel(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "person_type") val personType: String, // 固定员工、临时工
    @ColumnInfo(name = "reference_salary") val referenceSalary: Double = 0.0, // 参考日工资标准
    @ColumnInfo(name = "phone") val phone: String? = null,
    @ColumnInfo(name = "join_date") val joinDate: String? = null,
    @ColumnInfo(name = "create_time") val createTime: String
)
