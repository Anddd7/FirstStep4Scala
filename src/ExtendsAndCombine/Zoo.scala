package ExtendsAndCombine

import scala.io.StdIn

/**
  * Created by AnDong on 2016/12/26.
  * 使用一个动物园来描述组合和继承的关系
  *
  * 组合是have-a  继承是is-a
  */

//trait 特质
//是重用代码的一个基本单位 用于封装方法和变量
//类似于Java的接口 可以继承多个trait(只能继承一个class) 可以有实现
//=java的 interface+abstract class
trait Animal {
  def bark: Unit
}

//可以用trait做大多数事 包括设置字段和状态值
//但是无法像类一样创建构造参数
//因此scala倾向于胖接口设计 因为在接口上可以进行处理 可以减少实现者的压力
//同时调用者可以从胖接口的多种方法上获利
trait Color {
  val defaultColor = Array("红", "橙", "黄", "绿", "青", "蓝", "紫")

  def color: String = {
    defaultColor((new util.Random).nextInt(defaultColor.length))
  }
}

class Cat extends Animal with Color {
  println("你抓住了一只" + color + "色的猫")

  override def bark: Unit = println("喵喵喵")
}

class Dog extends Animal with Color {
  println("你抓住了一只" + color + "色的狗")

  override def bark: Unit = println("汪汪汪")
}

class Zoo {
  //private 只能类/对象本身访问
  //protect 类和派生类可访问
  //public 默认权限 公开访问
  private var animalMap: Map[String, Animal] = Map()
  initZoo

  def findAnimal(name: String): Animal = {
    animalMap(name)
  }

  private def initZoo: Unit = {
    //多态
    animalMap += ("dog" -> new Dog)
    animalMap += ("cat" -> new Cat)
  }
}

object ZooMgnt {
  def main(args: Array[String]): Unit = {
    val zoo = new Zoo

    var line = ""
    while (true) {
      line = StdIn.readLine()
      try {
        zoo.findAnimal(line.trim.toLowerCase).bark
      } catch {
        case ex: Exception => println("动物园里还没有这种动物")
      }
    }
  }
}