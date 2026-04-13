package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.Project
import com.financeattendance.data.entity.ProjectExpenseStats
import com.financeattendance.data.entity.ProjectPersonStats
import com.financeattendance.data.repository.ProjectRepository
import com.financeattendance.domain.service.ProjectStatsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository,
    private val projectStatsService: ProjectStatsService
) : ViewModel() {
    private val _projects = MutableStateFlow<List<Project>>(emptyList())
    val projects: StateFlow<List<Project>> = _projects.asStateFlow()

    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    private val _personStats = MutableStateFlow<List<ProjectPersonStats>>(emptyList())
    val personStats: StateFlow<List<ProjectPersonStats>> = _personStats.asStateFlow()

    private val _expenseStats = MutableStateFlow<ProjectExpenseStats?>(null)
    val expenseStats: StateFlow<ProjectExpenseStats?> = _expenseStats.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadProjects() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                repository.queryAllProjects().collect { projectsList ->
                    _projects.value = projectsList
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载项目失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addProject(project: Project) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newProject = project.copy(
                    id = UUID.randomUUID().toString(),
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                repository.addProject(newProject)
                loadProjects()
            } catch (e: Exception) {
                _errorMessage.value = "添加项目失败: ${e.message}"
            }
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.updateProject(project)
                loadProjects()
            } catch (e: Exception) {
                _errorMessage.value = "更新项目失败: ${e.message}"
            }
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.deleteProject(project)
                loadProjects()
            } catch (e: Exception) {
                _errorMessage.value = "删除项目失败: ${e.message}"
            }
        }
    }

    fun selectProject(project: Project) {
        _selectedProject.value = project
        loadProjectStats(project.id)
    }

    private fun loadProjectStats(projectId: String) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                _personStats.value = projectStatsService.getProjectPersonStats(projectId)
                _expenseStats.value = projectStatsService.getProjectExpenseStats(projectId)
            } catch (e: Exception) {
                _errorMessage.value = "加载项目统计失败: ${e.message}"
            }
        }
    }
}
