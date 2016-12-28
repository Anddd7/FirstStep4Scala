package FirstInShiyanlou.ObjectAndClass

/**
  * Created by AnDong on 2016/12/20.
  * 创建一个scala类,以分数形式n/d表示有理数
  *
  * 学习trait后对代码进行改写
  * 对接口类方法依赖另一个方法的 ,可以使用胖接口设计
  * 留下最核心的功能让实现者实现 其他的通过相互调用提前在接口中实现
  * 例如实现<
  * >= 也可以使用 !< 实现
  *
  * scala还额外提供了 Ordered Trait ,针对需要比较排序的类 ,只需要实现compare函数 ,就可以进行比较
  */


//类定义时可以放置参数,相当于默认的构造函数,参数只能在函数内部进行使用(相当于private)
class Rational(n: Int, d: Int) extends Ordered[Rational] {

  //使用require校验参数,不通过则抛出异常
  //抓取异常使用 try-catch-case
  try {
    require(d != 0)
  } catch {
    case ex: Exception => throw new MyException
  }

  //求分子分母最大公约数 ,除去符号影响
  val g = gcd(n.abs, d.abs)

  //使用新的内部变量记录n/d ,用于object.n的访问
  val number = n / g
  val denom = d / g

  //内部的代码段(非函数定义)作为构造函数的代码自动顺序执行
  println("create %d/%d".format(n, d))

  //使用override 重载基类函数
  override def toString: String = "%d/%d".format(number, denom)

  //定义辅助构造函数
  def this(n: Int) = this(n, 1)

  //定义方法,支持符号命名
  def +(that: Rational): Rational = {
    new Rational(number * that.denom + denom * that.number, denom * that.denom)
  }

  //重载方法
  def +(i: Int): Rational = {
    new Rational(number + denom * i, denom)
  }

  //使用Ordered Trait 实现compare函数 ,就可以使用各种比较符号
  override def compare(that: Rational) = (this.number * that.denom) - (that.number * that.denom)

  //如果需要引用对象本事(比如返回自己),需要使用this
  def max(that: Rational): Rational = {
    if (<(that)) that
    else this
  }

  //求最大公约数 ,只内部使用
  private def gcd(a: Int, b: Int): Int = {
    if (b == 0) a else gcd(b, a % b)
  }

}

object RationalApp {
  def main(args: Array[String]): Unit = {
    //println(new Rational(1, 0).toString)

    val r1div2 = new Rational(6, 8)
    val r2div3 = new Rational(2, 3)
    println(r1div2.+(r2div3).toString)


    val r5div16 = new Rational(5, 16)
    println((r5div16 + 3).toString)

    if (r1div2 > r2div3) print("大于")
  }
}
