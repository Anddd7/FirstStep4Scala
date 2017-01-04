package SecondWithProgrammingInScala

import scala.collection.mutable.ArrayBuffer

/**
  * 隐含
  * import java.lang._   //everything in the java.lang package
  * import scala._       //everything in the scala package
  * import Predef._      //everything in the Predef object
  *
  * scala类的属性通常也定义为函数(一等公民)
  * 通常是无参函数 只是读取对象的状态
  * 这也是为了减少var的使用 将函数作为值进行传递
  * 每一次获取类的属性 其实就是调用函数并返回新的属性(当前状态的属性)
  *
  * 本章主要用2个打印图像的例子 ,来展示scala的继承重载和函数的应用
  */
abstract class Element {
  //定义的无具体实现的函数 需要子类去实现
  def contents: Array[String]

  //val 和 无参函数 等价
  //但val会稍快一点 val只加载一次 函数调用会有多次
  //但val后续无法修改 无参函数可以重载

  //val height = contents.length
  //val width = if (height == 0) 0 else contents(0).length

  def height: Int = contents.length

  def width: Int


  def show: Unit = contents.foreach(println)

  //引入静态方法
  import ElementFactory.elem

  //如果欲连接的elem比当前elem宽 则扩展当前elem ,左右加上都是' '的elem
  def widen(w: Int): Element = {
    if (w <= width) this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    }
  }

  //如果欲连接的elem比当前elem长 则扩展当前elem ,上下加上都是' '的elem
  def heighten(h: Int): Element = {
    if (h <= height) this
    else {
      val top = elem(' ', width, (h - height) / 2)
      val bottom = elem(' ', width, h - height - top.height)
      top above this above bottom
    }
  }

  //竖向连接 ,判断宽度是否一致 ,是否需要横向扩展
  def above(that: Element): Element = {
    val this1 = this widen that.width
    val that1 = that widen this.width
    elem(this1.contents ++ that1.contents)
  }

  //横向连接 ,判断高度是否一致 ,是否需要上下扩展
  def beside(that: Element): Element = {
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem(for ((line1, line2) <- this1.contents zip that1.contents)
    //yield 是将后面的变量保存到集合中 ,在for循环结束时作为返回值返回
      yield line1 + line2
    )
  }

  //final定义不可以重载/派生子类
  final def author: String = "Andong"

}

class ArrayElement(conts: Array[String]) extends Element {


  //对父类的抽象函数进行实现
  override def contents: Array[String] = conts

  //scala 成员函数和成员变量同级别 并且不可同名 同名时会自动覆盖(重载)
  //val contents: Array[String] = conts
  override def width: Int = if (height == 0) 0 else conts.sortWith(_.length > _.length)(0).length

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
    //elements += ElementFactory.elem(Array("i'm the first", "Array Element"))
    //elements += ElementFactory.elem("i just have one line")
    //elements += ElementFactory.elem('c', 4, 3)
    elements += ElementFactory.elem('*', 4, 5) above ElementFactory.elem("Above LineElem")
    elements += ElementFactory.elem(Array("Beside", "ArrayElem")) beside ElementFactory.elem('&', 4, 5)
    //动态绑定 调用
    elements.foreach(e => {
      e.show
      println()
    })
  }
}

object Spiral {

  import ElementFactory.elem

  //空格
  val space = elem(" ")
  //拐角
  val corner = elem("+")

  //打印函数 nEdgers表示拐角次数 direction表示初始方向(0向下 )
  def spiral(nEdges: Int, direction: Int): Element = {
    if (nEdges == 1) {
      corner //只有一个拐角 起始点
    }
    else {
      //递归打印前一条边 顺时针进行 逆时针回溯到上一个方向
      val sp = spiral(nEdges - 1, (direction + 3) % 4)
      //构造竖线
      def verticalBar = elem('|', 1, sp.height)
      //构造横线
      def horizontalBar = elem('-', sp.width, 1)

      //默认顺时针进行
      val temp = {
        if (direction == 0)
        //向左 -> (转角+ 连接 横边-) 盖住 (原图形 连接 空格 )
          (corner beside horizontalBar) above (sp beside space)
        else if (direction == 1)
        //向下 -> (原图形 盖住 空格 ) 连接 (转角+ 盖住 竖边|)
          (sp above space) beside (corner above verticalBar)
        else if (direction == 2)
        //向右 -> (空格 连接 原图形)盖住 (横边- 连接 转角+)
          (space beside sp) above (horizontalBar beside corner)
        else
        //向上 -> (竖边| 盖住 转角+) 连接 (空格 盖住 原图形)
          (verticalBar above corner) beside (space above sp)
      }
      println(nEdges + "  " + direction)
      temp.show
      temp
    }
  }

  def main(args: Array[String]): Unit = {
    spiral(11, 0)
  }
}