package SecondWithProgrammingInScala

/**
  * 样本类case class和模式匹配pattern matching
  * [实验楼专题课程](https://www.shiyanlou.com/courses/514)
  * 编写一个操作数学表达式的库
  */

/**
  * 使用case关键字定义的即为样本类
  * 1.scala编译器会自动为其加入同名的工厂方法(类似Array(1,2))
  * 2.参数列表会被当做字段维护 ,可通过.访问
  * 3.编译器自动添加了toString,hashCode,equals"自然"实现(简单粗暴)
  * ps.因为带有编译器自动添加的部分 ,所以实际的类稍大一些
  */
//表达式基类
//使用 sealed 封闭样本类层级 ,未覆盖的类会warning
sealed abstract class Expr {
  /**
    * 模式匹配(使用match-case 类似switch-case)
    * 在表达式中 ,类似 -(-1)=1 / x+0=x / x*1=x 这样可以简化的操作 ,可以通过模式匹配预先处理掉
    * 常量模式(constant pattern) : case 1/"+" ,用来精确匹配 ,主要是判断是否==
    * 变量模式(variable) : 类似下面的e ,用来指代所有可能的指
    * 通配模式(wildcard pattern) : 使用_ ,指代所有可能的值
    * 构造器模式(constructor) : 使用类作为匹配项 ,检查构造函数的参数是否相同
    */

  //简化表达式
  def simplifyTop: Expr = {
    this match {
      case UnOp("-", UnOp("-", e)) => e //双重负号
      case BinOp("+", e, Number(0)) => e //+0
      case BinOp("+", Number(0), e) => e //+0
      case BinOp("*", Number(1), e) => e //*1
      case BinOp("*", e, Number(1)) => e //*1

      //两次绝对值 消除一次
      //case UnOp("abs", UnOp("abs", e)) => UnOp("abs", e)
      //变量绑定: 使用@ ,对pattern中的变量再进行一次匹配 ,而变量的值可以直接利用
      case UnOp("abs", e@UnOp("abs", _)) => e

      //模式守卫(pattern guard) : 模式之后的布尔表达式
      case BinOp("+", x, y) if x == y => BinOp("*", x, Number(2))

      case _ => this
    }
  }

  //获取简化后的表达式
  def getExpr: Expr

  //获取格式化的表达式 简化书上的例子
  def formatExpr: Array[String]
}

//变量
case class Var(name: String) extends Expr {
  def getExpr: Expr = this

  def formatExpr: Array[String] = Array(name)
}

//数值
case class Number(num: Double) extends Expr {
  def getExpr: Expr = this

  def formatExpr: Array[String] = Array(num.toString)
}

//单个操作符 -1
case class UnOp(operator: String, right: Expr) extends Expr {
  def getExpr: Expr = {
    val arg1 = right.getExpr
    UnOp(operator, arg1).simplifyTop
  }

  def formatExpr: Array[String] = {
    val rightExpr = right.formatExpr
    val rightHeight = rightExpr.length
    val resultExpr = new Array[String](rightExpr.length)

    for (i <- 0 to rightHeight - 1) {
      if (i == (rightHeight - 1) / 2) resultExpr(i) = operator + rightExpr(i)
      else resultExpr(i) = " " + rightExpr(i)
    }

    resultExpr
  }
}


//中缀操作符 a + b
case class BinOp(operator: String, left: Expr, right: Expr) extends Expr {
  def getExpr: Expr = {
    val left1 = left.getExpr
    val right1 = right.getExpr
    BinOp(operator, left1, right1).simplifyTop
  }

  def formatExpr: Array[String] = {
    operator match {
      case "/" => formatDiv
      case _ => formatNormal
    }
  }

  def formatNormal: Array[String] = {
    val leftExpr = paddingTop(left.formatExpr, right.formatExpr)
    val rightExpr = paddingTop(right.formatExpr, left.formatExpr)
    val height = leftExpr.length

    val resultExpr = new Array[String](rightExpr.length)

    for (i <- 0 to height - 1) {
      if (i == (height - 1) / 2) resultExpr(i) = leftExpr(i) + " " + operator + " " + rightExpr(i)
      else resultExpr(i) = leftExpr(i) + "   " + rightExpr(i)
    }

    resultExpr
  }

  def formatDiv: Array[String] = {
    val leftExpr = paddingLeft(left.formatExpr, right.formatExpr)
    val rightExpr = paddingLeft(right.formatExpr, left.formatExpr)

    val width = leftExpr(0).length
    val opLine = "-" * width

    val opArray = Array(opLine)
    leftExpr ++ opArray ++ rightExpr
  }

  //统一高度
  def paddingTop(a: Array[String], b: Array[String]): Array[String] = {
    if (a.length >= b.length) a
    else {
      val top = (b.length - a.length) / 2
      val bottom = b.length - a.length - top

      val line = " " * a(0).length
      val topArray = Array.fill(top)(line)
      val bottomArray = Array.fill(bottom)(line)

      topArray ++ a ++ bottomArray
    }
  }

  //统一宽度
  def paddingLeft(a: Array[String], b: Array[String]): Array[String] = {
    if (a(0).length >= b(0).length) a
    else {
      val left = (b(0).length - a(0).length) / 2
      val right = b(0).length - a(0).length - left

      val leftStr = " " * left
      val rightStr = " " * right
      for (aa <- a) yield leftStr + aa + rightStr
    }
  }
}


