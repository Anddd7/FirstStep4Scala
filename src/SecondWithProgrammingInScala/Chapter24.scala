package SecondWithProgrammingInScala

/**
  * 抽取器
  *
  * 抽取器可以在匹配的同时进行分解 ,可以直接抽出里面的值
  * 相当与 is是非语句 + get方法
  * 通常配合模式匹配使用 ,在之前章节中匹配并获取数组值 Array(x,y)=> x+y ,也是抽取器
  *
  * 之前的模式匹配和样本类 ,标注了case的样本类可以将参数列表作为匹配项
  * 实质就一个语法糖 ,为样本类同时声称了一个伴生对象(同名object) ,并提供了了apply和unapply
  * 然后模式匹配时候调用unapply ,用于匹配和拆解参数
  *
  * 使用样本类 ,匹配表达式和构造器一致 ,数据和模型是一一对应的
  * 使用抽取器 ,可以为一个模型添加不同的抽取方式(对Email字串执行多种方式的抽取) ,数据和模型是分开的
  */

object Email {
  //抽取方法 : 匹配并分解值
  def unapply(s: String): Option[(String, String)] = {
    val parts = s split "@"
    if (parts.length == 2) Some(parts(0), parts(1)) else None
  }

  //注入方法 : 相对的
  def apply(user: String, domain: String) = user + "@" + domain
}

//比较是否是连续的email
object TwiceEmail {
  def unapply(s: String): Option[String] = {
    val length = s.length / 2
    val half = s.substring(0, length)
    if (half == s.substring(length)) Some(half) else None
  }
}

//判断是否全为大写
object UpperCase {
  //没有设置apply方法 ,在模式匹配时 UpperCase() 是没有参数的
  //如果想要利用(输出)匹配到的参数 ,可以使用 str @ UpperCase()
  def unapply(s: String): Boolean = s.toUpperCase == s
}

//拆解域名
object Domain {
  //拆解成seq
  def unapplySeq(whole: String): Option[Seq[String]] = Some(whole.split("""\."""))
}

object ExtractorsApp {
  //配合模式匹配使用 ,当匹配的是抽取器 ,会自动调用unapply方法获取返回值
  def parser(s: String) = {
    s match {
      case Email(user, domain) => println(user + " AT " + domain)
      case _ => print("not an email address")
    }
  }

  def userTwiceUpper(s: String): Unit = {
    s match {
      case Email(TwiceEmail(user@UpperCase()), domain) => println("Twice,Upper :  " + user + " AT " + domain)
      case Email(user@UpperCase(), domain) => println("Upper :  " + user + " AT " + domain)
      case Email(TwiceEmail(user), domain) => println("Twice :  " + user + " AT " + domain)

      case Email(user@UpperCase(), domain@UpperCase()) => println("Upper :  " + user + " AT " + "Upper :  " + domain)
      case _ => println("not match :  " + s)
    }
  }

  def isSinaEmail(s: String): Boolean = s match {
    case Email(user, Domain("sina", _*)) => true
    case _ => false
  }

  def main(args: Array[String]): Unit = {
    val emails = List("liaoad@asiainfo.com", "liaoadliaoad@aisainfo.com", "LIAOLIAO@sina.com", "LIAOAD@QQ.COM")

    emails.foreach(userTwiceUpper)
    emails.foreach(x => if (isSinaEmail(x)) println(x))
  }
}

/**
  * 正则表达式
  */
object RegexApp {
  def main(args: Array[String]): Unit = {
    import scala.util.matching.Regex
    //使用 """ 三引号表示一个原始字符串 ,里面的字符串默认不为\转义 (\d即匹配数字 ,\.即匹配.)
    //RichString提供了 .r 方法 ,将字符串转化成Regex
    val Decimal =
    """(-)?(\d+)(\.\d*)?""".r
    val input = "for -0.1 to 99 by 3"

    //在字符串中查询数字 生成Seq 打印
    for (s <- Decimal findAllIn input) println(s)
  }
}

