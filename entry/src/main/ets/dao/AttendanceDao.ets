import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { AttendanceRecord } from '../model/AttendanceRecord';

export class AttendanceDao {
  public async insert(store: relationalStore.RdbStore, record: AttendanceRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': record.id,
      'person_id': record.personId,
      'project_id': record.projectId,
      'clock_date': record.clockDate,
      'morning_start': record.morningStart,
      'morning_end': record.morningEnd,
      'morning_hours': record.morningHours,
      'afternoon_start': record.afternoonStart,
      'afternoon_end': record.afternoonEnd,
      'afternoon_hours': record.afternoonHours,
      'overtime_start': record.overtimeStart,
      'overtime_end': record.overtimeEnd,
      'overtime_hours': record.overtimeHours,
      'custom_periods': record.customPeriods,
      'total_hours': record.totalHours,
      'create_time': record.createTime
    };

    try {
      let promise = store.insert('attendance_record', valueBucket);
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

  public async query(store: relationalStore.RdbStore, startDate: string, endDate: string, personId?: string, projectId?: string): Promise<AttendanceRecord[]> {
    let predicates = new relationalStore.RdbPredicates("attendance_record");
    if (startDate && endDate) {
      predicates = predicates.between("clock_date", startDate, endDate);
    }
    if (personId) {
      predicates = predicates.equalTo("person_id", personId);
    }
    if (projectId) {
      predicates = predicates.equalTo("project_id", projectId);
    }
    predicates = predicates.orderByDesc("clock_date");

    let promise = store.query(predicates, ['id', 'person_id', 'project_id', 'clock_date', 'morning_start', 'morning_end', 'morning_hours', 'afternoon_start', 'afternoon_end', 'afternoon_hours', 'overtime_start', 'overtime_end', 'overtime_hours', 'custom_periods', 'total_hours', 'create_time']);
    let records: AttendanceRecord[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const personId = resultSet.getString(resultSet.getColumnIndex('person_id'));
          const projectId = resultSet.getString(resultSet.getColumnIndex('project_id'));
          const clockDate = resultSet.getString(resultSet.getColumnIndex('clock_date'));
          const morningStart = resultSet.getString(resultSet.getColumnIndex('morning_start'));
          const morningEnd = resultSet.getString(resultSet.getColumnIndex('morning_end'));
          const morningHours = resultSet.getDouble(resultSet.getColumnIndex('morning_hours'));
          const afternoonStart = resultSet.getString(resultSet.getColumnIndex('afternoon_start'));
          const afternoonEnd = resultSet.getString(resultSet.getColumnIndex('afternoon_end'));
          const afternoonHours = resultSet.getDouble(resultSet.getColumnIndex('afternoon_hours'));
          const overtimeStart = resultSet.getString(resultSet.getColumnIndex('overtime_start'));
          const overtimeEnd = resultSet.getString(resultSet.getColumnIndex('overtime_end'));
          const overtimeHours = resultSet.getDouble(resultSet.getColumnIndex('overtime_hours'));
          const customPeriods = resultSet.getString(resultSet.getColumnIndex('custom_periods'));
          const totalHours = resultSet.getDouble(resultSet.getColumnIndex('total_hours'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const record: AttendanceRecord = {
            id,
            personId,
            projectId,
            clockDate,
            morningStart,
            morningEnd,
            morningHours,
            afternoonStart,
            afternoonEnd,
            afternoonHours,
            overtimeStart,
            overtimeEnd,
            overtimeHours,
            customPeriods,
            totalHours,
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

  public async update(store: relationalStore.RdbStore, record: AttendanceRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'person_id': record.personId,
      'project_id': record.projectId,
      'clock_date': record.clockDate,
      'morning_start': record.morningStart,
      'morning_end': record.morningEnd,
      'morning_hours': record.morningHours,
      'afternoon_start': record.afternoonStart,
      'afternoon_end': record.afternoonEnd,
      'afternoon_hours': record.afternoonHours,
      'overtime_start': record.overtimeStart,
      'overtime_end': record.overtimeEnd,
      'overtime_hours': record.overtimeHours,
      'custom_periods': record.customPeriods,
      'total_hours': record.totalHours,
    };
    let predicates = new relationalStore.RdbPredicates("attendance_record");
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
    let predicates = new relationalStore.RdbPredicates("attendance_record");
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
