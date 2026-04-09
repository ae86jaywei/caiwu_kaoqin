@Entry
@ComponentV2
struct Index {
  @State message: string = 'Hello World';
  @State current: number = 0;

  build() {
    Tabs({ barPosition: BarPosition.End }) {
      TabContent() {
        Text('财务')
          .fontColor($r('app.color.start_window_background'))
      }
      .tabBar(new SubTabBarStyle('财务'))

      TabContent() {
        Text('考勤')
          .fontColor($r('app.color.start_window_background'))
      }
      .tabBar(new SubTabBarStyle('考勤'))

      TabContent() {
        Text('工资')
          .fontColor($r('app.color.start_window_background'))
      }
      .tabBar(new SubTabBarStyle('工资'))

      TabContent() {
        Text('工作记录')
          .fontColor($r('app.color.start_window_background'))
      }
      .tabBar(new SubTabBarStyle('工作记录'))

      TabContent() {
        Text('人员')
          .fontColor($r('app.color.start_window_background'))
      }
      .tabBar(new SubTabBarStyle('人员'))
    }
    .width('100%')
    .onChange((index: number) => {
      console.log(`index: ${index}`);
    })
  }
}
