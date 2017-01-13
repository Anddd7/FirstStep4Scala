package SecondWithProgrammingInScala

import java.util.{Comparator, RandomAccess}

/**
  * 隐式转换和参数
  */

//对一些已有的无法改动的类(如库函数/第三方库) ,扩展自己定义的方法
//通过隐式转换 ,将类A->类B ,类B是具有需要的方法的类
class A {
  val name = "ClassA"
}

class B(val name: String) {
  def print = println("hello " + name)
}

object Transform {
  def main(args: Array[String]): Unit = {
    //定义隐式转换将A->B
    /**
      * 隐式转换和使用对象需要在一个作用域
      * 无歧义 ,同时只有一种隐式转换可以调用
      * 每次只调用一个隐式转换 ,不能嵌套
      * 优先执行显示调用 ,只有无显示方法才会隐式转换
      *
      * 类似Map(1->"A")中的->也是通过隐式转化 ,转化成Predef.ArrowAssoc ,并调用->方法
      */
    implicit def wapper(a: A) = new B(a.name)

    //使用a调用他并没有的方法
    val a = new A
    a.print

    //隐式转换还可以通过参数列表传入
    def max1[T](a: T, b: T)(implicit cp: Comparator[T]) = {
      if (cp.compare(a, b) >= 0) a else b
    }

    //声明参数并引入
    val cp = new Comparator[Int] {
      override def compare(a: Int, b: Int) = a - b
    }
    println(max1(5, 6)(cp))

    //直接声明作用域变量
    implicit val cp2 = new Comparator[Int] {
      override def compare(a: Int, b: Int) = a - b
    }
    println(max1(5, 6))
  }
}
