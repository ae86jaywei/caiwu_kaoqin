import { relationalStore } from '@kit.ArkData';
import { BusinessError } from '@ohos.base';

const STORE_CONFIG: relationalStore.StoreConfig = {
  name: 'FinanceAttendance.db',
  securityLevel: relationalStore.SecurityLevel.S1
};

const SQL_CREATE_FINANCE_RECORD = `CREATE TABLE IF NOT EXISTS finance_record (
  id TEXT PRIMARY KEY,
  record_type TEXT NOT NULL,
  amount REAL NOT NULL,
  date TEXT NOT NULL,
  project_id TEXT,
  project_name TEXT,
  remark TEXT,
  create_time TEXT NOT NULL
)`;

const SQL_CREATE_ATTENDANCE_RECORD = `CREATE TABLE IF NOT EXISTS attendance_record (
  id TEXT PRIMARY KEY,
  person_id TEXT NOT NULL,
  project_id TEXT,
  clock_date TEXT NOT NULL,
  morning_start TEXT,
  morning_end TEXT,
  morning_hours REAL,
  afternoon_start TEXT,
  afternoon_end TEXT,
  afternoon_hours REAL,
  overtime_start TEXT,
  overtime_end TEXT,
  overtime_hours REAL,
  custom_periods TEXT,
  total_hours REAL,
  create_time TEXT NOT NULL
)`;

const SQL_CREATE_SALARY_RECORD = `CREATE TABLE IF NOT EXISTS salary_record (
  id TEXT PRIMARY KEY,
  person_id TEXT NOT NULL,
  pay_date TEXT NOT NULL,
  work_days INTEGER NOT NULL,
  daily_salary REAL NOT NULL,
  should_pay REAL NOT NULL,
  actual_pay REAL,
  remark TEXT,
  create_time TEXT NOT NULL
)`;

const SQL_CREATE_PERSONNEL = `CREATE TABLE IF NOT EXISTS personnel (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  person_type TEXT NOT NULL,
  reference_salary REAL,
  phone TEXT,
  join_date TEXT,
  create_time TEXT NOT NULL
)`;

const SQL_CREATE_WORK_RECORD = `CREATE TABLE IF NOT EXISTS work_record (
  id TEXT PRIMARY KEY,
  person_id TEXT NOT NULL,
  work_date TEXT NOT NULL,
  work_content TEXT NOT NULL,
  project_id TEXT,
  create_time TEXT NOT NULL
)`;

const SQL_CREATE_PROJECT = `CREATE TABLE IF NOT EXISTS project (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  start_date TEXT NOT NULL,
  end_date TEXT,
  address TEXT,
  customer_name TEXT,
  phone TEXT,
  contract_amount REAL,
  status TEXT NOT NULL,
  remark TEXT,
  create_time TEXT NOT NULL
)`;

class RdbOpenCallback implements relationalStore.RdbOpenCallback {
  onCreate(store: relationalStore.RdbStore): void | Promise<void> {
    console.info(`RdbOpenCallback onCreate`);
    store.executeSql(SQL_CREATE_FINANCE_RECORD);
    store.executeSql(SQL_CREATE_ATTENDANCE_RECORD);
    store.executeSql(SQL_CREATE_SALARY_RECORD);
    store.executeSql(SQL_CREATE_PERSONNEL);
    store.executeSql(SQL_CREATE_WORK_RECORD);
    store.executeSql(SQL_CREATE_PROJECT);
  }

  onUpgrade(store: relationalStore.RdbStore, oldVersion: number, newVersion: number): void | Promise<void> {
    console.info(`RdbOpenCallback onUpgrade`);
  }
}

export class DatabaseHelper {
  private static instance: DatabaseHelper;

  private constructor() {
  }

  public static getInstance(): DatabaseHelper {
    if (!DatabaseHelper.instance) {
      DatabaseHelper.instance = new DatabaseHelper();
    }
    return DatabaseHelper.instance;
  }

  public async initRdbStore(context: Context): Promise<relationalStore.RdbStore> {
    const predicates = new relationalStore.RdbPredicates("EMPLOYEE");
    predicates.equalTo("NAME", "Lisa");
    predicates = predicates.and().equalTo("AGE", 18);
    predicates = predicates.or().equalTo("SALARY", 100.5);
    predicates = predicates.orderByDesc("NAME");
    predicates = predicates.limit("3");
    predicates = predicates.offset("3");
    let rdbStore: relationalStore.RdbStore = await relationalStore.getRdbStore(context, STORE_CONFIG, new RdbOpenCallback());
    return rdbStore;
  }
}
