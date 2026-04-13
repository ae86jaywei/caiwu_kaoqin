package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(project: Project): Long

    @Update
    fun update(project: Project): Int

    @Delete
    fun delete(project: Project): Int

    @Query("SELECT * FROM project ORDER BY create_time DESC")
    fun queryAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM project WHERE id = :id")
    fun getProjectById(id: String): Flow<Project?>
}
