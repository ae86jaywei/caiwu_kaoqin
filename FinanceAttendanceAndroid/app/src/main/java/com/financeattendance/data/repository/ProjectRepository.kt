package com.financeattendance.data.repository

import com.financeattendance.data.dao.ProjectDao
import com.financeattendance.data.entity.Project
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun addProject(project: Project) = projectDao.insert(project)
    suspend fun updateProject(project: Project) = projectDao.update(project)
    suspend fun deleteProject(project: Project) = projectDao.delete(project)
    suspend fun queryAllProjects() = projectDao.queryAllProjects()
    suspend fun getProjectById(id: String) = projectDao.getProjectById(id)
}
