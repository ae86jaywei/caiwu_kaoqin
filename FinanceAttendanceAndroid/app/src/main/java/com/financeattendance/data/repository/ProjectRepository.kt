package com.financeattendance.data.repository

import com.financeattendance.data.dao.ProjectDao
import com.financeattendance.data.entity.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun addProject(project: Project): Long = withContext(Dispatchers.IO) {
        projectDao.insert(project)
    }
    suspend fun updateProject(project: Project): Int = withContext(Dispatchers.IO) {
        projectDao.update(project)
    }
    suspend fun deleteProject(project: Project): Int = withContext(Dispatchers.IO) {
        projectDao.delete(project)
    }
    
    fun queryAllProjects(): Flow<List<Project>> = projectDao.queryAllProjects()
    fun getProjectById(id: String): Flow<Project?> = projectDao.getProjectById(id)
}