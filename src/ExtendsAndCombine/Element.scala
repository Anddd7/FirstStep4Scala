package ExtendsAndCombine

import scala.collection.mutable.ArrayBuffer

/**
  * Created by AnDong on 2016/12/26.
  *
  * scala类的属性通常也定义为函数(一等公民)
  * 通常是无参函数 只是读取对象的状态
  * 这也是为了减少var的使用 将函数作为值进行传递
  * 每一次获取类的属性 其实就是调用函数并返回新的属性(当前状态的属性)
  */
abstract class Element {
  //定义的无具体实现的函数 需要子类去实现
  def contents: Array[String]

  //val 和 无参函数 等价
  //但val会稍快一点 val只加载一次 函数调用会有多次
  //但val后续无法修改 无参函数可以重载

  def height: Int = contents.length

  def width: Int = if (height == 0) 0 else contents(0).length

  //val height = contents.length
  //val width = if (height == 0) 0 else contents(0).length

  def print : Unit = contents.foreach(println)
}

class ArrayElement(conts: Array[String]) extends Element {
  //对父类的抽象函数进行实现
  override def contents: Array[String] = conts

  //scala 成员函数和成员变量同级别 并且不可同名 同名时会自动覆盖(重载)
  //val contents: Array[String] = conts
}

//还可以放置在定义时
class ArrayElement2(val contents: Array[String]) extends Element {
}

//继承 参数可传递 可重载(同名必须添加override标识)
class LineElement(s: String) extends ArrayElement(Array(s)) {
  override def width = s.length
  override def height = 1
}

//重载放在定义时 并且增加一个变量
class UniformElement(ch: Char,
                     override val width: Int,
                     override val height: Int) extends Element {
  private val line = ch.toString * width

  def contents = Array.fill(height)(line) //用后面的数值填充Array
}


object ElementDev{
  def main(args: Array[String]): Unit = {
    val elements = new ArrayBuffer[Element]()
    elements += new ArrayElement(Array("i'm the first","Array Element"))
    elements += new ArrayElement2(Array("i'm the second","Array Element2"))
    elements += new LineElement("i just have one line")
    elements += new UniformElement('c',4,3)

    elements.foreach(e => e.print)
  }
}