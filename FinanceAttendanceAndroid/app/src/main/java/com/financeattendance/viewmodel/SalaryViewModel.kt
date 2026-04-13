package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.SalaryRecord
import com.financeattendance.data.repository.SalaryRepository
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
class SalaryViewModel @Inject constructor(
    private val repository: SalaryRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<SalaryRecord>>(emptyList())
    val records: StateFlow<List<SalaryRecord>> = _records.asStateFlow()

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
                _errorMessage.value = "加载薪资记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecord(record: SalaryRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newRecord = record.copy(
                    id = UUID.randomUUID().toString(),
                    shouldPay = record.workDays * record.dailySalary,
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                repository.addRecord(newRecord)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "添加工资记录失败: ${e.message}"
            }
        }
    }

    fun updateRecord(record: SalaryRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.updateRecord(record)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "更新工资记录失败: ${e.message}"
            }
        }
    }

    fun deleteRecord(record: SalaryRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.deleteRecord(record)
                loadRecords(null)
            } catch (e: Exception) {
                _errorMessage.value = "删除工资记录失败: ${e.message}"
            }
        }
    }
}
