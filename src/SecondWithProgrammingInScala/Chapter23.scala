package SecondWithProgrammingInScala.Chapter23

/**
  * 重访For表达式
  */

case class Person(name: String, isMale: Boolean, children: Person*)

object ForApplication {

  val lara = Person("Lara", false)
  val bob = Person("Bob", true)
  val julie = Person("Julie", false, lara, bob)
  val persons = List(lara, bob, julie)

  def main(args: Array[String]): Unit = {
    /**
      * map方法 : 对每一个子元素执行方法 ,并将返回值记录生成一个Seq(some) ,最后返回一个大的Seq( Seq(some1)...Seq(somen) )
      * flatMap方法 : 类似map ,将返回值记录到同一个Seq(some) ,最后返回Seq(some1...somen)
      */

    //List的高阶函数
    persons
      .filter(p => !p.isMale)
      .flatMap(p => (
        p.children.map(c => (p.name, c.name))
        )
      )

    /**
      * scala的for循环有着更多的结构和功能 ,可以完成递归/查询/过滤等操作
      * p <- persons 生成式
      * if !p.isMale 过滤式
      * name = p.name 定义式
      * yield 保留字
      *
      */

    //for循环
    for (p <- persons if !p.isMale;
         c <- p.children)
      yield (p.name, c.name)
  }

  /**
    * 在一些时候 ,我们提倡多使用List的高阶函数 ,map/filter等 ,他们看起来更直观
    * 但面对一些复杂操作时 ,他们的使用关系也可能变得复杂 ,并不利于阅读
    * 例如 : 找出所有母子的姓名对
    *
    * 这时候就可以使用for-if-yield方法
    * 实质上 ,scala会把
    * 带有yield的编译成filter/flatMap/map方法组合
    * 不带yield的编译成filter/foreach方法组合
    *
    * 转义生成器
    * for(x<-expr1) yield expr2
    * expr1.map(x=>expr2)
    *
    * 转义过滤
    * for(x<-expr1 if expr2) yield expr3
    * for(x<-expr1 filter(x=>expr2)) yield expr3
    * expr1 filter(x=>expr2) map(x=>expr3)
    *
    * 转义两个生成器
    * for(x<-expr1 ;y<-expr2) yield expr3
    * expr1 flatMap( x=> for(x<-expr2) yield expr3 )
    * expr1 flatMap( x=> expr2.map(x=>expr3) )
    *
    * 转义生成器中的模式
    * for((x,y) <- expr1 ) yield expr2
    * expr1 map( case (x,y) => expr2)
    *
    * for("pat" <- expr1 ) yield expr2
    * expr1 filter(
    * case "pat" => true
    * case _ => false
    * ) map( case (x,y) => expr2)
    *
    * 转义定义
    * for(x<-expr1 ; y=expr2) yield expr3
    * for( (x,y)<-  for(x<-expr1) yield (x,expr2)) yield expr3
    *
    * 转义循环
    * for(x<-expr1) body
    * expr1 foreach (x => body)
    */
}
