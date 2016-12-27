package ExtendsAndCombine

import scala.io.StdIn

/**
  * Created by AnDong on 2016/12/26.
  * 使用一个动物园来描述组合和继承的关系
  *
  * 组合是have-a  继承是is-a
  */

//trait 特质 类似于Java的接口
trait Animal {
  def bark: Unit
}

class Cat extends Animal {
  println("你抓住了一只猫")

  override def bark: Unit = println("喵喵喵")
}

class Dog extends Animal {
  println("你抓住了一只狗")

  override def bark: Unit = println("汪汪汪")
}

class Zoo {
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