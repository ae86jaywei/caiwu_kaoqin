import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';
import { Project } from '../model/Project';

export class ProjectDao {
  public async insert(store: relationalStore.RdbStore, project: Project): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'id': project.id,
      'name': project.name,
      'start_date': project.startDate,
      'end_date': project.endDate,
      'address': project.address,
      'customer_name': project.customerName,
      'phone': project.phone,
      'contract_amount': project.contractAmount,
      'status': project.status,
      'remark': project.remark,
      'create_time': project.createTime
    };

    try {
      let promise = store.insert('project', valueBucket);
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

  public async queryAll(store: relationalStore.RdbStore): Promise<Project[]> {
    let predicates = new relationalStore.RdbPredicates("project");
    predicates = predicates.orderByDesc("create_time");

    let promise = store.query(predicates, ['id', 'name', 'start_date', 'end_date', 'address', 'customer_name', 'phone', 'contract_amount', 'status', 'remark', 'create_time']);
    let projects: Project[] = [];
    try {
      promise.then((resultSet: relationalStore.ResultSet) => {
        console.log(`resultSet columnNames: ${resultSet.columnNames}, columnCount: ${resultSet.columnCount}`);
        while (resultSet.goToNextRow()) {
          const id = resultSet.getString(resultSet.getColumnIndex('id'));
          const name = resultSet.getString(resultSet.getColumnIndex('name'));
          const startDate = resultSet.getString(resultSet.getColumnIndex('start_date'));
          const endDate = resultSet.getString(resultSet.getColumnIndex('end_date'));
          const address = resultSet.getString(resultSet.getColumnIndex('address'));
          const customerName = resultSet.getString(resultSet.getColumnIndex('customer_name'));
          const phone = resultSet.getString(resultSet.getColumnIndex('phone'));
          const contractAmount = resultSet.getDouble(resultSet.getColumnIndex('contract_amount'));
          const status = resultSet.getString(resultSet.getColumnIndex('status'));
          const remark = resultSet.getString(resultSet.getColumnIndex('remark'));
          const createTime = resultSet.getString(resultSet.getColumnIndex('create_time'));
          const project: Project = {
            id,
            name,
            startDate,
            endDate,
            address,
            customerName,
            phone,
            contractAmount,
            status,
            remark,
            createTime
          };
          projects.push(project);
        }
        resultSet.close();
      }).catch((err: BusinessError) => {
        console.error(`query failed, code is ${err.code},message is ${err.message}`);
      })
    } catch (e) {
      console.error(`query failed, code is ${(e as BusinessError).code},message is ${(e as BusinessError).message}`);
    }
    return projects;
  }

  public async update(store: relationalStore.RdbStore, project: Project): Promise<number> {
    const valueBucket: relationalStore.ValuesBucket = {
      'name': project.name,
      'start_date': project.startDate,
      'end_date': project.endDate,
      'address': project.address,
      'customer_name': project.customerName,
      'phone': project.phone,
      'contract_amount': project.contractAmount,
      'status': project.status,
      'remark': project.remark,
    };
    let predicates = new relationalStore.RdbPredicates("project");
    predicates.equalTo('id', project.id);
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
    let predicates = new relationalStore.RdbPredicates("project");
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
