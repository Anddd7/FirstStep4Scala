package FirstInShiyanlou

import java.io.{BufferedReader, File, InputStreamReader, PrintWriter}
import java.net.ConnectException

import scala.io.{Source, StdIn}

/**
  * Created by AnDong on 2016/12/16.
  * 文件操作
  */
class FileOperate {

  def readFromFile(path: String) = {
    val source = Source.fromFile(path)
    val file = source.getLines() // 返回值为Iterator[String]
    file.foreach { line => println(line) }
    source.close()
  }

  def readFromUrl(url: String) = {
    try {
      val source = Source.fromURL(url)
      val file = source.getLines() // 返回值为Iterator[String]
      file.foreach { line => println(line) }
      source.close()
    } catch {
      case ex: ConnectException => println("Url " + url + " is invalid.")
    }
  }

  def writeToFileWithJava() = {
    val pw = new PrintWriter(new File("test.txt"))
    for (i <- 1 to 10) pw.println(i)
    pw.flush()
    pw.close()
  }

  /**
    * Java风格的从控制台读取数据并打印
    */
  def writeToFileFromReadWithJava() = {
    print("Please Enter:")
    val br = new BufferedReader(new InputStreamReader(System.in))
    /**
      *
      * 按照下属方式是可以正常读取并结束的，但是如果是我们平时熟悉的java方式的话：
      * while ((line = br.readLine()) != "bye") {......}则是有问题，IDE提示：
      * comparing values of types Unit and String using '!=' will always yield true
      **/
    var line = br.readLine()
    while (line != "exit") {
      println("Receive from console : " + line)
      print("Please Enter:")
      line = br.readLine()
    }
    println("Exit.")
    br.close()
  }

  /**
    * Scala风格的从控制台读取数据并打印
    * scala.Console
    */
  def writeToFileFromReadWithScala() = {
    print("Please Enter:")
    //var line = Console.readLine()
    var line = StdIn.readLine()
    while (line != "exit") {
      println("Receive from console : " + line)
      print("Please Enter:")
      line = Console.readLine()
    }
    println("Exit.")
  }
}

//object 表示单例静态对象 内部方法和属性都是 static的
//class 不能设置静态的东西
object FileOperateApp {
  def main(args: Array[String]) {
    // 从文件中读数据
    val fileUtil = new FileOperate
    fileUtil.readFromFile("test.txt")

    // 从URL中读数据
    // fileUtil.readFromUrl("http://www.text.com")
    // fileUtil.readFromUrl("https://www.baidu.com")

    // 写数据到文本文件
    fileUtil.writeToFileWithJava()
    fileUtil.writeToFileFromReadWithJava()
    fileUtil.writeToFileFromReadWithScala()
  }
}