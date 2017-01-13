package SecondWithProgrammingInScala

/**
  * 抽象成员
  * 不完全定义的类或者特质 ,包含val,var,def方法,type类型
  */
trait Abstract {
  //类型成员 : 简化类型参数 ,方便定义
  type T

  def transform(x: T): T

  //表示确定的不会变化的值
  val initial: T
  //相当于生成了get/set方法
  var current: T
}

//实现时候必须要对所有抽象的成员进行实现
class Concrete extends Abstract {
  type T = String

  override def transform(x: String): String = initial + x

  override val initial: String = "hi"
  override var current: String = initial
}

//val的初始化 ,如果成员变量是一个表达式 x+3 ,
//在IntTest初始化的时候不会计算表达式 ,initial会被默认为0
trait IntTest {
  val initial: Int
  val time: Int
  require(initial != 0)

  def result = time * initial
}

//val的懒加载
object LazyDemo {
  println("initial object")
  val x = println("initial x")
  lazy val y = println("initial y")

}

object AbstractApp {
  def main(args: Array[String]): Unit = {
    LazyDemo
    println
    LazyDemo.y

    val num = 2
    //预初始化
    val test1 = new {
      val initial = num + 3
    } with IntTest {
      val time = 2

      def print = println(result)
    }
    test1.print


    val testError = new IntTest {
      val initial = num + 3
      val time = 2
    }
  }
}

//合理利用抽象类型
trait Food {
  def name: String
}

abstract class Animal {
  def name: String

  type SuitableFood <: Food

  def eat(food: SuitableFood) = println(name + "吃" + food.name)
}

class Grass extends Food {
  override def name: String = "草"
}

class Cow extends Animal {
  override type SuitableFood = Grass

  override def name: String = "牛"
}

object AbstractTypeApp {
  def main(args: Array[String]): Unit = {
    //将抽象类型放在子类中指定 ,更细致的控制
    val anima = new Cow
    val food = new Grass
    anima.eat(food)
  }
}

//枚举
object Color extends Enumeration {
  val R = "red"
  val G = "green"
  val B = "blue"
}