import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { FinanceRecord } from '../model/FinanceRecord';

export class FinanceDao {
  public async insert(store: relationalStore.RdbStore, record: FinanceRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': record.id,
      'record_type': record.recordType,
      'amount': record.amount,
      'date': record.date,
      'project_id': record.projectId,
      'project_name': record.projectName,
      'remark': record.remark,
      'create_time': record.createTime
    };

    try {
      let promise = store.insert('finance_record', valueBucket);
      promise.then((rowId: number) => {
        console.log(`insert is successful, rowId = ${rowId}`);
      }).catch((err: BusinessError) => {
        console.error(`insert is failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`insert failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return 0;
  }

  public async query(store: relationalStore.RdbStore, startDate: string, endDate: string, recordType: string): Promise<FinanceRecord[]> {
    let predicates = new relationalStore.RdbPredicates("finance_record");
    if (startDate && endDate) {
      predicates = predicates.between("date", startDate, endDate);
    }
    if (recordType) {
      predicates = predicates.equalTo("record_type", recordType);
    }
    predicates = predicates.orderByDesc("date");

    let promise = store.query(predicates, ['id', 'record_type', 'amount', 'date', 'project_id', 'project_name', 'remark', 'create_time']);
    let records: FinanceRecord[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const recordType = resultSet.getString(resultSet.getColumnIndex('record_type'));
          const amount = resultSet.getDouble(resultSet.getColumnIndex('amount'));
          const date = resultSet.getString(resultSet.getColumnIndex('date'));
          const projectId = resultSet.getString(resultSet.getColumnIndex('project_id'));
          const projectName = resultSet.getString(resultSet.getColumnIndex('project_name'));
          const remark = resultSet.getString(resultSet.getColumnIndex('remark'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const record: FinanceRecord = {
            id,
            recordType,
            amount,
            date,
            projectId,
            projectName,
            remark,
            createTime
          };
          records.push(record);
        }
        resultSet.close();
      }).catch((err: BusinessError) => {
        console.error(`query failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`query failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return records;
  }

  public async update(store: relationalStore.RdbStore, record: FinanceRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'record_type': record.recordType,
      'amount': record.amount,
      'date': record.date,
      'project_id': record.projectId,
      'project_name': record.projectName,
      'remark': record.remark,
    };
    let predicates = new relationalStore.RdbPredicates("finance_record");
    predicates.equalTo('id', record.id);
    try {
      let promise = store.update(valueBucket, predicates);
      promise.then((rows: number) => {
        console.log(`update is successful, rows affected = ${rows}`);
      }).catch((err: BusinessError) => {
        console.error(`update failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`update failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return 0;
  }

  public async delete(store: relationalStore.RdbStore, id: string): Promise<number> {
    let predicates = new relationalStore.RdbPredicates("finance_record");
    predicates.equalTo('id', id);
    try {
      let promise = store.delete(predicates);
      promise.then((rows: number) => {
        console.log(`delete is successful, rows affected = ${rows}`);
      }).catch((err: BusinessError) => {
        console.error(`delete failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`delete failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return 0;
  }
}
