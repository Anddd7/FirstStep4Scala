package SecondWithProgrammingInScala

/**
  * 使用scala解释器 类似于脚本编程
  * 定义的变量和函数会暂存在解释器中(就像解释器是一个大的object 写的代码会实时编译到里面)
  * 使用函数会将返回值显示在下一行
  * $ scala
  */
object Part1 {
}

/**
  * 变量定义
  * var : variable 变量 初始化后可以改变
  * val : value 固定值 赋值后不可变
  */
object Part2 {
  val a = 1
  var b = 1
  b = 2
  //a = 2 //error
  //a = b //error
}

/**
  * 函数定义
  */
object Part3 {
  //完整定义版本
  def max(x: Int, y: Int): Int = {
    if (x > y) x
    else y
  }

  //简化版本 :简化返回类型 ,自动推断(递归时必须指定类型) ;函数体只包含ifelse一句 ,直接使用等于
  def max2(x: Int, y: Int) = if (x > y) x else y

  //调用
  max(3, 5)

  //没有参数和返回值的
  def greet() = {
    println("Hello,World!")
  }

  //简化
  def greet2 = println("Hello,World!")

  /**
    * 从上面可以看出 ,函数在没有参数的情况下和变量定义极为相似
    * 有些时候可以把这样的函数直接当变量用 把函数作为第一要素
    */
  //定义一个函数 返回1
  def get1 = 1

  //变量通过调用函数赋值
  val one = get1
  //函数当做变量参与运算
  val two = one + get1
}

/**
  * 编写scala脚本
  * 在scala文件中直接书写代码 ,不添加class/object/main函数
  * 直接使用$ scala xxx.scala 就可以运行
  *
  * 运行时会自动在外层包裹一个main函数 ,并传入args参数
  * $ scala xxx.scala str1 str2
  */
object Part4 {
  def main(args: Array[String]): Unit = {
    println("This is ScalaScript")
    args.foreach(println)
  }
}

/**
  * while和if的使用和Java没有太大区别
  * scala没有i++ ,只能i+=1
  * 指令式imperative(代码去描述枚举 ,逐条执行)
  */

object Part5 {
  val array = Array("Scala", "is", "fun")
  var i = 0
  while (i < array.length) {
    if (i != 0) print("  ")
    print(array(i))
    i += 1
  }
  println()
}

/**
  * for和foreach基本用法和Java差不多
  * 但Scala在语法中增加了filter/exist等语法糖和函数 ,更加方便一些
  * 函数式functional
  */
object Part6 {
  val array = Array(1, -2, 3, -4, 5, -6)

  //foreach自动枚举数组 ,将函数作为参数传入 ,让foreach自动调用你传入的函数
  def printInt(i: Int) = println(i)

  array.foreach((i: Int) => printInt(i))
  //通常省略函数定义和类型(自动推断) 直接放入 字面量i 函数体println(i)
  array.foreach(i => println(i))
  //如果函数字面量只有一个参数 ,函数体也只有一句(唯一性 ,没有歧义)
  array.foreach(println)

  /**
    * 函数字面量
    * (x:Int,y:Int)   =>      x+y
    * 函数参数列表    右键头   函数体
    */
  //scala依旧保留了Java中指令式的for循环
  //其中的ele是val ,每次枚举都是新的值 ,而非var
  for (ele <- array) println(ele)

}

object Chapter2App {
  def main(args: Array[String]): Unit = {
    //Part3
    Part5
  }
}