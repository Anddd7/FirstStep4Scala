package SecondWithProgrammingInScala.Chapter3

/**
  * 使用类型参数化数组Array
  * 使用new实例化对象(class类) ,实例化时可以用值和类型使其参数化
  * 定义类型和访问元素的方式有所不同
  * arg[1] -> arg(1)
  * List<String> -> Array[String]
  */
object Part1 {
  //使用val表示该变量不能再赋值 ,但所指的对象内部却可以改变
  val greetStrings = new Array[String](3)
  greetStrings(0) = "Hello"
  greetStrings(1) = ","
  greetStrings(2) = "world!\n"

  //可以使用 x.to(y) 表示一个Range ,函数只有一个参数时 ,可以省略.和后者的括号(无歧义)
  for (i <- 0 to 2) println(greetStrings(i))

  /**
    * scala的操作符 + - * /
    * scala允许将字符作为函数名 ,因此+实际上是一个函数
    * 如 1+2 ,实质是调用定义在1(Int)类中的+函数 ,然后将2作为参数传递进去
    */
  val two = 1.+(1)
  val three = 1 + 2

  /**
    * scala使用括号访问数组元素
    * scala的数组也是类的实例 ,用括号传递参数实际是对apply方法的调用 ,所以访问数组元素也是调用的方法
    * 适用于所有类 ,对对象的值参数应用都转换为apply的调用(前提是定义过apply方法)
    */
  greetStrings.update(0, "See")
  greetStrings.update(1, "U")
  greetStrings.update(2, "Again!\n")
  for (i <- 0 to greetStrings.length - 1) println(greetStrings.apply(i))

}


object Chapter3App {
  def main(args: Array[String]): Unit = {
    Part1
  }
}
