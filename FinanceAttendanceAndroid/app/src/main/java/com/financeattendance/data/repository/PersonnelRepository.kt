package com.financeattendance.data.repository

import com.financeattendance.data.dao.PersonnelDao
import com.financeattendance.data.entity.Personnel
import javax.inject.Inject

class PersonnelRepository @Inject constructor(
    private val personnelDao: PersonnelDao
) {
    suspend fun addPerson(person: Personnel) = personnelDao.insert(person)
    suspend fun updatePerson(person: Personnel) = personnelDao.update(person)
    suspend fun deletePerson(person: Personnel) = personnelDao.delete(person)
    suspend fun queryAllPersons() = personnelDao.queryAllPersons()
    suspend fun getPersonById(id: String) = personnelDao.getPersonById(id)
}
