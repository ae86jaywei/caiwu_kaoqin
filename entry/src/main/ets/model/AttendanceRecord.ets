export interface CustomPeriod {
  start: string
  end: string
  hours: number
}

@Observed
export class AttendanceRecord {
  id: string = ''
  personId: string = ''
  projectId: string = ''
  clockDate: string = ''
  morningStart: string = ''
  morningEnd: string = ''
  morningHours: number = 0
  afternoonStart: string = ''
  afternoonEnd: string = ''
  afternoonHours: number = 0
  overtimeStart: string = ''
  overtimeEnd: string = ''
  overtimeHours: number = 0
  customPeriods: string = '' // JSON字符串
  totalHours: number = 0
  createTime: string = ''
}
