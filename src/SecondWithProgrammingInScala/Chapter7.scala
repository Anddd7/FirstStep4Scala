//可以自定义包结构 ,还可以在同一文件中定义多个包
package SecondWithProgrammingInScala.Chapter7 {


  class ControlStructure(x: Int, y: Int) {
    var temp = x
    //if-else结构具有返回值 ,会将最后一行的变量返回
    println(if (x > y) x else y)

    //赋值语句不会再返回右边的值 ,不能使用 (x=2) < 3 这样的判断
    //println((temp = 2) < y) //error


    //for表达式进阶
    val args = Array("Hello", ",", "Scala", "!")

    //过滤(循环条件内使用if)
    for (arg <- args
         if arg != (",")
         if arg != ("!")
    ) print(arg)
    println()

    //嵌套
    for (arg <- args;
         c <- arg.toCharArray
    ) print(c)
    println()

    //制造新集合
    val newArgs =
    for (arg <- args
         if arg != (",")
         if arg != ("!")
    ) yield arg
    println(newArgs.toString)


    //try处理异常 throw抛出 catch捕捉 finally
    def tryCatch: String = {
      try {
        val a = x / y
        a.toString
      } catch {
        case ex: Exception => throw ex
      } finally {
        println("tryCatch方法结束")
      }
    }


    //match表达式 类似switch
    args.foreach(arg => {
      arg match {
        case "Hello" => print("欢迎")
        case _ => print(arg)
      }
    })
    println()


    //不使用break和continue ,用if-var(if true if false)去跳过和弹出
    //或者使用递归 (有一定风险) 需要优化
  }

  /**
    * 使用函数式风格打印一个乘法表
    * 和Java的区别主要在于语法和val的使用
    * 语法上 : 类的定义/隐藏的构造函数/if-else替代三元表达式/函数自带返回值
    * 风格上 : 减少了var的使用 ,将变量隐藏在函数参数中 ,所有值在进入函数体后都是不可改变的
    * 需要的每一个值都通过其他val值得出 ,层次比较清晰
    * 而var(Java中重复赋值) ,到最后可能都忘记之前是什么值做什么用
    */
  class MultiTable(row: Int, col: Int) {
    private val maxWidthOfNum = (row * col).toString.length

    private def makeRow(r: Int) = {
      for (c <- 1 to col) yield {
        val prod = (r * c).toString
        val width = maxWidthOfNum - prod.toString.length
        val padding = if (c == 1) " " * width else " " * (width + 2)
        padding + prod
      }
    }

    def printTable = {
      for (r <- 1 to row) println(makeRow(r).mkString + "\n")
    }
  }

  object Chapter7App {
    def main(args: Array[String]): Unit = {
      val multiTable = new MultiTable(12, 9)
      multiTable.printTable

      try {
        val test1 = new ControlStructure(6, 2)
        println(test1.tryCatch)

        val test2 = new ControlStructure(2, 0)
        println(test2.tryCatch)
      } catch {
        case ex: Exception => println(ex.getMessage)
      }
    }
  }

}