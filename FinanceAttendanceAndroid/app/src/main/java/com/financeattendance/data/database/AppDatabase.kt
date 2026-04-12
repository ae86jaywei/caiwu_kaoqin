package com.financeattendance.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.financeattendance.data.dao.AttendanceDao
import com.financeattendance.data.dao.FinanceDao
import com.financeattendance.data.dao.PersonnelDao
import com.financeattendance.data.dao.ProjectDao
import com.financeattendance.data.dao.SalaryDao
import com.financeattendance.data.dao.WorkDao
import com.financeattendance.data.entity.AttendanceRecord
import com.financeattendance.data.entity.FinanceRecord
import com.financeattendance.data.entity.Personnel
import com.financeattendance.data.entity.Project
import com.financeattendance.data.entity.SalaryRecord
import com.financeattendance.data.entity.WorkRecord

@Database(
    entities = [
        FinanceRecord::class,
        AttendanceRecord::class,
        SalaryRecord::class,
        Personnel::class,
        WorkRecord::class,
        Project::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun salaryDao(): SalaryDao
    abstract fun personnelDao(): PersonnelDao
    abstract fun workDao(): WorkDao
    abstract fun projectDao(): ProjectDao

    companion object {
        private const val DATABASE_NAME = "finance_attendance.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
