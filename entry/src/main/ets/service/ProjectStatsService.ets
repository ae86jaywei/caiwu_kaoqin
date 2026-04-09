import { relationalStore } from '@kit.ArkData';
import { AttendanceDao } from '../dao/AttendanceDao';
import { FinanceDao } from '../dao/FinanceDao';
import { ProjectPersonStats, ProjectExpenseStats } from '../model/Project';
import { DatabaseHelper } from '../database/DatabaseHelper';

export class ProjectStatsService {
  private attendanceDao: AttendanceDao = new AttendanceDao();
  private financeDao: FinanceDao = new FinanceDao();
  private store: relationalStore.RdbStore | null = null;

  public async init(context: Context) {
    this.store = await DatabaseHelper.getInstance().initRdbStore(context);
  }

  public async getProjectPersonStats(projectId: string): Promise<ProjectPersonStats[]> {
    if (!this.store) {
      return [];
    }
    const records = await this.attendanceDao.query(this.store, '', '', undefined, projectId);
    const statsMap: Map<string, ProjectPersonStats> = new Map();

    for (const record of records) {
      if (!statsMap.has(record.personId)) {
        statsMap.set(record.personId, {
          personId: record.personId,
          personName: record.personId, // TODO: 获取人员姓名
          workDays: 0,
          totalHours: 0
        });
      }
      const stats = statsMap.get(record.personId)!;
      stats.workDays += 1;
      stats.totalHours += record.totalHours;
    }
    return Array.from(statsMap.values());
  }

  public async getProjectExpenseStats(projectId: string): Promise<ProjectExpenseStats> {
    if (!this.store) {
      return {
        materialCost: 0,
        transportCost: 0,
        officeCost: 0,
        livingCost: 0,
        totalCost: 0
      };
    }
    const records = await this.financeDao.query(this.store, '', '', '');
    const stats: ProjectExpenseStats = {
      materialCost: 0,
      transportCost: 0,
      officeCost: 0,
      livingCost: 0,
      totalCost: 0
    };

    for (const record of records) {
      if (record.projectId === projectId) {
        if (record.recordType === '工程收支') {
          stats.materialCost += record.amount;
        } else if (record.recordType === '交通费用') {
          stats.transportCost += record.amount;
        } else if (record.recordType === '办公用品') {
          stats.officeCost += record.amount;
        } else if (record.recordType === '生活食材') {
          stats.livingCost += record.amount;
        }
      }
    }
    stats.totalCost = stats.materialCost + stats.transportCost + stats.officeCost + stats.livingCost;
    return stats;
  }
}
