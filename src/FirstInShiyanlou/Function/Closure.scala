package FirstInShiyanlou.Function

/**
  * Created by AnDong on 2016/12/22.
  * 闭包
  */
object Closure {
  def main(args: Array[String]): Unit = {
    //more是一个自由变量 ， 其值及类型是在运行的时候得以确定的
    //x是类型确定的 ， 其值是在函数调用的时候被赋值的
    //这样的函数称之为闭包 ： 从开放到封闭的过程
    // x+more 是开放的(包含自由变量)open term
    // x+1 是封闭的closed term

    //1闭包会在使用时去捕捉自由变量的值 ,形成封闭的函数再进行调用
    var more = 1
    val addMore = (x: Int) => x + more //x+1
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
