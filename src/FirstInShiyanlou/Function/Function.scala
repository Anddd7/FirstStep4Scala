package FirstInShiyanlou.Function

import scala.io.Source

/**
  * Created by AnDong on 2016/12/22.
  * scala主旨是函数式语言 ,兼顾java形式的类/对象定义
  * 函数式编程 -> 函数作为第一位 ,函数可以作为参数/值/对象等进行传递和计算
  * 因此scala很多范式和语法糖都具有返回值 ,可以直接将函数作为参数传递(实质是传递函数的返回值)
  *
  * 对于无参函数 ,可不加括号(无参函数返回值固定 ,就像一个变量一样)
  * 函数调用可不使用 . (出于代码的可读性和习惯 ,感觉加上比较好)
  *
  */
object Function {

  def processFile(fileName: String, width: Int): Unit = {
    //scala允许定义嵌套函数 ,类似局部变量 ,放在使用函数前
    def processLine(line: String): Unit = {
      //嵌套函数可以直接使用父函数的参数和变量 ,
      if (line.length > width)
        println(fileName + ":" + line.trim)
    }

    val source = Source.fromFile(fileName)
    for (line <- source.getLines)
      processLine(line)
  }

  //函数字面量 -> 函数作为变量/变量绑定函数
  //(x: Int) => x + 1 : 函数字面量 ,在scala内部作为 functionN对象(N代表参数个数) 存在
  //    (x:Int) :声明有一个 整型参数x 的函数
  //    x + 1 :描述函数的操作 ,将参数x+1并返回
  //    => :连接
  //    var increase :绑定到一个变量(类似Javascript的 var a = function(x){return x + 1})
  var increase = (x: Int) => {
    println("input %d".format(x))
    x + 1
  }

  //scala的库也支持将函数作为参数传入
  var printList = (list: List[Int]) => {
    //foreach接受函数作为参数,对集合内每一个元素执行函数操作(将集合类元素作为参数传入目标函数)
    //第一种 ,对函数字面量进行简写 ,由上下文推断
    list.foreach(x => println(x))
    //第二种
    //    list.foreach((x: Int) => println(x))
    //第三种
    //    var printX = (x: Int) => println(x)
    //    list.foreach(x => printX(x))
  }

  //_ 的使用 : 作为一个参数替代符 ,由scala推断上下文进行填写 ,一般用在结构固定的函数
  var filterList = (list: List[Int]) => {
    // _ > 0 被替换成 x => x > 0
    list.filter(_ > 0)
      // println(_) 被替换成 x => println(x)
      //.foreach(println(_))
      //当表达式中的参数可以被一个 _ 替代 ,_也可以删掉(因为参数的传递是固定的1->1 ,没有其他可能和歧义)
      .foreach(println)

    // filter/foreach 方法会取出集合内每一个元素 ,作为参数传入到后面的函数
    // _ 则表示被传入的参数
  }

  //可变参数(最后一个参数) :
  //[args:String *]   加上*表示可变 ,实质Array ,但是Array不能直接作为参数
  //[args: _*]    改进 ,如果传入Array ,则依次传入每个元素 ,不是Array本身
  def echo(args: String*): Unit = {
    //for (arg <- args) println(arg)
    args.foreach(println)
  }

  //命名参数:
  // 传入参数时候指定参数名 ,可以不按照参数的顺序
  //processFile("test.txt", 1)
  processFile(width = 1, fileName = "test.txt")

  //缺省参数:
  //定义函数时默认参数值 ,不传入参数就按默认的进行
  def printTime(out: java.io.PrintStream = Console.out, divisor: Int = 1) =
  out.println("time = " + System.currentTimeMillis() / divisor)

  def main(args: Array[String]): Unit = {

    //object中定义的函数作为静态函数 ,还可以通过脚本方式调用
    //object中的变量也可以直接使用(变量绑定到函数 ,也可以调用到函数)
    Function.increase(20)

    val data = List(-11, 22, 12, 34 - 2, 9, -1)
    Function.printList(
      //使用filter过滤集合 ,输入一个lambda表达式 ,返回 true通过/false去除
      data.filter(x => x < 0)
    )
    Function.filterList(data)


    Function.printTime()
    Function.printTime(divisor=1000)
  }

}
