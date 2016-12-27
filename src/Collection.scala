import java.io.{File, PrintWriter}

import scala.collection.mutable.ArrayBuffer
import scala.io.{Source, StdIn}

/**
  * Created by AnDong on 2016/12/14.
  * 基本操作和数据结构
  */
class Collection {

  //Unit 表示无明显有意义的返回值
  def getTuples(): Unit = {
    //类似list但可以存放不同类型的数据
    val pair = (99, "Luftballons") //实际类型是 Tuple[Int,String]
    println(pair._1)
    println(pair._2)
  }

  def getSets(): Unit = {
    var jetSet = Set("Boeing", "Airbus")
    jetSet += "Cessna"
    println(jetSet.contains("Cessna"))
  }

  def getMaps(): Unit = {

    //定义Map
    var A: Map[Char, Int] = Map() //定义确定类型的空Map
    val romanNumeral = Map(1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V") //定义并初始化Map 自动推断类型

    //操作
    A += ('I' -> 1) //使用 操作符+= 和 范式key->value
    A ++= Map('J' -> 5) //++=结合Map
    println(A('I')) //使用默认的apply函数查找
    println(A.get('I')) //使用get函数
    A -= ('J') //移除

    //获取集合
    A.keys //key集合
    A.values //值集合
    A.isEmpty //是否为空

  }

  def getArray(): Array[String] = {
    //定长数组
    val arr1 = new Array[Double](5) //长度为5的Double类型的数组 默认为0
    val arr2 = Array(1, 2, 3, 4, 5) //使用Array对象的静态方法 初始化并赋值
    val arr3 = Array.fill(5)(3) //用3填充长度为5的数组


    //变长数组
    val arr11 = new ArrayBuffer[String]() //类似StringBuffer 声明空的数组
    arr11 += ("H", "e") //添加元素
    arr11 ++= Array("l", "l", "o") //添加数组
    arr11.insert(2, "*", "%") //插入元素
    arr11.remove(2, 1) //移除指定位置后n个元素

    //多维数组
    val arr21 = new Array[Array[Int]](5) //长度为5的 存放数组 的数组
    val arr22 = Array.ofDim[Double](5, 3, 2) //ofDim通常用来声明多维数组
    val arr23 = new ArrayBuffer[ArrayBuffer[String]]() //变长的多维数组

    //定长/变长转换(多维度时,只转换最外层)
    arr1.toBuffer //转换为ArrayBuffer
    arr11.toArray //转换为Array

    //zip 取数组对应位置形成二维数组
    Array(1, 2, 3) zip Array("a", "b") //Array((1,'a'), (2,'b'))


    getList().toArray
  }

  def getList(): List[String] = {
    //list 不可修改 只能新建一个 并组合新元素或新List
    //:: 是右操作符 因此组合时 用空列表Nil 开始 -> Nil.add(2).add(one)
    val str1 = List("Hello", ",", "scala") ::: "!" :: Nil
    val str2 = List("Hello", ",", "World ", "!")

    //合并list
    str1 ::: str2
  }

  def compare(x: Int, y: Int): Int = {
    //if else函数自带返回效果
    if (x > y) x
    else y
  }

  def transByMatch(args: Array[String]): Unit = {
    args.foreach(arg => {
      arg match {
        case "Hello" => print("欢迎")
        case "World" => print("世界")
        case _ => print(arg)
      }
    })
  }

  def printByWhile(args: Array[String]): Unit = {
    var time = 0
    while (time < args.size) {
      println(args(time))
      time += 1
    }

    time = 0
    do {
      println(args(time))
      time += 1
    } while (time < args.size)

  }

  def printByFor(args: Array[String]): Unit = {
    // <- 被称为 生成器 (generator)
    // obj <- collection(集合)/range(范围)
    for (arg <- args
         if arg != (",") // 使用if 添加过滤器
         if arg != ("!");
         c <- arg.toCharArray //多重循环 使用;分隔
         if c.!=('o')
    ) {
      print(c)
    }

    for (i <- 0 to args.size - 1) {
      println(args(i))
      //println(args.apply(i))
    }

  }

  def printByForeach(args: Array[String]): Unit = {
    args.foreach(println)
    args.foreach(arg => println(arg))
  }

}

object CollectionApp {

  def main(args: Array[String]): Unit = {
    val collectionUtil = new Collection
    //    collectionUtil.compare(10, 23)
    //    collectionUtil.printByFor(collectionUtil.getArray)
    collectionUtil.transByMatch(collectionUtil.getArray)
    //    collectionUtil.printByForeach(collectionUtil.getArray)
    //    collectionUtil.getTuples
    //    collectionUtil.getSets
    //    collectionUtil.getMaps()
  }
}
