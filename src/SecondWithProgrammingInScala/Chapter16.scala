package SecondWithProgrammingInScala

import scala.collection.mutable

/**
  * 之前被scala的书写方式吸引 ,想要以scala的方式去写排序方法
  * 但在实际操作中 ,遇到一些困难
  * 主要在于一些排序方法需要使用"交换"这种操作 ,很显然会违背scala使用变量的条件
  * 而一些排序是以 一步一步 为基础 ,无法拆分成最小的单元 ,进行递归
  * 因此还是有一些局限性
  *
  * [Scala排序简写及效率](http://nneverwei.iteye.com/blog/1251704)
  * scala使用了一些内置函数和递归去实现一些算法 ,使得算法看起来非常漂亮
  * 但实际的效率上并没有那么的"漂亮"
  *
  * 本章就作为一个排序算法复习章节 ,学习一下排序的写法 ,并尽量采用scala的方式
  * 熟悉底层 ,多多封装
  *
  * 外部排序(内存和外存结合)
  * 内部排序(使用内存)
  * -插入排序
  * --直接插入
  * --希尔排序
  * -选择排序
  * --简单选择
  * --堆排序
  * -交换排序
  * --冒泡排序
  * --快速排序
  * -归并排序
  * -基数排序
  *
  * 当数据量大时(内存排序) ,时间复杂度最低的是O(n * log2(n)) : 快速排序 堆排序 归并排序
  * 快速排序是基于比较的内部排序的最好办法 ,当关键字随机分布时平均时间最短
  *
  * 默认从大到小
  */

/**
  * 归并排序  O(n * log2(n))
  * 1.将两个个有序集合 归并成一个有序集合(归并)
  * 2.只有一个元素时 ,必定是排好序的(初始判断条件)
  * 3.因此使用递归 ,将List拆分成最小块(1,2个元素) ,再依次归并上来(拆分)
  */
object MergeSort {
  //1.归并操作 : 这里假设xs ,ys 是已经排好序(从大到小)的List
  //使用了泛型和隐式转换 :[T :Ordering]表示上下文界定的隐式转换,变为Ordering[T]
  def merge[T](xs: List[T], ys: List[T])(implicit ord: Ordering[T]): List[T] = {
    (xs, ys) match {
      //左边为空
      case (Nil, _) => ys
      //右边为空
      case (_, Nil) => xs
      //左右都还有元素 : x是变量 ,匹配任意的List首元素 ; xs1匹配任意List(包括Nil)
      //这里的目的就是取出List第一个元素 ,用x代替 ,参与比较
      case (x :: xs1, y :: ys1) =>
        //如果x比y大 : 则把x放到队首 ,并将剩下的List继续参与下一轮比较
        if (ord.gt(x, y)) x :: merge(xs1, ys) //取走x 剩下xs1参与下次
        else y :: merge(xs, ys1) //取走y 剩下ys1参与下次
    }
  }

  //2.拆分操作 : 依次拆分
  def oper[T](target: List[T])(implicit ord: Ordering[T]): List[T] = {
    val n = target.length / 2
    if (n == 0) target //没有或只有一个元素
    else {
      //从中间拆分List成左右2部分
      val (left, right) = target.splitAt(n)
      //对左右两部分再依次拆分 -> 进入递归 ,直到只有一个元素(一定有序)
      val xs = oper(left)
      val ys = oper(right)
      //假设拆分后的List都是有序的 -> 保证之前的合并都是有序的
      merge(xs, ys)(ord)
    }

    /**
      * 递归会不断的调用oper(left) ,直到最左边的List只包含一个元素(1/2=0) ,返回那个元素
      * 右边同理 ,最初的 xs 和 ys 都只有一个元素  ,然后调用merge(x ,y)
      * 若 x > y ,进行 x :: merge(xs1, y) ,但x只有一个元素 , xs1 = Nil
      * 因此merge(xs1, y) = merge(Nil, y)  = y
      * 然后依次合并 , x::y = List(x,y)
      *
      * 同理 ,类似树形结构 ,从最左端依次运算
      */
  }

  def main(args: Array[String]): Unit = {
    //实例化一个模板类Ordering[] ,重写compare方法 ,然后就可以使用隐式转换
    val intOrder = new Ordering[Int]() {
      override def compare(a: Int, b: Int): Int = a - b
    }

    //合并两个有序列表
    val test1 = merge(List(88, 55, 32, 11, 3), List(120, 54, 44, 38, 5, 2))(intOrder)
    println(test1)

    //归并排序
    val test2 = oper(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))(intOrder)
    println(test2)
  }
}

object InsertionSort {
  /**
    * 直接插入 O(n'2) :
    * 1.将元素依次插入到已经有序的列表中 ,
    * 2.第一个元素一定是有序的 ,
    * 3.递归/循环 从第一个元素进行
    */
  def straight[T](target: List[T])(implicit ord: Ordering[T]): List[T] = {
    //将一个元素x ,插入到已经有序的xs序列
    def insert(x: T, xs: List[T]): List[T] = {
      xs match {
        //有序序列为空 x是第一个元素
        case List() => List(x)
        //取出有序序列的第一个元素
        case y :: xs1 =>
          if (ord.gt(x, y)) x :: xs //x>y(有序的第一个元素) x放首位
          else y :: insert(x, xs1) //x<y y放在首位 将x与剩下的序列继续插入比较
      }
    }

    //判定剩下的无序数组
    target match {
      //剩下的为空 直接返回
      case List() => target
      //取出第一个元素 ,插入到已经排好序的后面的序列
      case first :: other => insert(first, straight(other))
    }

    /**
      * 最后一行会不断的调用straight(other) ,直到序列尾部 x :: Nil
      * 再次调用时候 ,会返回Nil(= List()) ,开始进入insert(first, Nil)操作
      * 依次将之前的元素插入到后面已经排好序的序列里
      */
  }


