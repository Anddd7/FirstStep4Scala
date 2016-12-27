package ExtendsAndCombine

import scala.collection.mutable.ArrayBuffer
/**
  * 隐含
  * import java.lang._   //everything in the java.lang package
  * import scala._       //everything in the scala package
  * import Predef._      //everything in the Predef object
  */


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

  def show: Unit = contents.foreach(println)

  //父类定义接受多态参数的函数 并组合子类进行返回
  def above(that: Element): Element = new ArrayElement(this.contents ++ that.contents)

  def beside(that: Element): Element = new ArrayElement(
    for ((line1, line2) <- this.contents zip that.contents)
    //yield 是将后面的变量保存到集合中 ,在for循环结束时作为返回值返回
      yield line1 + line2
  )

  //final定义不可以重载/派生子类
  final def author: String = "Andong"
}

class ArrayElement(conts: Array[String]) extends Element {
  //对父类的抽象函数进行实现
  override def contents: Array[String] = conts

  //scala 成员函数和成员变量同级别 并且不可同名 同名时会自动覆盖(重载)
  //val contents: Array[String] = conts

}

//还可以放置在定义时
//class ArrayElement2(val contents: Array[String]) extends Element {
//}

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

  //重载打印函数
  override def show: Unit = {
    for (i <- 0 to height) {
      for (j <- 0 to width) {
        if (i == height / 2 && j == width / 2) print("i'm UniformElement")
        else print(ch)
      }
      println()
    }
  }
}

//定义工厂
object ElementFactory {
  def elem(contents: Array[String]): Element =
    new ArrayElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element =
    new UniformElement(chr, width, height)

  def elem(line: String): Element =
    new LineElement(line)
}

object ElementDev {
  def main(args: Array[String]): Unit = {

    val elements = new ArrayBuffer[Element]()
    //多态
    elements += ElementFactory.elem(Array("i'm the first", "Array Element"))
    elements += ElementFactory.elem("i just have one line")
    elements += ElementFactory.elem('c', 4, 3)
    elements += ElementFactory.elem('*', 4, 3) above ElementFactory.elem("test above")
    elements += ElementFactory.elem(Array("test", "beside")) beside ElementFactory.elem('t', 2, 3)
    //动态绑定 调用
    elements.foreach(e => e.show)
  }
}