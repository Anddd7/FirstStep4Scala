package SecondWithProgrammingInScala.Chapter3 {

  import scala.collection.mutable.ListBuffer

  /**
    * 使用类型参数化数组Array
    * 使用new实例化对象(class类) ,实例化时可以用值和类型使其参数化
    * 定义类型和访问元素的方式有所不同
    * arg[1] -> arg(1)
    * List<String> -> Array[String]
    */
  object Part1 {
    //使用val表示该变量不能再赋值 ,但所指的对象内部却可以改变
    val greetStrings = new Array[String](3)
    greetStrings(0) = "Hello"
    greetStrings(1) = ","
    greetStrings(2) = "world!\n"

    //可以使用 x.to(y) 表示一个Range ,函数只有一个参数时 ,可以省略.和后者的括号(无歧义)
    for (i <- 0 to 2) println(greetStrings(i))

    /**
      * scala的操作符 + - * /
      * scala允许将字符作为函数名 ,因此+实际上是一个函数
      * 如 1+2 ,实质是调用定义在1(Int)类中的+函数 ,然后将2作为参数传递进去
      */
    val two = 1.+(1)
    val three = 1 + 2

    /**
      * scala使用括号访问数组元素
      * scala的数组也是类的实例 ,用括号传递参数实际是对apply方法的调用 ,所以访问数组元素也是调用的方法
      * 适用于所有类 ,对对象的值参数应用都转换为apply的调用(前提是定义过apply方法)
      */
    greetStrings.update(0, "See")
    greetStrings.update(1, "U")
    greetStrings.update(2, "Again!\n")
    for (i <- 0 to greetStrings.length - 1) println(greetStrings.apply(i))

    //scala风格初始化数组 ,类型和值参数(数组大小)会自动推断
    val numbers = Array(1, 2, 3)
  }

  /**
    * 使用列表List(不可变对象序列)
    * 即便Array在声明时是val ,他内部的元素依旧是可变的 ,可以.update
    * 但List(scala.List)创建后就无法改变 ,用于实现函数式风格
    *
    * 函数式风格 : 方法没有副作用 ,只计算并返回(不会去改变数据状态?)
    * 优点1 方法间耦合度降低 ,可靠且易于重用 ;优点2 方法参数和返回值经过类型检查器(静态类型语言) ,避免类型错误
    * 对应到面向对象 ,就意味着对象一旦建立就不再改变(final) ,函数每次都返回新的new
    */
  object Part2 {
    //初始化List
    val oneTwo = List(1, 2)
    val threeFour = List(3, 4)

    //不能改写(update不会报错 但无效)
    //threeFour(1) = 1 //error
    //threeFour.updated(1, 1) //无效的
    //可以访问
    println(threeFour(1))

    //List连接List(类似addAll操作)
    val oneTwoThreeFour = oneTwo ::: threeFour
    println(oneTwo + " and " + threeFour + " are still holding old data")
    println("thus " + oneTwoThreeFour + "is a new List")

    //List连接元素(类似add) 从头部
    val twoThree = List(2, 3)
    val oneTwoThree = 1 :: twoThree
    println(oneTwoThreeFour)

    //::是右操作数(List)的方法 scala中以':'结尾的方法都是右操作数方法
    //val twoThreeFour = twoThree :: 4 //error 整数没有::方法
    //为了方便的从尾部串接元素 ,提供了 Nil 空列表 ,通过调用Nil的::方法 ,将元素从右至左串接到头部
    val twoThreeFour = 2 :: 3 :: 4 :: Nil
    println(twoThreeFour)
    /**
      * Scala的list不提供append(java.util.List的add方法)
      * 因为append耗时会随着List的容量变化O(n) ;而::前缀链接是固定时间O(C1) ,取决于前缀连接的集合大小
      * List (1,2,3) 可以理解为 1 -> (2 -> (3 -> List()))
      * 对于::          4 -> ( 1 -> (2 -> (3 -> List())) )
      * 对于append  1 -> (2 -> (3 ->( 4-> List()  )))
      * 显然append需要遍历整个List到队尾进行插入
      */
    //Scala也提供了ListBuffer ,具有append操作
    val numBuffer = ListBuffer(11, 12, 13)
    numBuffer.append(14)
    numBuffer += 15
    println(numBuffer)

    /* 通过ListBuffer的+=定义 ,解析List和ListBuffer的效率差别

    def +=(x: A): this.type = {
       if (exported) copy()
       if (start.isEmpty) {
         last0 = new ::(x, Nil) start = last0
       }
       else {
         val last1 = last0
         last0 = new ::(x, Nil) last1.tl = last0
       }
       len += 1 this
     }

     ListBuffer内部也是使用的List ,每次+=就是替换List的队尾元素
     因此随着last0的变大 ,复制last1占用的时间空间都会增长
     如果把List的::操作时间设为O(C1) (C1 = 与连接到头部的List大小相关)
     ListArray的+=操作时间就是O(C2) (C2 = 与已有List大小有关)

     在做相同操作时 ,一开始C1与C2区别不大 ,在数据量很大时 ,如果同时添加一个1 ,C2 >> C1=1
     如果需要相同顺序的列表 ,可以对List使用reverse
     List          t1 = O(C1)*n +O(n)
     ListArray   t2 = O(C2)*n

     数据量大的时候还是List更好 ,数据量较小的化为了方便可以使用ListArray
     */

    //List基本操作 ,所有操作都是函数式的 ,结果都通过返回值返回 ,原有List没有变化
    val empty = List()
    val group1 = List("盖伦", "拉克丝", "VN")
    val group2 = "凯南" :: "狼人" :: Nil
    val blueTeam = group1 ::: group2
    //返回第一个
    println(blueTeam.head)
    //返回最后一个
    println(blueTeam.last)
    //返回除第一个剩下的list
    println(blueTeam.tail)
    //返回除最后一个剩下的list
    println(blueTeam.init)
    //去掉前两个元素
    println(blueTeam.drop(2))
    //去掉后两个元素(从右)
    println(blueTeam.dropRight(2))
    //用", "连接元素形成字符串
    println(blueTeam.mkString(", "))
    //倒序排列
    println(blueTeam.reverse)

    //使用函数字面量遍历元素
    //是否存在 ,返回boolean
    println(blueTeam.exists(_ == "VN"))
    //过滤 ,返回过滤后的新List
    println(blueTeam.filter(_.length < 3))
    //对所有元素判断语句的正确 ,返回boolean
    println(blueTeam.forall(_.length <= 3))
    //计算元素长度为3的元素个数 ,返回int
    println(blueTeam.count(_.length == 3))
    //对每个元素执行 ,返回生成的新List
    println(blueTeam.map(_.length == 3))
    //除去长度为3的 ,true继续 ,false弹出 ,返回新List
    println(blueTeam.dropWhile(_.length < 3))
    //排序 ,返回排序成功的List
    println(blueTeam.sortWith((s, t) => s.length > t.length))
  }

  /**
    * 使用元组Tuple ,没有类型参数的List
    */
  object Part3 {
    //可以存放不同类型 ,因此无法用foreach这些去统一的操作里面的元素 ,只能分别访问 ,并且从_1开始
    val pair = (99, "Luftballons")
    println(pair._1)
    println(pair._2)

  }

  /**
    * 使用集Set ,映射Map
    * scala致力于区分可变和不可变(val/var ,List/Array)
    * Set和Map也有各自的不同版本 ,通过import不同包mutated/immutable
    */
  object Part4 {
    //初始化jetset ,调用immuta.Set.apply ,scala编译器推断为不可变的set
    var jetSet = Set("Boeing", "Airbus")
    //调用不可变set的+=方法 ,类似于List的:: ,是从新生成了一个Set ,并赋值给Set
    jetSet += "Lear"
    println(jetSet.contains("Cessna"))

    //通常你使用var的set是不符合函数式编程的规范的
    //你需要的是一个不变的jetSet ,和可变的Set元素
    import scala.collection.mutable.Set

    val movieSet = Set("Hitch", "Poltergeist")
    movieSet += "Shrek"
    println(movieSet)

    /**
      * 使用 val和mutable 代替 var和immutable
      * 确定你需要改变的是 Set本身还是他的内容
      */

    //HashSet
    import scala.collection.immutable.HashSet

    val hashSet = HashSet("Tomatoes", "Chilies")
    print(hashSet + "Coriander")

    //Map
    import scala.collection.mutable.Map

    val treasureMap = Map[Int, String]()
    treasureMap += (1 -> "Go to island.")
    treasureMap += (2 -> "Find big X on ground")
    treasureMap += (3 -> "DIg.")
    println(treasureMap(2))

    val romanNumberal = Map(1 -> "Ⅰ", 2 -> "Ⅱ", 3 -> "Ⅲ", 4 -> "Ⅳ", 5 -> "Ⅴ")
    println(romanNumberal(4))
  }

  /**
    * 函数式风格 ,代码简洁逻辑清晰 ,不易出错
    * 去掉var : 使用for/foreach代替while
    * 减少副作用(判断返回结果是否是Unit) : 返回Unit ,那么函数的作用就不是 接受->返回 ,而是一些其他作用
    *
    * 权衡使用 ,val/var并不是绝对的
    */
  object Part5 {
  }

  /**
    * 简单的读取文件
    */
  object Part6 {
    //通过文件名读取 默认路径在 $projectPath/
    def printFormatFile(fileName: String): Unit = {
      import scala.io.Source

      def widthOfLineNum(s: String) = s.length.toString.length

      if (fileName.length > 0) {
        //getLines获得的是iterator ,只能迭代一次 ,有多个循环时切换成List
        val lines = Source.fromFile(fileName).getLines().toList

        var maxWidthOfLine = 0
        for (line <- lines)
          maxWidthOfLine = maxWidthOfLine.max(widthOfLineNum(line))

        for (line <- lines) {
          val padding = " " * (maxWidthOfLine - widthOfLineNum(line))
          println(padding + line.length + "|  " + line)
        }
      }
      else
        Console.err.println("Please enter filename")
    }
  }

  object Chapter3App {
    def main(args: Array[String]): Unit = {
      //Part1
      //Part2
      //Part3
      //Part4
      Part6.printFormatFile("README.md")
    }
  }

}
