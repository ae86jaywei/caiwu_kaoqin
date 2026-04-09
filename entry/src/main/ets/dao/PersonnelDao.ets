import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { Personnel } from '../model/Personnel';

export class PersonnelDao {
  public async insert(store: relationalStore.RdbStore, personnel: Personnel): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': personnel.id,
      'name': personnel.name,
      'person_type': personnel.personType,
      'reference_salary': personnel.referenceSalary,
      'phone': personnel.phone,
      'join_date': personnel.joinDate,
      'create_time': personnel.createTime
    };

    try {
      let promise = store.insert('personnel', valueBucket);
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

  public async queryAll(store: relationalStore.RdbStore): Promise<Personnel[]> {
    let predicates = new relationalStore.RdbPredicates("personnel");
    predicates = predicates.orderByDesc("create_time");

    let promise = store.query(predicates, ['id', 'name', 'person_type', 'reference_salary', 'phone', 'join_date', 'create_time']);
    let personnelList: Personnel[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const name = resultSet.getString(resultSet.getColumnIndex('name'));
          const personType = resultSet.getString(resultSet.getColumnIndex('person_type'));
          const referenceSalary = resultSet.getDouble(resultSet.getColumnIndex('reference_salary'));
          const phone = resultSet.getString(resultSet.getColumnIndex('phone'));
          const joinDate = resultSet.getString(resultSet.getColumnIndex('join_date'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const personnel: Personnel = {
            id,
            name,
            personType,
            referenceSalary,
            phone,
            joinDate,
            createTime
          };
          personnelList.push(personnel);
        }
        resultSet.close();
      }).catch((err: BusinessError) => {
        console.error(`query failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`query failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return personnelList;
  }

  public async update(store: relationalStore.RdbStore, personnel: Personnel): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'name': personnel.name,
      'person_type': personnel.personType,
      'reference_salary': personnel.referenceSalary,
      'phone': personnel.phone,
      'join_date': personnel.joinDate,
    };
    let predicates = new relationalStore.RdbPredicates("personnel");
    predicates.equalTo('id', personnel.id);
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
    let predicates = new relationalStore.RdbPredicates("personnel");
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