  /**
    * 希尔排序 不稳定
    * 1.将待排元素序列分割成若干个子序列（由相隔某个“增量”的元素组成的）分别进行直接插入排序，
    * 2.然后依次缩减增量再进行排序，
    * 3.待整个序列中的元素基本有序（增量足够小）时，再对全体元素进行一次直接插入排序。
    *
    * 因为直接插入在元素基本有序的情况下（接近最好情况），效率很高
    */
  def shell[T](target: List[T])(implicit ord: Ordering[T]): List[T] = {
    val temp = target.toBuffer
    val length = target.length

    //取增量因子为长度的一半
    var factor = length / 2
    //factor=1时 ,为直接插入排序
    while (factor > 0) {
      //对每一个序列都进行插入排序(一共有factor个序列)
      for (i <- factor until length) {
        /**
          * 优化了原本逻辑 ,不直接使用插入排序 ,而是参照该元素之前的序列 ,使用冒泡排序递进
          */
        //当前检视的序列在 i 之前共有多少个数
        val pos = i / factor
        //println("factor:" + factor + "    pos:" + pos)
        //比较当前位置的 i元素 和他这个序列 i-j*factor 的大小 ,序列中至少有1个元素
        for (j <- 1 to pos) {
          //当前元素
          val current = i - factor * (j - 1)
          //序列中的前一个元素
          val prev = i - factor * j
          //println("factor:" + factor + "   current:" + current + "    prev:" + prev + "i:" + i + "   j:" + j)
          //当前元素更大 ,插入到前一个元素的位置
          // (因为被分成多个序列 ,子序列中的插入对整个列表看起来是交换位置)
          // 被插入的元素在后移时 ,不能影响其他序列的元素 ,直接使用冒泡的方式
          if (ord.gt(temp(current), temp(prev))) {
            val value = temp(current)
            temp(current) = temp(prev)
            temp(prev) = value
          }
        }
      }
      factor /= 2
    }
    temp.toList
  }

  def main(args: Array[String]): Unit = {
    val intOrder = new Ordering[Int]() {
      override def compare(a: Int, b: Int): Int = a - b
    }
    //插入排序
    val test1 = straight(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))(intOrder)
    //println(test1)
    //希尔排序
    val test2 = shell(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))(intOrder)
    println(test2)
  }
}


object SelectionSort {
  /**
    * 简单选择排序
    * 1.在欲排序的数中 依次选出最大的
    * 2,将最大的与数列的首部的元素交换
    * 3.直到第n-1和n位置的数比较结束
    */

  def selection(target: List[Int]): List[Int] = {

    def sort(xs: mutable.Buffer[Int]): mutable.Buffer[Int] = {
      for (i <- 0 until xs.length) {
        //从位置i开始 找最大值并放在i处
        var maxIndex = i
        for (j <- i + 1 until xs.length) {
          if (xs(maxIndex) < xs(j)) {
            maxIndex = j
          }
        }
        //替换最大值到位置i
        if (maxIndex != i) {
          val max = xs(maxIndex)
          xs(maxIndex) = xs(i)
          xs(i) = max
        }
      }
      xs
    }

    sort(target.toBuffer).toList
  }

  /**
    * 堆排序（Heap Sort）树形选择排序
    */

  def main(args: Array[String]): Unit = {
    //选择排序
    val test1 = selection(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))
    println(test1)
  }
}

object SwapSort {
  /**
    * 冒泡排序（Bubble Sort）
    */
  def bubble(target: List[Int]): List[Int] = {

    def sort(xs: mutable.Buffer[Int]): mutable.Buffer[Int] = {
      for (i <- 0 until xs.length; j <- (i + 1 until xs.length).reverse) {
        if (xs(j) > xs(j - 1)) {
          val max = xs(j)
          xs(j) = xs(j - 1)
          xs(j - 1) = max
        }
      }
      xs
    }

    sort(target.toBuffer).toList
  }

  /**
    * 递归实现
    */
  def sort(list: List[Int]): List[Int] = list match {
    case List() => List()
    case head :: tail => compute(head, sort(tail)) //递归比较第一个元素和后面序列
  }

  def compute(data: Int, dataSet: List[Int]): List[Int] = {
    dataSet match {
      case List() => List(data)
      case head :: tail => if (data >= head) data :: dataSet else head :: compute(data, tail) //当前元素比后序列第一个元素大 放在后序列首位 否则与后序列的子序列比较
    }
  }

  /**
    * 快速排序（Quick Sort）
    */
  def quick(target: List[Int]): List[Int] = {
    target match {
      case Nil => Nil
      case List() => List()
      case head :: tail =>
        val (left, right) = tail.partition(_ > head)
        quick(left) ::: head :: quick(right)
    }
  }


  def main(args: Array[String]): Unit = {
    //冒泡排序
    val test1 = bubble(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))
    println(test1)

    val test11 = sort(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))
    println(test11)

    //快速排序
    val test2 = quick(List(8, 55, 132, 6, 21, 3, 44, 54, 13, 120, 5, 2))
    println(test2)
  }
}

/**
  * 桶排序/基数排序(Radix Sort)
  */
object RadixSort {

}
