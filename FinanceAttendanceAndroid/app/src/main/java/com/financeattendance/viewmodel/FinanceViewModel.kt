package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.FinanceRecord
import com.financeattendance.data.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val repository: FinanceRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<FinanceRecord>>(emptyList())
    val records: StateFlow<List<FinanceRecord>> = _records.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadRecords("", "", "")
    }

    fun loadRecords(startDate: String, endDate: String, type: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                repository.queryRecords(startDate, endDate, type).collect { recordsList ->
                    _records.value = recordsList
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载财务记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addRecord(record: FinanceRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val newRecord = record.copy(
                    id = UUID.randomUUID().toString(),
                    createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                )
                repository.addRecord(newRecord)
                loadRecords("", "", "")
            } catch (e: Exception) {
                _errorMessage.value = "添加财务记录失败: ${e.message}"
            }
        }
    }

    fun updateRecord(record: FinanceRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.updateRecord(record)
                loadRecords("", "", "")
            } catch (e: Exception) {
                _errorMessage.value = "更新财务记录失败: ${e.message}"
            }
        }
    }

    fun deleteRecord(record: FinanceRecord) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                repository.deleteRecord(record)
                loadRecords("", "", "")
            } catch (e: Exception) {
                _errorMessage.value = "删除财务记录失败: ${e.message}"
            }
        }
    }
}
