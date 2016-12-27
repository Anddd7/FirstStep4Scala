package ExtendsAndCombine

import scala.io.StdIn

/**
  * Created by AnDong on 2016/12/26.
  */
abstract class Animal {
  def bark: Unit
}

class Cat extends Animal {
  println("你召唤了一只猫")

  override def bark: Unit = println("喵喵喵")
}

class Dog extends Animal {
  println("你召唤了一只狗")

  override def bark: Unit = println("汪汪汪")
}

object ZooMgnt {
  def main(args: Array[String]): Unit = {
    var line = ""
    while (true) {
      line = StdIn.readLine()
      if (line.equals("猫") || line.equals("Cat")) {
        new Cat().bark
      } else if (line.equals("狗") || line.equals("Dog")) {
        new Dog().bark
      } else {
        println("动物园里还没有这种动物")
      }
    }
  }
}