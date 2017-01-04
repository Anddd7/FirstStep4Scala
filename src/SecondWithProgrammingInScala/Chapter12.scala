package SecondWithProgrammingInScala


package Zoo {


  import scala.io.StdIn

  /**
    * trait 特质
    * 是重用代码的一个基本单位 用于封装方法和变量
    * 类似于Java的接口 可以继承多个trait(只能继承一个class) 可以有实现
    * =java的 interface+abstract class
    *
    * 使用一个动物园来描述 继承和特质
    * 组合是have-a  继承是is-a
    */

  //可以用trait做大多数事 包括设置字段和状态值
  //但是无法像类一样创建构造参数
  //因此scala倾向于胖接口设计 因为在接口上可以进行处理 可以减少实现者的压力
  //同时调用者可以从胖接口的多种方法上获利
  abstract class Animal {
    def bark: Unit
  }

  trait Body {
    def body: String
  }

  trait Color {
    private val defaultColor = Array("红", "橙", "黄", "绿", "青", "蓝", "紫")

    def color: String = {
      defaultColor((new util.Random).nextInt(defaultColor.length))
    }
  }

  /**
    * 从下面例子可见
    * 1.abstract只能继承一个 ,trait特质可以有多个(with连接)
    * 2.trait特质也可以设置内部变量 ,可以设置使用范围(private只能在特质内)
    * 3.应用胖接口设计 ,对一些接口可以在特质中提前实现 ,不必交给使用者(相比java的接口)
    */
  class Cat extends Animal with Color with Body {
    println("你抓住了一只" + color + "色的" + body + "猫")

    override def bark: Unit = println("喵喵喵")

    override def body: String = "小"
  }

  class Dog extends Animal with Color with Body {
    println("你抓住了一只" + color + "色的" + body + "狗")

    override def bark: Unit = println("汪汪汪")

    override def body: String = "大"
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

}

//胖接口应用 : Ordered Trait
//Ordered Trait 接口内部利用compare函数 ,实现了大于小于等其他比较 ,因此用户只需要实现compare函数就可以使用其他比较符号
//override def compare

package Queue {

  import scala.collection.mutable.ArrayBuffer

  /**
    * 使用Trait处理可叠加的修改操作 创建对象时混合trait
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

}
