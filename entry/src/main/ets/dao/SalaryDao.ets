import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { SalaryRecord } from '../model/SalaryRecord';

export class SalaryDao {
  public async insert(store: relationalStore.RdbStore, record: SalaryRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': record.id,
      'person_id': record.personId,
      'pay_date': record.payDate,
      'work_days': record.workDays,
      'daily_salary': record.dailySalary,
      'should_pay': record.shouldPay,
      'actual_pay': record.actualPay,
      'remark': record.remark,
      'create_time': record.createTime
    };

    try {
      let promise = store.insert('salary_record', valueBucket);
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

  public async query(store: relationalStore.RdbStore, personId?: string): Promise<SalaryRecord[]> {
    let predicates = new relationalStore.RdbPredicates("salary_record");
    if (personId) {
      predicates = predicates.equalTo("person_id", personId);
    }
    predicates = predicates.orderByDesc("pay_date");

    let promise = store.query(predicates, ['id', 'person_id', 'pay_date', 'work_days', 'daily_salary', 'should_pay', 'actual_pay', 'remark', 'create_time']);
    let records: SalaryRecord[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const personId = resultSet.getString(resultSet.getColumnIndex('person_id'));
          const payDate = resultSet.getString(resultSet.getColumnIndex('pay_date'));
          const workDays = resultSet.getLong(resultSet.getColumnIndex('work_days'));
          const dailySalary = resultSet.getDouble(resultSet.getColumnIndex('daily_salary'));
          const shouldPay = resultSet.getDouble(resultSet.getColumnIndex('should_pay'));
          const actualPay = resultSet.getDouble(resultSet.getColumnIndex('actual_pay'));
          const remark = resultSet.getString(resultSet.getColumnIndex('remark'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const record: SalaryRecord = {
            id,
            personId,
            payDate,
            workDays,
            dailySalary,
            shouldPay,
            actualPay,
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

  public async update(store: relationalStore.RdbStore, record: SalaryRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'person_id': record.personId,
      'pay_date': record.payDate,
      'work_days': record.workDays,
      'daily_salary': record.dailySalary,
      'should_pay': record.shouldPay,
      'actual_pay': record.actualPay,
      'remark': record.remark,
    };
    let predicates = new relationalStore.RdbPredicates("salary_record");
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
    let predicates = new relationalStore.RdbPredicates("salary_record");
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
