package com.financeattendance.data.repository

import com.financeattendance.data.dao.ProjectDao
import com.financeattendance.data.entity.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun addProject(project: Project): Long = projectDao.insert(project)
    suspend fun updateProject(project: Project): Int = projectDao.update(project)
    suspend fun deleteProject(project: Project): Int = projectDao.delete(project)
    
    fun queryAllProjects(): Flow<List<Project>> = projectDao.queryAllProjects()
    fun getProjectById(id: String): Flow<Project?> = projectDao.getProjectById(id)
}
