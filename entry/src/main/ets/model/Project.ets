@Observed
export class Project {
  id: string = ''
  name: string = ''
  startDate: string = ''
  endDate: string = ''
  address: string = ''
  customerName: string = ''
  phone: string = ''
  contractAmount: number = 0
  status: string = '' // 进行中、已完成、已暂停
  remark: string = ''
  createTime: string = ''
}

export interface ProjectPersonStats {
  personId: string
  personName: string
  workDays: number
  totalHours: number
}

export interface ProjectExpenseStats {
  materialCost: number
  transportCost: number
  officeCost: number
  livingCost: number
  totalCost: number
}
