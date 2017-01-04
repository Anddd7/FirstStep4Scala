package SecondWithProgrammingInScala.Package

/**
  * 包和引用
  * 之前的代码中已经展示过
  * 在scala中 ,可以在同一个文件中定义多个包 ,并且设置各自包内的类的访问权限
  * import也可以在任意地方使用 ,并且像局部变量 ,有着应用域
  */

//在文件中还可以定义package用来分隔文件
package navigation {

  import SecondWithProgrammingInScala.Package.navigation

  //private[x]  protect[x]
  // x可以是包/对象/类 表示允许访问的范围
  private[Package] class Navigator {

    protected[navigation] def useStarChart() {}

    //内部类
    class LegOfJourney {
      private[Navigator] val distance = 100
    }


    private[this] var speed = 200

    def demo: Unit = {
      val other = new Navigator
      // private[this] 只在创建该实例中可用 这里使用了new 外界无法读取
      //other.speed

      val leg = new LegOfJourney
      val dis = leg.distance
    }
  }

  class NavDemo {
    val navigator = new Navigator
    navigator.useStarChart()

    //使用内部类
    val leg = new navigator.LegOfJourney
    //distance是Navigator类下可访问
    //val dis = leg.distance
  }

}

package Launch {
  //引入包
  import FirstInShiyanlou.Package.Launch
  import navigation._

  object Vehicle {
    //Navigator是Package包下可访问
    private[Launch] val guide = new Navigator

    //useStarChart是Navigation包下可访问
    //guide.useStarChart //error

    //使用内部类
    val leg = new guide.LegOfJourney
    //distance是Navigation包下可访问
    //val dis = leg.distance  //error
  }

}