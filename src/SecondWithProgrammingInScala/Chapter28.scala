package SecondWithProgrammingInScala

/**
  * 对象相等性
  *
  * 如何比较值和对象相等
  *
  * 对象默认比较是确定是否是同一个引用 ,因此对于复杂对象的比较需要重载equals或==方法
  * (默认类型已经重载或定义过)
  * equals使用的是java Object的方法
  */

object Chapter28 {
  val i = 1
  i == 2
  i.equals(2)

}