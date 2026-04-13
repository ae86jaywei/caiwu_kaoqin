package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.Personnel
import com.financeattendance.data.repository.PersonnelRepository
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
class PersonnelViewModel @Inject constructor(
    private val repository: PersonnelRepository
) : ViewModel() {
    private val _persons = MutableStateFlow<List<Personnel>>(emptyList())
    val persons: StateFlow<List<Personnel>> = _persons.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadPersons() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                repository.queryAllPersons().collect { personsList ->
                    _persons.value = personsList
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载人员失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addPerson(person: Personnel) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newPerson = person.copy(
                    id = UUID.randomUUID().toString(),
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                repository.addPerson(newPerson)
                loadPersons()
            } catch (e: Exception) {
                _errorMessage.value = "添加人员失败: ${e.message}"
            }
        }
    }

    fun updatePerson(person: Personnel) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.updatePerson(person)
                loadPersons()
            } catch (e: Exception) {
                _errorMessage.value = "更新人员失败: ${e.message}"
            }
        }
    }

    fun deletePerson(person: Personnel) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.deletePerson(person)
                loadPersons()
            } catch (e: Exception) {
                _errorMessage.value = "删除人员失败: ${e.message}"
            }
        }
    }
}
