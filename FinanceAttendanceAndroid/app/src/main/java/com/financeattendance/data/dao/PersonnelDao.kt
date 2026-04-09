package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.Personnel

@Dao
interface PersonnelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: Personnel)

    @Update
    suspend fun update(person: Personnel)

    @Delete
    suspend fun delete(person: Personnel)

    @Query("SELECT * FROM personnel ORDER BY create_time DESC")
    suspend fun queryAllPersons(): List<Personnel>

    @Query("SELECT * FROM personnel WHERE id = :id")
    suspend fun getPersonById(id: String): Personnel?
}
