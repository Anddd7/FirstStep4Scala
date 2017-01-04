package SecondWithProgrammingInScala

/**
  * 函数式风格会将函数划分一个一个小块 ,完成最简单最基本的功能 ,然后根据需要组装起来
  * 小的组件易于理解/修改 ,这也与<代码清洁之道>的思想相同
  * 只不过Java语言的设计上就是面向对象的 ,scala直接将函数作为主体 ,拆分起来更方便和简洁
  *
  * 因为主体是函数 ,scala设置了多种语法去描述/简化函数的定义和使用
  * 本地函数 : 函数中定义并使用 ,作用于只在父函数内(嵌套,类似变量)
  * 函数字面量 : (x:Int) => x+1 ,这是一个函数定义 ,但也可以将他作为变量保存 ,再调用变量
  * 占位符 : _ ,用来指代函数字面量中的参数 (只能指代一个 ,保持没有歧义)
  * 重复参数 : args:String* (类似java的 String ...args )
  */
object Fuction {
  //封闭函数
  def printTable(x: Int, y: Int) = {
    def printRow(x: Int) = {
      print("*" * x)
    }
    for (i <- 1 to y) {
      printRow(x)
      println()
    }
  }

  printTable(5, 5)
  //printRow() //error 封闭函数无法调用

  val array = Array(1, 2, 4, 5, 6)
  //函数字面量
  var p = (x: Int) => print(x)
  array.foreach(p)
  println()
  array.foreach((x: Int) => print(x))
  println()
  array.foreach(x => print(x))
  println()
  //占位符
  array.foreach(print(_))
  println()

  //重复参数
  def printWord(ch: String*): Unit = {
    println(ch.mkString(" "))
  }

  printWord("I'm", "a", "good", "Person")

  def main(args: Array[String]): Unit = {
    Fuction
  }
}


/**
  * 闭包 : (x:Int) => x+more
  * more是一个自由变量 ， 其值及类型是在运行的时候得以确定的
  * x是类型确定的 ， 其值是在函数调用的时候被赋值的
  * x+more 是开放的(包含自由变量)open term
  * x+1 是封闭的closed term
  * 函数会在运行时捕获自由变量 ,完成从开放到封闭的过程 -> 闭包
  */
object Closure {
  def main(args: Array[String]): Unit = {
    //1闭包会在使用时去捕捉自由变量的值 ,形成封闭的函数再进行调用
    var more = 0
    val addMore = (x: Int) => x + more //x+1

    more = 1 //x+1
    println(addMore(10)) //11
    more = 99 //x+99
    println(addMore(10)) //109

    //2闭包内的变化也反馈到外部
    val someNumbers = List(-11, -10, -5, 0, 5, 10)
    var sum = 0
    someNumbers.foreach(sum += _)
    println(sum)

    //3如果需要多个版本的more共存 ,并分别调用
    //对每一个版本的more ,生成不同的addMore函数(快照)
    //调用闭包时 ,依赖的外部变量已经确定( = 定义闭包时的more变量)
    def makeIncreaser(more: Int) = (x: Int) => x + more

    val func1 = makeIncreaser(1) // more=1
    val func99 = makeIncreaser(99) // more=99

    println(func1(10)) //11
    println(func99(10)) //109
  }
}

/**
  * 尾递归
  * 递归方法比循环更容易理解 ,代码也更加清晰
  * 但是实际情况中 ,递归重复调用函数 ,导致方法栈溢出是一个严重的问题
  * 因此scala对递归进行优化 ,让你书写递归并转换为高效的循环
  * 但这个优化仅支持严格的尾递归
  */
object Chapter8App {

  //最后一个是+操作
  def boom(x: Int): Int = {
    if (x == 0) throw new Exception("boom!") else boom(x - 1) + 1
  }

  //最后一个是递归
  def bang(x: Int): Int = {
    if (x == 0) throw new Exception("boom!") else bang(x - 1)
  }

  def main(args: Array[String]): Unit = {
    //只调用一次bang函数 异常在里面
    bang(5)

    //调用多次boom函数
    boom(5)
  }

}
