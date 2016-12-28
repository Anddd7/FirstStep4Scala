package FirstInShiyanlou.Function

import java.io.{File, PrintWriter}

/**
  * Created by AnDong on 2016/12/22.
  * scala特色的函数
  *
  * 函数式编程 ,通常使用递归 ,可以消除 var变量和循环
  *
  * (val不可变 符合函数式的思想 状态不可变 避免线程不安全等)
  * (java内的递归会引发stackoverflow 多次调用的函数参数/内部参数等信息放在堆栈中 )
  * (scala使用递归 代码结构清晰明了 并且会自动优化为循环 无损性能)
  *
  *
  * scala 提供各种对函数重构/组合/结构变化的方法
  * 围绕函数 最后写出干净 易懂的代码
  * 将复杂的工作交给编译器
  */
object Function2 {

  //尾递归 : 函数最后一行递归调用本身 scala会进行优化为循环
  //使用编译参数 -g:notailcalls 取消优化
  def bang(x: Int): Int = {
    if (x == 0) throw new Exception("boom!") else bang(x - 1)
  }

  //最后一个是+操作
  def boom(x: Int): Int = {
    if (x == 0) throw new Exception("boom!") else boom(x - 1) + 1
  }


  //函数简化聚合 : 可以在函数内定义一个函数结构 ,将函数作为参数传递进去 类似与同一接口不同实现
  //定义一个公共静态
  private def filesHere = (new java.io.File(".")).listFiles

  //定义一个函数作为参数
  def filesMatching(matcher: (String) => Boolean) = {
    for (file <- filesHere; //函数作为变量
         if matcher(file.getName)) //调用定义的函数->实际传入的函数
      yield file
  }

  //具体实现的函数
  // 调用filesEnding ->
  // 调用filesMatching 并将_.endsWith(query)函数作为参数传递 ->
  // 替换matcher为传入的_.endsWith(query) ->
  // 替换变量 _ 为 file.getName ->
  // 计算file.getName.endsWith(query)
  def filesEnding(query: String) =
  filesMatching(_.endsWith(query))

  def filesContaining(query: String) =
    filesMatching(_.contains(query))

  def filesRegex(query: String) =
    filesMatching(_.matches(query))


  //柯里化(Currying)函数 : 把接受多个参数的函数变换成接受一个单一参数（最初函数的第一个参数）的函数
  //类似于之前有关闭包的东西 将原函数拆分成2级 传入第一个参数生成函数快照 用快照接受剩下的函数进行运行
  def curriedSum(x: Int)(y: Int) = x + y

  def closureSum(x: Int) = (y: Int) => x + y

  val a = curriedSum(1)(2)
  val b = closureSum(1)(2)
  //因为参数列表分离 可以用Currying形成快照
  val onePlus = curriedSum(1)_//第二个参数用_标识 不可省略
  //onePlus是一个函数快照
  val twoPlus = curriedSum(2)_

  val result = onePlus(2) + twoPlus(3) //second调用函数快照


  //复杂控制结构(柯里化 应用) : 把函数作为变量 函数互相影响 改变结构
  //当一段逻辑需要重复多次 就可以进行重写 将变化的部分写成函数参数(有点像java的多态)
  //定义一个函数 将传入的函数递归一次
  def twice(op: Double => Double, x: Double) = op(op(x))

  //租赁模式 : 比如对文件操作结束必须要关闭 ,将这个操作屏蔽在内部 ,把正常功能租给外界使用(有点像AOP)
  def withPrintWriter(file: File)(op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer) //调用用户传入的方法 并将writer作为参数传入
    } finally {
      writer.close()
    }
  }

  //() {}式传参 : 对只有一个参数的函数 (){}都可以传递参数 通过柯里化应用到多个参数上
  val file = new File("test.txt")
  withPrintWriter(file)(
    writer => writer.println("") //获取方法注入的对象 创建函数 传入函数
  )
  withPrintWriter {
    file
  } {
    writer => writer.println("")
  }




  //传名参数 : 传递函数类型的参数
  def method1(predicate:  => Boolean) =
    print("传名参数")
  //传值参数 : 传递基本类型的参数
  def method2(predicate: Boolean) =
    print("传值参数")
  //使用传名参数 可以形成原生if else的效果
  def if2(assertion: =>Boolean)(method:  => Any) {
   if(assertion) method //如果判断条件成立 则执行条件体内的方法
   else println("不成立")
  }


  def main(args: Array[String]): Unit = {
    //只调用一次bang函数 异常在里面
    // bang(5)

    //调用多次boom函数
    //boom(5)

    if2(3>2){
      println("成立")
    }

  }

}
