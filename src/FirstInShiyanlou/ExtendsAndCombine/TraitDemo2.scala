package FirstInShiyanlou.ExtendsAndCombine

import scala.collection.mutable.ArrayBuffer

/**
  * Created by AnDong on 2016/12/27.
  * 使用Trait处理可叠加的修改操作
  * 创建时混合trait
  */

//定义一个队列
abstract class IntQueue {
  //返回队首
  def get(): Int

  //加入到队尾
  def put(x: Int)
}

class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]

  def get() = buf.remove(0)

  def put(x: Int) {
    buf += x
  }

  def show {
    println(buf)
  }
}

//trait继承基类 保证继承trait的只能是基类的子类(具有相同的函数)
//如果调用的super.put是一个没有实现的抽象函数 那么在重载的函数前面也要加上abstract
//那么在最后实现的时候 调用的super.put会指向 super->IntQueue->IntQueue子类的put实现
//否则会去寻找IntQueue的实现
trait Doubling extends IntQueue {
  //利用trait重载函数
  abstract override def put(x: Int) {
    //重载的函数调用父类函数 => 父类函数被其子类实现 => 调用子类实现
    super.put(2 * x)
  }
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int) {
    super.put(x + 1)
  }
}

trait Filtering extends IntQueue {
  abstract override def put(x: Int) {
    if (x >= 0) super.put(x)
  }
}

object DemoApp {
  def main(args: Array[String]): Unit = {

    val basicQueue = new BasicIntQueue
    val doubleQueue = new BasicIntQueue with Doubling
    val incrementQueue = new BasicIntQueue with Incrementing
    val filterQueue = new BasicIntQueue with Filtering

    Array(10, 2, 33, -21, -1, 4, -3).foreach(e => {
      basicQueue.put(e)
      doubleQueue.put(e)
      incrementQueue.put(e)
      filterQueue.put(e)
    })

    Array(basicQueue, doubleQueue, incrementQueue, filterQueue).foreach(e => {
      e.show
    })
  }
}
