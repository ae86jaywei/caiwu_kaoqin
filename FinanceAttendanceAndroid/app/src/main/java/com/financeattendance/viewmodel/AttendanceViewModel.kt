package com.financeattendance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financeattendance.data.entity.AttendanceRecord
import com.financeattendance.data.repository.AttendanceRepository
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
class AttendanceViewModel @Inject constructor(
    private val repository: AttendanceRepository
) : ViewModel() {
    private val _records = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    val records: StateFlow<List<AttendanceRecord>> = _records.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadRecords("", "", "", "")
    }

    fun loadRecords(startDate: String, endDate: String, personId: String, projectId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.queryRecords(startDate, endDate, personId, projectId).collect { recordsList ->
                _records.value = recordsList
            }
            _isLoading.value = false
        }
    }

    fun clockIn(personId: String, period: String, projectId: String?, customStart: String?, customEnd: String?) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            var existingRecord: AttendanceRecord? = null
            repository.queryRecords(today, today, personId, projectId ?: "").collect { recordsList ->
                if (recordsList.isNotEmpty()) {
                    existingRecord = recordsList[0]
                }
            }

            if (existingRecord == null) {
                val record = AttendanceRecord(
                    id = UUID.randomUUID().toString(),
                    personId = personId,
                    projectId = projectId ?: "",
                    clockDate = today,
                    createTime = now
                )
                val updatedRecord = when (period) {
                    "morning" -> record.copy(morningStart = now)
                    "afternoon" -> record.copy(afternoonStart = now)
                    "overtime" -> record.copy(overtimeStart = now)
                    "custom" -> {
                        if (customStart != null && customEnd != null) {
                            val hours = calculateHours(customStart, customEnd)
                            record.copy(customPeriods = "[{\"start\":\"$customStart\",\"end\":\"$customEnd\",\"hours\":$hours}]".also { it })
                        } else record
                    }
                    else -> record
                }
                repository.addRecord(updatedRecord)
            } else {
                val record = existingRecord!!
                val updatedRecord = when (period) {
                    "morning" -> record.copy(morningStart = now)
                    "afternoon" -> record.copy(afternoonStart = now)
                    "overtime" -> record.copy(overtimeStart = now)
                    else -> record
                }
                repository.updateRecord(updatedRecord)
            }
        }
    }

    fun clockOut(personId: String, period: String, projectId: String?) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            var existingRecord: AttendanceRecord? = null
            repository.queryRecords(today, today, personId, projectId ?: "").collect { recordsList ->
                if (recordsList.isNotEmpty()) {
                    existingRecord = recordsList[0]
                }
            }

            if (existingRecord != null) {
                val record = existingRecord!!
                val updatedRecord = when (period) {
                    "morning" -> {
                        if (record.morningStart.isNotEmpty()) {
                            val hours = calculateHours(record.morningStart, now)
                            record.copy(morningEnd = now, morningHours = hours)
                        } else record
                    }
                    "afternoon" -> {
                        if (record.afternoonStart.isNotEmpty()) {
                            val hours = calculateHours(record.afternoonStart, now)
                            record.copy(afternoonEnd = now, afternoonHours = hours)
                        } else record
                    }
                    "overtime" -> {
                        if (record.overtimeStart.isNotEmpty()) {
                            val hours = calculateHours(record.overtimeStart, now)
                            record.copy(overtimeEnd = now, overtimeHours = hours)
                        } else record
                    }
                    else -> record
                }
                val totalHours = updatedRecord.morningHours + updatedRecord.afternoonHours + updatedRecord.overtimeHours
                repository.updateRecord(updatedRecord.copy(totalHours = totalHours))
            }
        }
    }

    private fun calculateHours(start: String, end: String): Double {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startTime = sdf.parse(start)?.time ?: 0
        val endTime = sdf.parse(end)?.time ?: 0
        val hours = (endTime - startTime) / (1000 * 60 * 60)
        return Math.round(hours * 100) / 100.0
    }
}
