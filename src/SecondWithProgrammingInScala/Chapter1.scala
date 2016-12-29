package SecondWithProgrammingInScala

/**
  * Scala名称来自于scalable(可伸缩的) language ,它的设计目的就是为了方便使用者的扩展(如自己构造的if(){}的语法糖函数)
  * Scala运行在JVM上 ,可以与Java无缝连接(编译后都是class码) ,也可以作为脚本使用(还有命令行解释器)
  * Scala = JS (弱类型) + Java(面向对象) + C(函数式编程)
  * 每一门语言有着他的特性和优缺点 ,有着局限性和适用性
  * Scala给你多种特性去自由选择 ,需要对象就class ,需要函数就用函数特性
  *
  */
/**
  * Scala属于高级语言 ,每一句代码都具有很强的目的性 ,不使用分号和类型注释 ,显得干净准确
  * "关联映射"?
  */
object Part1 {
  var capital = Map("US" -> "Washington", "France" -> "Paris")
  capital += ("Japan" -> "Tokyo")
  println(capital)

  /**
    * Scala可以让使用者进行扩展和修改 ,可以从语言层面上进行修改
    * 例如BigInt不属于原生类型(Int/Char/Boolean等) 但是也可以像内建类型一样使用+/-等算数方法和整数进行计算
    * 如果换成Java里面的类型就不那么自由了
    */
  def factorial(x: BigInt): BigInt =
  if (x == 0) 1 else x * factorial(x - 1)

  /**
    * 使用Java里面定义的大整型 ,相比之下因为函数名和语法的限制 ,运算显得很臃肿
    */

  import java.math.BigInteger

  def factorial(x: BigInteger): BigInteger =
    if (x == BigInteger.ZERO) BigInteger.ONE
    else x.multiply(factorial(x.subtract(BigInteger.ONE)))

  /**
    * 可变的控制结构 ,书中的scala actor已经废弃 ,因此用另一个例子说明
    * 可以看到 ,我自己书写了一个if2 ,他使用起来和内置的if几乎一样(只作为控制结构变换展示)
    */
  def if2(assertion: => Boolean)(method: => Any) {
    if (assertion) method //如果判断条件成立 则执行条件体内的方法
    else println("不成立")
  }

  if2(1 == 2) {
    println("成立")
  }
}

/**
  * scala是面向对象的
  * 它包含class/object ,也有着trait/abstract的设计 ,也有private/protect的访问限制
  * 调用方法也是可以通过 class.xxx 调用类和对象的方法 ,只是语法上做了简化
  */
object Part2 {
  //弱类型 自动判断
  val one = 1
  //强类型 指定
  val two: Int = 2
  //直接使用 + 函数
  val three = one + two
  //实际是调用的 Int.+ 函数
  val four = three.+(1)

  /**
    * scala是函数式的
    * 函数是头等值 ,与整数字符串同等地位 ,函数可以作为参数传递 ,可以在函数中做在其他地方能做的任何事
    * 程序的输入值应该映射成输出值 ,而不是加以修改(返回new) ,鼓励使用不可变数据结构 ,方法透明(用方法的返回替代输出值)
    *
    * 从很多设计可以看出 ,作者是想要大家使用函数式的代码方式 ,减少var的使用 ,ifelse携带返回
    */
}

/**
  * 兼容 :Java交互 .底层JVM .
  * 简洁 :能省则省 ,还可以改变函数控制结构去省(交给编译器解释 ,编译的时间会更长)
  * 高层抽象 :使用更具体的函数foreach(e=>{}) 或是 _ 函数字面量 ,可以省略很多代码
  * 高级的静态类型化 :静态类型可以避免布尔型不会与整数型相加;增加类型推断/隐式转换减少定义强类型的情况
  */
object Part3 {

  /*这是一个Java类
  class Message {
    private String msg;

    public Message (String msg) {
      this.msg = msg;
    }

    public boolean hasUpperCase() {
      boolean result = false;
      for (int i = 0;i < msg.length;i ++){
        if (Character.isUpperCase(name.charAt(i))) {
          result = true;
          break
        }
      }
      return result
    }
  }
  */
  //上面的java代码使用scala
  class Message(msg: String) {
    def hasUpperCase: Boolean = {
      msg.exists(_.isUpper)
    }
  }

  //弱类型 隐式转换
  val i = 'i'
  val i1 = i + 1

  println(i1)
  //隐藏的类型规则 会及时提醒错误
  //val i2: Char = i + 2
  //val T = true + 1

  //除去重复的定义
  import scala.collection.immutable.HashMap

  //val x: HashMap[Int, String] = new HashMap[Int, String]
  val y: HashMap[Int, String] = new HashMap
}

/**
  * scala的特点就是集众家之长
  * 语法上主要借助Java和C#(他们的句法来自于C/C++) ,
  * 和Java一样的 表达式/句子/代码块 ,类/包/引用 ,基本类型/类库 ,和执行模式(.class->JVM)
  *
  * 统一对象模型来自Smalltalk(也影响了Ruby)
  * 通用嵌套思想 ,类/函数/变量都能互相嵌套 ,出现在Algol/Simula
  * 方法调用和字段选择的统一访问原则来自Eiffel
  * 函数式编程的处理方式则以SML/OCaml/F#为代表的ML系列语言相近 ,一些高阶函数也出现在ML和Haskell中
  * 隐式参数来自Haskell的类型类
  * 基于actor的并发库(akka.actor)来自于Erlang的思想
  *
  * Iswim/Smalltalk -> 把前缀操作符视为函数 1.+(1) ;允许函数字面量(或代码块)作为参数 ,从而定义控制结构
  * C++ -> 重载操作符 ;模板
  * 多种函数式语言中也引入了对象
  */
object Part4 {
}

/**
  * 多多练习
  * Java->Scala 需要更多了了解他的类型系统和函数式编程思想
  */
object Part5 {
}

object Chapter1App {
  def main(args: Array[String]): Unit = {
    //Part1
    Part3
  }
}

