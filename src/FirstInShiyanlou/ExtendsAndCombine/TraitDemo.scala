package FirstInShiyanlou.ExtendsAndCombine

/**
  * Created by AnDong on 2016/12/27.
  * 应用Trait书写绘图程序库
  */

//定义基本坐标点
class Point(val x: Int, val y: Int)

//抽取共有属性
trait Rectangular {
  def topLeft: Point

  def bottomRight: Point

  def left = topLeft.x

  def right = bottomRight.x

  def top = topLeft.y

  def bottom = bottomRight.y

  def width = right - left

  def height = bottom - top
}

//用2点定义矩形
class Rectangle(val topLeft: Point, val bottomRight: Point) extends Rectangular {
}

//定义Ui元素
abstract class Component extends Rectangular {

}

