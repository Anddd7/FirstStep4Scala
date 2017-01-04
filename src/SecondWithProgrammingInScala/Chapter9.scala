package SecondWithProgrammingInScala

import java.io.{File, PrintWriter}

/**
  * 闭包的高级应用
  *
  * 参考知乎对JavaScript的闭包研究
  * [到底什么是闭包 - 张恂老师](https://zhuanlan.zhihu.com/p/21346046)
  *
  * 函数A中声明函数B ,函数B作为参数传递给函数C并在C中执行 ,因为可以访问到A中的变量 ,称B的上下文是闭包
  *
  * 闭包是一个有状态的函数/一个有记忆的函数/一个只有一个方法的对象
  * 传统函数是没有状态的 ,变量必须放在函数参数中传递 ,每次调用是幂等的
  * 闭包则是把函数作为保存变量的环境 ,闭包内的变量被放在了不同的地方
  * (函数内的局部变量在栈上 函数退出即销毁 ;闭包内的数据被放在堆上 可以重复访问)
  *
  * 闭包的出现是因为词法作用域(lexical scope) ,在函数作为头等公民的语言(scala,Javascript)中 ,函数可以作为参数和返回值
  * 如果没有闭包 ,那么函数的free变量(不在使用处定义的变量)就没法正确的引用
  */
object ClosureApplication {
  private def filesHere = (new java.io.File(".")).listFiles

  //函数简化聚合 : 可以在函数内定义一个函数结构 ,将函数作为参数传递进去 类似与同一接口不同实
  private def filesMatching(matcher: (String) => Boolean) = {
    for (file <- filesHere; //函数作为变量
         if matcher(file.getName)) //调用定义的函数->实际传入的函数
      yield file
  }

  /**
    * 这里使用了闭包的概念 - filesEnding函数A - _.endsWith(query)函数B - filesMatching函数C
    * 调用filesEnding(".scala")时 ,".scala"作为一个自由变量(函数A的参数) ,被函数字面量_.endsWith(query)捕获
    * 产生了新的函数_.endsWith(".scala") (引用函数A的参数的函数B),并将这个函数传入filesMatching(函数C)
    * 此时传递的是已经闭合的函数快照(函数C中使用函数B ,函数B已经捕获函数A的参数)
    */

  def filesEnding(query: String) =
  filesMatching(_.endsWith(query))

  def filesContaining(query: String) =
    filesMatching(_.contains(query))

  def filesRegex(query: String) =
    filesMatching(_.matches(query))

  filesEnding("txt")

  //把函数作为变量可以创建复杂的控制结构(对函数进行操作)
  //当一段逻辑需要重复多次 就可以进行重写 将变化的部分写成函数参数(有点像java的多态)
  //定义一个函数 将传入的函数递归一次
  def twice(op: Double => Double, x: Double) = op(op(x))

  val three = (1 + 1) + 1
  val four = twice(_ + 1, 2)
  println(three)
  print(four)

  def main(args: Array[String]): Unit = {
    ClosureApplication
  }
}

/**
  * 柯里化
  * 把接受多个参数的函数变换成接受一个单一参数（最初函数的第一个参数）的函数
  * 类似于之前的闭包 将原函数拆分成2级 传入第一个参数生成函数快照 用快照接受剩下的函数进行运行
  */
object Currying {
  def curriedSum(x: Int)(y: Int) = x + y

  def closureSum(x: Int) = (y: Int) => x + y

  val a = curriedSum(1)(2)
  val b = closureSum(1)(2)
  //因为参数列表分离 可以用Currying形成快照
  //但原函数有2个参数列表 ,第二个参数用_标识 ,不可省略(与闭包快照不同)
  val onePlus = curriedSum(1) _
  val twoPlus = curriedSum(2) _
  //调用函数快照
  val result = onePlus(2) + twoPlus(3)

  println(a)
  println(b)
  println(onePlus)
  println(twoPlus)
  println(result)

  //租赁模式(柯里化+函数参数) : 比如对文件操作结束必须要关闭 ,将这个操作屏蔽在内部 ,把正常功能租给外界使用(有点像AOP)
  def withPrintWriter(file: File)(op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer) //调用用户传入的方法 并将writer作为参数传入
    } finally {
      writer.close()
    }
  }

  //(){}式传参 : 对只有一个参数的函数 (){}都可以传递参数 通过柯里化应用到多个参数上
  val file = new File("test.txt")
  //对于柯里化函数的每一个参数 ,都能生成 只有一个参数的函数(半成品) ,因此都可以使用{}
  withPrintWriter(file) {
    writer => writer.println("file")
  }
  withPrintWriter {
    file
  } { writer => writer.println("file") }

  def main(args: Array[String]): Unit = {
    Currying
  }
}

/**
  * 传名参数
  * 传值参数
  */
object ByNameParameter {

  //传名参数 : 传递函数类型的参数 并且省略参数列表
  def method1(predicate: => Boolean) =
  print("传名参数")

  //传值参数 : 传递基本类型的参数
  def method2(predicate: Boolean) =
  print("传值参数")

  //使用传名参数+柯里化 可以形成原生if else的效果
  def if2(assertion: => Boolean)(method: => Any) {
    if (assertion) method //如果判断条件成立 则执行条件体内的方法
    else println("不成立")
  }

  def main(args: Array[String]): Unit = {
    if2(3 > 2) {
      println("成立")
    }
    if2(2 > 3) {
      println("成立")
    }
  }
}

