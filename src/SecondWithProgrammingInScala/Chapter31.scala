package SecondWithProgrammingInScala

import scala.util.parsing.combinator.{JavaTokenParsers, RegexParsers}

/**
  * 连接符解析
  *
  * scala提供了一个连接符解释器 ,可以借助他实现一个编译器 ,编译解释你自己的文法
  * [上下文无关:对于文法G = (N, Σ, P, S) ,其中V->w ,V总是能够被w替换而与V的所在上下文无关]
  * [反例 ,汉语 : '本来'可以去成都的. 拿我的笔记'本来' ,"本来"出现的位置决定他的含义]
  */
class Arith extends JavaTokenParsers {
  /**
    * JavaTokenParsers 按照Java的格式识别一些参数值
    *
    * expr ::= term {'+' term | '-' term}
    * term ::= factor {'*' factor | '/' factor}
    * factor ::= floatingPointNumber | '(' expr ')'
    */
  def expr: Parser[Any] = term ~ rep("+" ~ term | "-" ~ term)

  def term: Parser[Any] = factor ~ rep("*" ~ factor | "/" ~ factor)

  def factor: Parser[Any] = floatingPointNumber | "(" ~ expr ~ ")"
}

object ArithParser extends Arith with App {
  println(parseAll(expr, "5+2*7/(3-1)"))
}

object MyParsers extends RegexParsers {
  /**
    * 按正则表达式匹配
    */
  val ident: Parser[String] =
  """[a-zA-Z_]\w*""".r
}

class JSON extends JavaTokenParsers {
  /**
    * 对象:用大括号包裹多条属性
    * obj ::= "{" [members] "}"
    * 数组:用中括号包裹多条值
    * arr ::= "[" [values] "]"
    * 属性:由key:value构成
    * member ::= stringLiteral ":" value
    * 多条属性:属性间用逗号隔开
    * members ::= member {"," member}
    * 值:可以是任意对象/数组/字符/数字/布尔值
    * value ::=
    * 多条值:用逗号分隔
    * values ::= value {"," value}
    */

  //使用^^指定输出解析器 ,会一直追溯到最底层
  //例如从obj开始解析
  //    解析是否是{member}
  //        解析member结构
  //            调用解析器生成(k,v)键值对
  //        调用解析器将m键值对加入Map
  //    返回形成的属性Map
  //解析obj完成
  def obj: Parser[Map[String, Any]] = "{" ~ repsep(member, ",") ~ "}" ^^ { case "{" ~ members ~ "}" => Map() ++ members }

  def arr: Parser[List[Any]] = "[" ~ repsep(value, ",") ~ "]" ^^ { case "[" ~ values ~ "]" => values }

  def member: Parser[(String, Any)] = stringLiteral ~ ":" ~ value ^^ { case k ~ ":" ~ v => (k, v) }

  def value: Parser[Any] =
    obj |
      arr |
      stringLiteral |
      floatingPointNumber ^^ (_.toDouble) |
      "null" ^^ (x => null) |
      "true" ^^ (x => true) |
      "false" ^^ (x => false)
}

object JSONParser extends JSON with App {
  val json = "{\"country\":{\"name\":\"China\",\"province\":[{\"name\":\"北京\",\"citys\":[\"东城区\"]},{\"name\":\"重庆\",\"citys\":[\"九龙坡\",\"万州\",\"涪陵\"]},{\"name\":\"上海\",\"citys\":[\"浦东\"]},{\"name\":\"天津\",\"citys\":[\"海港\"]}]}}"

  println(parseAll(obj, json))
}
