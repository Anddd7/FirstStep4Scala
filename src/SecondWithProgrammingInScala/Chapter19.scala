package SecondWithProgrammingInScala

import java.util.Comparator

/**
  * scala的泛型和约束[类型系统]
  * 书上的例子比较散 ,所以参考了其他资料来了解scala这个最强大的特性
  */

//1.泛型 (类似Java)
class Reference[T] {
  private var contents: T = _

  def set(value: T) {
    contents = value
  }

  def get: T = contents
}

object ReferenceApp {
  def main(args: Array[String]) {
    val cell = new Reference[Int]
    cell.set(13)
    println("Reference contains the half of " + (cell.get * 2))

    val cell2 = new Reference[String]
    cell2.set("33")
    println("Reference contains the number " + cell2.get)
  }
}

//2.类型变量界定 : 划定一个类型的范围
//类似java中 List<? extends Object> list = new ArrayList<String>(); ,限制类型
// <:泛型类型限定符，Comparable[T]是类型T的上界，T是Comparable[T]的下界
class Pair[T <: Comparable[T]](val first: T, val second: T) {
  //表示T必须是Comparable的子类 ,可以使用compareTo方法进行比较，如果大于0返回first
  def bigger = if (first.compareTo(second) > 0) first else second
}

class PairLowerBound[T](val first: T, val second: T) {
  // 传入的参数泛型T必须为 R的子类 ,返回构造Pair_Lower_Bound对象 ,R是T的上界，T是R的下界
  // new了一个父类型
  def replaceFirst[R >: T](newFirst: R) = new PairLowerBound[R](newFirst, second)
}

object TypeVariableBoundsApp {
  def main(args: Array[String]): Unit = {
    val pair = new Pair("Spark", "Hadoop")
    println(pair.bigger)

    val pairLowerBound = new PairLowerBound("Int", 12)
    println(pairLowerBound.first.getClass)
    println(pairLowerBound.replaceFirst('I').first.getClass)
  }
}

//3.视图界定 : 自动的隐式转换
//视图界定(Views Bounds) ,其符号为 T <% S，关系和上界类似
//如果类型T不是Ordered[T]的子类 ,会进行隐式转换成Ordered的子类 ,再使用这个方法
class ViewBounds[T <% Ordered[T]](val first: T, val second: T) {
  def compare = if (first > second) 1 else 0
}

class ViewBounds2[T](val first: T, val second: T)(implicit ord: T => Ordered[T]) {
  def compare = if (ord(first) > second) 1 else 0
}


object ViewBoundsApp {
  def main(args: Array[String]): Unit = {
    //Int(没有Ordered方法)->RichInt
    val c = new ViewBounds[Int](2, 1)
    println(c.compare)
  }
}

//4.上下文界定 : 为隐式参数引入的语法糖 ,使隐式转换简洁
object ContextBound {
  //隐式参数的做法
  def max1[T](a: T, b: T)(implicit cp: Comparator[T]) = {
    if (cp.compare(a, b) >= 0) a else b
  }

  //使用上下文界定 : 将参数列表中的隐式转换移到内部
  def max2[T: Comparator](a: T, b: T) = {
    //def inner(implicit c: Comparator[T]) = c.compare(a, b)
    //if (inner > 0) a else b
    val cp = implicitly[Comparator[T]]
    if (cp.compare(a, b) > 0) a else b
  }

  def main(args: Array[String]): Unit = {
    implicit val c = new Comparator[Int] {
      override def compare(a: Int, b: Int) = a - b
    }

    println(max1(5, 6))
    println(max2(5, 6))
  }
}


//5.组合界定符
/*
  表示：A和B为T上界
  T <: A with B
  表示 ： A和B为T下界
  T >: A with B
  表示 ： 同时拥有上界和下界 ， 并且A为下界 ， B为上界 ， A为B的子类 ， 顺序不能颠倒 。
  T >: A <: B
  表示 ： 类型变量界定 ， 即同时满足AT这种隐式值和BT这种隐式值
  T: A: B
  表示 ： 视图界定 ， 即同时能够满足隐式转换的A和隐式转换的B
  T <% A <% B
*/


//6.泛型关键字
//Java中的泛型是编译时的 ,编译后会对泛型继续擦除
//List<String>,List<Int>到了JVM中都是List类型 ,只是会对他加上String,Int的强制转换
//因此如果运行时需要判断类型 ,就要对类型参数进行保存
object KeyWord {
  /**
    * scala在2.10里用TypeTag替代了Manifest,用ClassTag替代了ClassManifest
    * 因为Manifest在路径依赖系统中有问题
    * 例如 类A{类B}
    * 类B的类型会随着类A的不同实例而不同
    * 然而Manifest判断是相同的
    */
  //ClassTag : 把原始类型T保存在方法上下文
  //数组必须明确具体类型 ,但只有在函数运行时才会知道类型 ,这时候泛型已经被擦除了
  import scala.reflect.ClassTag

  def mkArray1[T: ClassTag](elems: T*) = Array[T](elems: _*)

  //TypeTag不仅包含类的类别信息，还包含了所有静态的类信息
  //在没有路径歧义的地方 ,使用Manifest也可以 ,官方建议替换
  import scala.reflect.runtime.universe._

  def matchType[T](x: List[T])(implicit tag: TypeTag[T]) = {
    if (typeOf[T] =:= typeOf[String])
      println("Hey, this list is full of strings")
    else
      println("Non-stringy list")
  }

  def main(args: Array[String]): Unit = {
    mkArray1(1, 2).foreach(println)
    matchType(List("1"))
    matchType(List(1))

    //7.类型约束
    // A =:=B // 表示A类型等同于B类型
    // A <:<B   // 表示A类型是B类型的子类
    def rocky[T](i: T)(implicit ev: T <:< java.lang.String) {
      println("Life is short ,you need spark!!!")
    }
    //rocky(1) 无法运行
    rocky("Spark")
  }
}


//8.协变和逆变
/**
  * covariant协变 : C[+T]：如果A是B的子类，那么C[A]是C[B]的子类。
  * contravariance逆变 : C[-T]：如果A是B的子类，那么C[B]是C[A]的子类。
  * Invariance不变(默认) : 只能使用原始类型 . C[T]：无论A和B是什么关系，C[A]和C[B]没有从属关系。
  */
//父类
class Person

//子类
class Student extends Person

//协变父类
class C[+T](val args: T)

//协变子类 继承协变类型
class S[+T](args: T) extends C[T](args)

object CovariantApp {
  def main(args: Array[String]): Unit = {
    //创建泛型为Student类的类C
    val child = new S[Student](new Student)
    //因为开启了协变 ,S[Person]是S[Student]的父类
    val parent: S[Person] = child
    //同样C是S的父类 ,C[Person]也是S[Student]的父类
    val parent1: C[Person] = child
  }
}


