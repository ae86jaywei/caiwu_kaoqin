@Observed
export class SalaryRecord {
  id: string = ''
  personId: string = ''
  payDate: string = ''
  workDays: number = 0
  dailySalary: number = 0
  shouldPay: number = 0
  actualPay: number = 0
  remark: string = ''
  createTime: string = ''
}
