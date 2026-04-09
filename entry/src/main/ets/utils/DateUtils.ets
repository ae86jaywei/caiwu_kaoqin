export class DateUtils {
  /**
   * 格式化日期
   * @param date
   * @returns yyyy-MM-dd
   */
  public static formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  /**
   * 格式化日期时间
   * @param date
   * @returns yyyy-MM-dd HH:mm:ss
   */
  public static formatDateTime(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hour = date.getHours().toString().padStart(2, '0');
    const minute = date.getMinutes().toString().padStart(2, '0');
    const second = date.getSeconds().toString().padStart(2, '0');
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
  }

  /**
   * 计算两个时间点之间的工作时长（小时）
   * @param startTime
   * @param endTime
   * @returns 小时数
   */
  public static calculateHours(startTime: string, endTime: string): number {
    const start = new Date(startTime);
    const end = new Date(endTime);
    const diff = end.getTime() - start.getTime();
    const hours = diff / (1000 * 60 * 60);
    return Math.round(hours * 100) / 100;
  }

  /**
   * 获取当前月第一天
   * @returns yyyy-MM-dd
   */
  public static getCurrentMonthFirstDay(): string {
    const today = new Date();
    const y = today.getFullYear();
    const m = today.getMonth();
    const firstDay = new Date(y, m, 1);
    return this.formatDate(firstDay);
  }

  /**
   * 获取当前月最后一天
   * @returns yyyy-MM-dd
   */
  public static getCurrentMonthLastDay(): string {
    const today = new Date();
    const y = today.getFullYear();
    const m = today.getMonth();
    const lastDay = new Date(y, m + 1, 0);
    return this.formatDate(lastDay);
  }
}
