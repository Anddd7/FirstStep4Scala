package SecondWithProgrammingInScala


import scala.collection.mutable
import scala.collection.mutable.Stack
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  * 集合库
  * mutable 元素可变(+/=操作是对元素进行修改)
  * immutable  元素不可变(使用var 和 +/=操作符实质是改变的变量指向 ,而不是元素变化)
  *
  */
class Collection {
  //基本序列
  val array = Array(1, 2, 3)
  val list = List(4, 5, 6)
  //缓存序列
  val arrayBuffer = ArrayBuffer(1, 2, 3)
  val listBuffer = ListBuffer(4, 5, 6)
  //队列
  val queue = new mutable.Queue[Int]
  queue.enqueue(1)
  queue.dequeue
  //栈
  val stack = new Stack[Int]
  stack.push(1, 2, 3)
  stack.pop

  //集合
  val set = Set("a", "b", "c")
  //映射
  val map = Map(1 -> "a", 2 -> "b")
  //有序的集合和映射
  val treeSet = mutable.TreeSet(3, 11, 2, 77, 4, 62, 1)
  val treeMap = mutable.TreeMap(5 -> "asf1", 33 -> "asf2", 2 -> "asf3")
  //同步Synchronized的集合和映射 -> Synchronized包下的类

  //初始化多采用工厂模式 ,会自动调用apply方法

  //元组 -> 同第三章

}
