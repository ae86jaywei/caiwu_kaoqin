import { relationalStore } from '@kit.ArkData';
import { AttendanceDao } from '../dao/AttendanceDao';
import { AttendanceRecord, CustomPeriod } from '../model/AttendanceRecord';
import { DatabaseHelper } from '../database/DatabaseHelper';

export class AttendanceService {
  private attendanceDao: AttendanceDao = new AttendanceDao();
  private store: relationalStore.RdbStore | null = null;

  public async init(context: Context) {
    this.store = await DatabaseHelper.getInstance().initRdbStore(context);
  }

  public async clockIn(personId: string, period: string, projectId?: string, customStart?: string, customEnd?: string): Promise<boolean> {
    if (!this.store) {
      return false;
    }
    const today = DateUtils.formatDate(new Date());
    const now = DateUtils.formatDateTime(new Date());

    let records = await this.attendanceDao.query(this.store, today, today, personId, projectId);
    if (records.length === 0) {
      const record: AttendanceRecord = {
        id: crypto.randomUUID(),
        personId: personId,
        projectId: projectId || '',
        clockDate: today,
        createTime: now
      };
      if (period === 'morning') {
        record.morningStart = now;
      } else if (period === 'afternoon') {
        record.afternoonStart = now;
      } else if (period === 'overtime') {
        record.overtimeStart = now;
      } else if (period === 'custom' && customStart && customEnd) {
        const periods: CustomPeriod[] = [{
          start: customStart,
          end: customEnd,
          hours: DateUtils.calculateHours(customStart, customEnd)
        }];
        record.customPeriods = JSON.stringify(periods);
        record.totalHours = periods[0].hours;
      }
      await this.attendanceDao.insert(this.store, record);
    } else {
      const record = records[0];
      if (period === 'morning') {
        record.morningStart = now;
      } else if (period === 'afternoon') {
        record.afternoonStart = now;
      } else if (period === 'overtime') {
        record.overtimeStart = now;
      }
      await this.attendanceDao.update(this.store, record);
    }
    return true;
  }

  public async clockOut(personId: string, period: string, projectId?: string): Promise<boolean> {
    if (!this.store) {
      return false;
    }
    const today = DateUtils.formatDate(new Date());
    const now = DateUtils.formatDateTime(new Date());

    let records = await this.attendanceDao.query(this.store, today, today, personId, projectId);
    if (records.length > 0) {
      const record = records[0];
      if (period === 'morning' && record.morningStart) {
        record.morningEnd = now;
        record.morningHours = DateUtils.calculateHours(record.morningStart, record.morningEnd);
      } else if (period === 'afternoon' && record.afternoonStart) {
        record.afternoonEnd = now;
        record.afternoonHours = DateUtils.calculateHours(record.afternoonStart, record.afternoonEnd);
      } else if (period === 'overtime' && record.overtimeStart) {
        record.overtimeEnd = now;
        record.overtimeHours = DateUtils.calculateHours(record.overtimeStart, record.overtimeEnd);
      }
      record.totalHours = record.morningHours + record.afternoonHours + record.overtimeHours;
      if (record.customPeriods) {
        const periods: CustomPeriod[] = JSON.parse(record.customPeriods);
        record.totalHours += periods.reduce((sum, p) => sum + p.hours, 0);
      }
      await this.attendanceDao.update(this.store, record);
    }
    return true;
  }

  public async queryRecords(startDate: string, endDate: string, personId?: string, projectId?: string): Promise<AttendanceRecord[]> {
    if (!this.store) {
      return [];
    }
    return await this.attendanceDao.query(this.store, startDate, endDate, personId, projectId);
  }

  public calculateWorkHours(record: AttendanceRecord): number {
    return record.morningHours + record.afternoonHours + record.overtimeHours;
  }
}
