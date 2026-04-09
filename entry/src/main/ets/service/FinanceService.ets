import { relationalStore } from '@kit.ArkData';
import { FinanceDao } from '../dao/FinanceDao';
import { FinanceRecord } from '../model/FinanceRecord';
import { DatabaseHelper } from '../database/DatabaseHelper';

export class FinanceService {
  private financeDao: FinanceDao = new FinanceDao();
  private store: relationalStore.RdbStore | null = null;

  public async init(context: Context) {
    this.store = await DatabaseHelper.getInstance().initRdbStore(context);
  }

  public async addRecord(record: FinanceRecord): Promise<boolean> {
    if (!this.store) {
      return false;
    }
    record.id = crypto.randomUUID();
    record.createTime = DateUtils.formatDate(new Date());

    await this.financeDao.insert(this.store, record);
    return true;
  }

  public async updateRecord(record: FinanceRecord): Promise<boolean> {
    if (!this.store) {
      return false;
    }
    await this.financeDao.update(this.store, record);
    return true;
  }

  public async deleteRecord(id: string): Promise<boolean> {
    if (!this.store) {
      return false;
    }
    await this.financeDao.delete(this.store, id);
    return true;
  }

  public async queryRecords(startDate: string, endDate: string, recordType: string): Promise<FinanceRecord[]> {
    if (!this.store) {
      return [];
    }
    return await this.financeDao.query(this.store, startDate, endDate, recordType);
  }

  public async exportRecords(startDate: string, endDate: string): Promise<string> {
    const records = await this.queryRecords(startDate, endDate, '');
    // TODO: export to Excel
    return '';
  }
}
