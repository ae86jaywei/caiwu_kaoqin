import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { WorkRecord } from '../model/WorkRecord';

export class WorkDao {
  public async insert(store: relationalStore.RdbStore, record: WorkRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': record.id,
      'person_id': record.personId,
      'work_date': record.workDate,
      'work_content': record.workContent,
      'project_id': record.projectId,
      'create_time': record.createTime
    };

    try {
      let promise = store.insert('work_record', valueBucket);
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

  public async query(store: relationalStore.RdbStore, personId?: string): Promise<WorkRecord[]> {
    let predicates = new relationalStore.RdbPredicates("work_record");
    if (personId) {
      predicates = predicates.equalTo("person_id", personId);
    }
    predicates = predicates.orderByDesc("work_date");

    let promise = store.query(predicates, ['id', 'person_id', 'work_date', 'work_content', 'project_id', 'create_time']);
    let records: WorkRecord[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const personId = resultSet.getString(resultSet.getColumnIndex('person_id'));
          const workDate = resultSet.getString(resultSet.getColumnIndex('work_date'));
          const workContent = resultSet.getString(resultSet.getColumnIndex('work_content'));
          const projectId = resultSet.getString(resultSet.getColumnIndex('project_id'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const record: WorkRecord = {
            id,
            personId,
            workDate,
            workContent,
            projectId,
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

  public async update(store: relationalStore.RdbStore, record: WorkRecord): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'person_id': record.personId,
      'work_date': record.workDate,
      'work_content': record.workContent,
      'project_id': record.projectId,
    };
    let predicates = new relationalStore.RdbPredicates("work_record");
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
    let predicates = new relationalStore.RdbPredicates("work_record");
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
