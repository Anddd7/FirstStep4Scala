package SecondWithProgrammingInScala

import java.io.File

import scala.swing.{TextField, _}
import scala.swing.event.{ButtonClicked, EditDone}

/**
  * GUI 编程
  * 大致的使用和实际效果和JavaSwing没啥区别
  * 之前使用过Swing和Vaadin(Java->Html/js) ,相比浏览器流派 ,Java客户端适用性还是低一些的
  *
  *
  * 使用swing时候出现了版本问题
  * scala在2.10以后对模块进行了比较大的划分 ,不少包被拆分成了不同的模块
  * 使用maven关联要注意
  * 以scala-library为核心 ,核心版本号2.12去关联其他模块包
  * 官方还提供了一个scala-lang-all ,包括了之前拆分出去的包
  *
  */
object ScalaSwingApp extends SimpleSwingApplication {
  /**
    * 和Java编写方式不同 ,Java大多将参数通过构造函数传递进去
    * Button okBtn = new Button(text = ok)
    * scala而多是使用{}直接重载里面的属性(实质是匿名子类) ,更符合实际 ,不同功能的按钮就是不同的类.
    * val okBtn = new Button{override text = ok}
    * :  new OkButton{override text = ok} extends Button
    */
  //设置程序主窗口
  override def top: Frame = new MainFrame {
    //窗口标题
    title = "Scala Swing App"
    //注册组件
    val clickBtn = new Button {
      text = "Click me"
    }
    val clickBlueBtn = new Button {
      background = java.awt.Color.BLUE
      text = "Click me"
    }
    val tipL = new Label {
      text = "No button clicks registered"
    }
    //设置窗口内容(布局){组件}
    contents = new BoxPanel(Orientation.Vertical) {
      //这里的contents是指BoxPanel布局的内容
      contents += clickBtn
      contents += clickBlueBtn
      contents += tipL
      border = Swing.EmptyBorder(30, 30, 10, 30)
    }
    //设置监听
    listenTo(clickBtn, clickBlueBtn)
    //设置监听回调函数
    var nClicks = 0
    reactions += {
      case ButtonClicked(btn) => {
        nClicks += 1
        if (btn == clickBlueBtn) tipL.text = "You just Click the Blue Button:" + nClicks
        else tipL.text = "Number of button Clicks:" + nClicks
      }
    }
  }
}

/**
  * 简单计算器
  */
object Calculation extends SimpleSwingApplication {
  override def top: Frame = new MainFrame {
    title = "简单计算器"

    //文本组件
    object Elem1 extends TextField {
      columns = 5
    }

    object Elem2 extends TextField {
      columns = 5
    }

    object Func extends TextField {
      columns = 2
    }

    object Result extends TextField {
      columns = 20
    }

    //提示组件
    val tips_defualt = "请输入要计算的值[0-9]和运算符[+-*/]"
    val tipsL = new Label(tips_defualt)
    //设置布局
    contents = new BoxPanel(Orientation.Vertical) {
      //设置提示
      contents += tipsL
      //设置输入框
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += Elem1
        contents += new Label("     ")
        contents += Func
        contents += new Label("     ")
        contents += Elem2
      }
      //设置结果
      contents += Result
      border = Swing.EmptyBorder(30, 30, 30, 30)
    }
    //设置动作
    var errors = 0
    listenTo(Elem1, Elem2, Func)

    reactions += {
      //输入完成
      case EditDone(_) => calculate
    }

    //设置计算函数
    def calculate: Unit = {
      try {
        //复原提示
        tipsL.text = tips_defualt
        //检查运算符并试图转换数值
        Func.text match {
          case "+" => Result.text = (Elem1.text.toInt + Elem2.text.toInt).toString
          case "-" => Result.text = (Elem1.text.toInt - Elem2.text.toInt).toString
          case "*" => Result.text = (Elem1.text.toInt * Elem2.text.toInt).toString
          case "/" => Result.text = (Elem1.text.toInt / Elem2.text.toInt).toString
          case _ => tipsL.text_=("运算符输入错误 ,请重新输入")
        }
      } catch {
        //数值转换出错
        case e: NumberFormatException => tipsL.text_=("数值输出错误 ,请重新输入")
        case e => tipsL.text_=(e.getMessage)
      }
    }
  }

}