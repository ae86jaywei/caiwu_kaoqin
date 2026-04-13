package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.WorkRecord
import com.financeattendance.data.repository.WorkRepository
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
class WorkViewModel @Inject constructor(
    private val repository: WorkRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<WorkRecord>>(emptyList())
    val records: StateFlow<List<WorkRecord>> = _records.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadRecords(personId: String?) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                repository.queryRecords(personId).collect { recordsList ->
                    _records.value = recordsList
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载工作记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecord(record: WorkRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newRecord = record.copy(
                    id = UUID.randomUUID().toString(),
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                repository.addRecord(newRecord)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "添加工作记录失败: ${e.message}"
            }
        }
    }

    fun updateRecord(record: WorkRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.updateRecord(record)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "更新工作记录失败: ${e.message}"
            }
        }
    }

    fun deleteRecord(record: WorkRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.deleteRecord(record)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "删除工作记录失败: ${e.message}"
            }
        }
    }
}
