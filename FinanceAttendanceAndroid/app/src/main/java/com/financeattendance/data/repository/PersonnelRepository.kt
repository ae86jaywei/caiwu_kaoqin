package com.financeattendance.data.repository

import com.financeattendance.data.dao.PersonnelDao
import com.financeattendance.data.entity.Personnel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersonnelRepository @Inject constructor(
    private val personnelDao: PersonnelDao
) {
    suspend fun addPerson(person: Personnel): Long = personnelDao.insert(person)
    suspend fun updatePerson(person: Personnel): Int = personnelDao.update(person)
    suspend fun deletePerson(person: Personnel): Int = personnelDao.delete(person)
    
    fun queryAllPersons(): Flow<List<Personnel>> = personnelDao.queryAllPersons()
    fun getPersonById(id: String): Flow<Personnel?> = personnelDao.getPersonById(id)
}