object CaseClassApp {
  //是否有变量
  def hasVar(expr: Expr): Boolean = {
    expr match {
      case Var(anyVar) => true
      case UnOp(anyOp, e) => hasVar(e) //递归校验
      case BinOp(op, left, right) => if (hasVar(left) || hasVar(right)) true else false
      case _ => false
    }
  }

  def main(args: Array[String]): Unit = {
    val x = Var("x")
    val xAdd1 = BinOp("+", Number(1), x)
    println(xAdd1)

    val a = Var("a")
    val b = Var("b")
    val c = Var("c")

    val aAdd0 = BinOp("+", a, Number(0))
    val bMult1 = BinOp("*", b, Number(1))
    val cc = UnOp("-", UnOp("-", c))

    val test1 = BinOp("/", BinOp("-", aAdd0, bMult1), cc).getExpr
    println(test1)
    println("hasVar?  " + hasVar(test1))

    val test2 = BinOp("-", BinOp("/", Number(24), Number(1)), UnOp("-", Number(1))).getExpr
    println(test2)
    println("hasVar?  " + hasVar(test2))

    //反向使用模式匹配拆解
    val BinOp(op, left, right) = test1
    println("op:%s    left:%s    right:%s".format(op, left, right))

    //序列化打印
    test1.formatExpr.foreach(println(_))
    test2.formatExpr.foreach(println(_))
  }
}


object PatternMore {
  def arrayMatch(any: Any): Unit = {
    any match {
      case List(0, _*) => println("0开始的任意长度的List")
      case List(1, 2) => println("1,2的List")
      case List(2, 3, 4, 5) => println("2,3,4,5的List")
      case _ => println("其他")
    }
  }

  def tupleMatch(any: Any): Unit = {
    any match {
      case (a, b, c) => println("任意三元组")
      case ("a", _) => println("第一个是a的二元组")
      case _ => println("其他")
    }
  }

  /**
    * Map[_, _],List[Double] : 因为泛型擦除(erasure) ,运行期的MapList是不会检查类型参数的 ,因此无法判断元素类型
    * Array[String] : Array被特殊处理 ,元素类型与数组值存放在一起 ,因此可以判断类型(像java中是直接用类型定义数组 Int[])
    */
  def typeMatch(any: Any): Int = {
    any match {
      case s: String => s.length
      case m: Map[_, _] => m.size
      case l: List[Double] => l.length
      case a: Array[String] => a.length
      case t: (_, _, _) => 3
      case _ => 1
    }
  }

  def main(args: Array[String]): Unit = {
    println("序列模式")
    val list = List(
      List(0, 1, 2, 3), List(1, 2, 3, 4), List(2, 3, 4, 5), List(3, 4, 5, 6)
    )
    list.foreach(arrayMatch(_))

    println("元组模式")
    val tuple = List(
      ("a"), ("a", "1"), (1, 2, 3, 4), (5, 6, 7)
    )
    tuple.foreach(tupleMatch(_))

    //识别List 类型参数无效
    println(typeMatch(list))

    //识别String
    println(typeMatch("abcddd"))

    //元组识别
    println(typeMatch(1, 2))
    println(typeMatch((1, 2, 3)))

    //数组识别 增加了String类型参数
    println(typeMatch(Array(1, 2)))
    println(typeMatch(Array("1", "11", "111", "1111", "11111")))
  }
}

/**
  * Java8也加入了Optional类做类似的事
  * 使用Option作为返回值 ,简化判定返回null的操作
  *
  * Option[String] = Some(Object) //正确返回
  * Option[String] = None //找不到元素
  */
object OptionTest {
  def main(args: Array[String]): Unit = {
    val capitals = Map("F" -> "France", "J" -> "Japan")

    //1.使用getOrElse方法 添加默认操作
    (capitals get "D").getOrElse(println("没有该元素"))

    //2.使用模式匹配
    def showCapital(x: Option[String]) = x match {
      case Some(s) => s
      case None => "?"
    }
    println(showCapital(capitals get "D"))
  }
}