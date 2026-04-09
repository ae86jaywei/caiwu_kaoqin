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

    fun loadProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            _projects.value = repository.queryAllProjects()
            _isLoading.value = false
        }
    }

    fun addProject(project: Project) {
        viewModelScope.launch {
            val newProject = project.copy(
                id = UUID.randomUUID().toString(),
                createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )
            repository.addProject(newProject)
            loadProjects()
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            repository.updateProject(project)
            loadProjects()
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            repository.deleteProject(project)
            loadProjects()
        }
    }

    fun selectProject(project: Project) {
        _selectedProject.value = project
        loadProjectStats(project.id)
    }

    private fun loadProjectStats(projectId: String) {
        viewModelScope.launch {
            _personStats.value = projectStatsService.getProjectPersonStats(projectId)
            _expenseStats.value = projectStatsService.getProjectExpenseStats(projectId)
        }
    }
}
