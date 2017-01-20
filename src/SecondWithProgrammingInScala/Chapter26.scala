package SecondWithProgrammingInScala

/**
  * 使用XML
  *
  * 读写/查询/拆解
  * scala可以方便的操作xml ,并在里面嵌入代码 ,或是代码中描述xml
  */

import scala.xml.{Elem, Node, XML}

abstract class CCTherm {
  val description: String
  val yearMade: Int
  val dateObtained: Elem
  val bookPrice: Int
  val purchasePrice: Int
  val condition: Int

  override def toString: String = description

  //序列化
  def toXml =
  <cctherm>
    <description>
      {description}
    </description>
    <yearMade>
      {yearMade}
    </yearMade>
    <dateObtained>
      {dateObtained}
    </dateObtained>
    <bookPrice>
      {bookPrice}
    </bookPrice>
    <purchasePrice>
      {purchasePrice}
    </purchasePrice>
    <condition>
      {condition}
    </condition>
  </cctherm>

  //反序列化(通过搜索节点)
  def fromXML(node: Node): CCTherm =
  new CCTherm {
    override val condition: Int = {
      node \ "condition"
    }.text.trim.toInt
    override val description: String = {
      node \ "description"
    }.text
    override val bookPrice: Int = {
      node \ "bookPrice"
    }.text.trim.toInt
    override val purchasePrice: Int = {
      node \ "purchasePrice"
    }.text.trim.toInt
    override val yearMade: Int = {
      node \ "yearMade"
    }.text.trim.toInt
    override val dateObtained: Elem = XML.loadString({
      node \ "dateObtained"
    }.toString.trim)
  }
}

object XMLApp {

  //保存到文件
  def saveAsFile(fileName: String, node: Node) = {
    XML.save(fileName + ".xml", node, "UTF-8", true, null)
  }

  //从文件读取
  def getXMLFromFile(fileName: String): Elem = {
    XML.loadFile(fileName + ".xml")
  }

  //只匹配一层
  def proc(node: Node): String =
  node match {
    case <dateObtained>
      {dateObtained}
      </dateObtained> =>
      "it's obtained in " + {
        dateObtained \\ "year"
      }.toString.trim
    case _ => "unknow"
  }

  def main(args: Array[String]): Unit = {
    //匿名子类
    val term = new CCTherm {
      override val condition: Int = 9
      override val description: String = "hot dog #5"
      override val bookPrice: Int = 2199
      override val purchasePrice: Int = 500
      override val yearMade: Int = 1952
      override val dateObtained: Elem =
        <base>
          <year>2006</year>
          <month>3</month>
          <day>12</day>
          03.12.2006
        </base>
    }

    //获取xml
    val termXML = term.toXml
    println(termXML)

    //保存xml
    saveAsFile("XMLUtilTest", termXML)
    val xmlFromFile = getXMLFromFile("XMLUtilTest")

    //转换成对象
    val obj = term.fromXML(xmlFromFile)
    println(obj)

    //将xml转化成text文字
    val termText = termXML.text
    println(termText)

    //通过签名找到xml子元素(只在根节点下查找)
    val description = termXML \ "description"
    println(description)
    println(termXML \ "day")

    //深度搜索查找子元素
    val dayOfDateObtained = termXML \\ "day"
    println(dayOfDateObtained)

    //模式匹配查找
    println(proc(termXML))
  }
}

