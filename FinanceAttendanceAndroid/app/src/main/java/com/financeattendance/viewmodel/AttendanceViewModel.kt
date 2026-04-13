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
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadRecords("", "", "", "")
    }

    fun loadRecords(startDate: String, endDate: String, personId: String, projectId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                repository.queryRecords(startDate, endDate, personId, projectId).collect { recordsList ->
                    _records.value = recordsList
                }
            } catch (e: Exception) {
                _errorMessage.value = "加载记录失败: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clockIn(personId: String, period: String, projectId: String?, customStart: String?, customEnd: String?) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val recordsList = repository.queryRecords(today, today, personId, projectId ?: "").first()
                val existingRecord = recordsList.firstOrNull()

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
                    val updatedRecord = when (period) {
                        "morning" -> existingRecord.copy(morningStart = now)
                        "afternoon" -> existingRecord.copy(afternoonStart = now)
                        "overtime" -> existingRecord.copy(overtimeStart = now)
                        else -> existingRecord
                    }
                    repository.updateRecord(updatedRecord)
                }
            } catch (e: Exception) {
                _errorMessage.value = "打卡失败: ${e.message}"
            }
        }
    }

    fun clockOut(personId: String, period: String, projectId: String?) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val recordsList = repository.queryRecords(today, today, personId, projectId ?: "").first()
                val existingRecord = recordsList.firstOrNull()

                if (existingRecord != null) {
                    val updatedRecord = when (period) {
                        "morning" -> {
                            existingRecord.morningStart?.takeIf { it.isNotEmpty() }?.let { start ->
                                val hours = calculateHours(start, now)
                                existingRecord.copy(morningEnd = now, morningHours = hours)
                            } ?: existingRecord
                        }
                        "afternoon" -> {
                            existingRecord.afternoonStart?.takeIf { it.isNotEmpty() }?.let { start ->
                                val hours = calculateHours(start, now)
                                existingRecord.copy(afternoonEnd = now, afternoonHours = hours)
                            } ?: existingRecord
                        }
                        "overtime" -> {
                            existingRecord.overtimeStart?.takeIf { it.isNotEmpty() }?.let { start ->
                                val hours = calculateHours(start, now)
                                existingRecord.copy(overtimeEnd = now, overtimeHours = hours)
                            } ?: existingRecord
                        }
                        else -> existingRecord
                    }
                    val totalHours: Double = updatedRecord.morningHours + updatedRecord.afternoonHours + updatedRecord.overtimeHours
                    repository.updateRecord(updatedRecord.copy(totalHours = totalHours))
                }
            } catch (e: Exception) {
                _errorMessage.value = "打卡失败: ${e.message}"
            }
        }
    }

    private fun calculateHours(start: String, end: String): Double {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        try {
            val startTime = sdf.parse(start)?.time ?: throw IllegalArgumentException("无效的开始时间格式: $start")
            val endTime = sdf.parse(end)?.time ?: throw IllegalArgumentException("无效的结束时间格式: $end")
            
            if (endTime <= startTime) {
                throw IllegalArgumentException("结束时间必须晚于开始时间: $start - $end")
            }
            
            val hours = (endTime - startTime).toDouble() / (1000 * 60 * 60)
            return Math.round(hours * 100) / 100.0
        } catch (e: Exception) {
            _errorMessage.value = "计算工时失败: ${e.message}"
            return 0.0
        }
    }
}
