package com.financeattendance.domain.service

import com.financeattendance.data.entity.ProjectExpenseStats
import com.financeattendance.data.entity.ProjectPersonStats
import com.financeattendance.data.repository.AttendanceRepository
import com.financeattendance.data.repository.FinanceRepository
import com.financeattendance.data.repository.PersonnelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProjectStatsService @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val financeRepository: FinanceRepository,
    private val personnelRepository: PersonnelRepository
) {

    suspend fun getProjectPersonStats(projectId: String): List<ProjectPersonStats> {
        return withContext(Dispatchers.IO) {
            val records = attendanceRepository.queryRecords("", "", "", projectId).first()
            val statsMap = HashMap<String, ProjectPersonStats>()

            for (record in records) {
                val currentStats = statsMap[record.personId]
                if (currentStats == null) {
                    val person = personnelRepository.getPersonById(record.personId).first()
                    statsMap[record.personId] = ProjectPersonStats(
                        personId = record.personId,
                        personName = person?.name?.takeIf { it.isNotBlank() } ?: record.personId,
                        workDays = 1,
                        totalHours = record.totalHours
                    )
                } else {
                    statsMap[record.personId] = currentStats.copy(
                        workDays = currentStats.workDays + 1,
                        totalHours = currentStats.totalHours + record.totalHours
                    )
                }
            }
            statsMap.values.toList()
        }
    }

    suspend fun getProjectExpenseStats(projectId: String): ProjectExpenseStats {
        return withContext(Dispatchers.IO) {
            val records = financeRepository.queryRecords("", "", "").first()
            var materialCost = 0.0
            var transportCost = 0.0
            var officeCost = 0.0
            var livingCost = 0.0

            for (record in records) {
                if (record.projectId == projectId) {
                    when (record.recordType) {
                        "工程收支" -> materialCost += record.amount
                        "交通费用" -> transportCost += record.amount
                        "办公用品" -> officeCost += record.amount
                        "生活食材" -> livingCost += record.amount
                    }
                }
            }
            ProjectExpenseStats(
                materialCost = materialCost,
                transportCost = transportCost,
                officeCost = officeCost,
                livingCost = livingCost,
                totalCost = materialCost + transportCost + officeCost + livingCost
            )
        }
    }
}