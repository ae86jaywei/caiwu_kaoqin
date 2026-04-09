package com.financeattendance.domain.service

import com.financeattendance.data.entity.AttendanceRecord
import com.financeattendance.data.entity.FinanceRecord
import com.financeattendance.data.entity.ProjectExpenseStats
import com.financeattendance.data.entity.ProjectPersonStats
import com.financeattendance.data.repository.AttendanceRepository
import com.financeattendance.data.repository.FinanceRepository
import com.financeattendance.data.repository.PersonnelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class ProjectStatsService @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val financeRepository: FinanceRepository,
    private val personnelRepository: PersonnelRepository
) {
    private val json = Json { ignoreUnknownKeys = true }
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    suspend fun getProjectPersonStats(projectId: String): List<ProjectPersonStats> {
        return withContext(Dispatchers.IO) {
            val records = attendanceRepository.queryRecords("", "", "", projectId)
            val statsMap = HashMap<String, ProjectPersonStats>()

            for (record in records) {
                if (!statsMap.containsKey(record.personId)) {
                    val person = personnelRepository.getPersonById(record.personId)
                    statsMap[record.personId] = ProjectPersonStats(
                        personId = record.personId,
                        personName = person?.name ?: record.personId,
                        workDays = 0,
                        totalHours = 0.0
                    )
                }
                val stats = statsMap[record.personId]!!
                stats.workDays += 1
                stats.totalHours += record.totalHours
            }
            statsMap.values.toList()
        }
    }

    suspend fun getProjectExpenseStats(projectId: String): ProjectExpenseStats {
        return withContext(Dispatchers.IO) {
            val records = financeRepository.queryRecords("", "", "")
            val stats = ProjectExpenseStats(
                materialCost = 0.0,
                transportCost = 0.0,
                officeCost = 0.0,
                livingCost = 0.0,
                totalCost = 0.0
            )

            for (record in records) {
                if (record.projectId == projectId) {
                    when (record.recordType) {
                        "工程收支" -> stats.materialCost += record.amount
                        "交通费用" -> stats.transportCost += record.amount
                        "办公用品" -> stats.officeCost += record.amount
                        "生活食材" -> stats.livingCost += record.amount
                    }
                }
            }
            stats.totalCost = stats.materialCost + stats.transportCost + stats.officeCost + stats.livingCost
            stats
        }
    }
}
