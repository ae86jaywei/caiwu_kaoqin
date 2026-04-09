@Observed
export class FinanceRecord {
  id: string = ''
  recordType: string = '' // 工程收支、交通费用、办公用品、生活食材、工资发放
  amount: number = 0
  date: string = ''
  projectId: string = ''
  projectName: string = ''
  remark: string = ''
  createTime: string = ''
}
