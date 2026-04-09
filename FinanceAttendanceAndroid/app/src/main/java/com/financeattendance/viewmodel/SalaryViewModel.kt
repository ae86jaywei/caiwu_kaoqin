package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.SalaryRecord
import com.financeattendance.data.repository.SalaryRepository
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
class SalaryViewModel @Inject constructor(
    private val repository: SalaryRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<SalaryRecord>>(emptyList())
    val records: StateFlow<List<SalaryRecord>> = _records.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadRecords(personId: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _records.value = repository.queryRecords(personId)
            _isLoading.value = false
        }
    }

    fun addRecord(record: SalaryRecord) {
        viewModelScope.launch {
            val newRecord = record.copy(
                id = UUID.randomUUID().toString(),
                shouldPay = record.workDays * record.dailySalary,
                createTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            )
            repository.addRecord(newRecord)
            loadRecords(null)
        }
    }

    fun updateRecord(record: SalaryRecord) {
        viewModelScope.launch {
            repository.updateRecord(record)
            loadRecords(null)
        }
    }

    fun deleteRecord(record: SalaryRecord) {
        viewModelScope.launch {
            repository.deleteRecord(record)
            loadRecords(null)
        }
    }
}
