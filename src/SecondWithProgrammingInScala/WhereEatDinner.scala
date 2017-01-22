package SecondWithProgrammingInScala

import java.awt.Font
import java.io._
import javax.swing.border._

import scala.collection.mutable.ListBuffer
import scala.swing.{BoxPanel, _}
import scala.swing.event.ButtonClicked
import scala.util.Random


object WhereEatDinner extends SimpleSwingApplication {

  override def top: Frame = new MainFrame {
    preferredSize_=(new Dimension(500, 600))
    //参数
    var optionList = DinnerOption.getOptionsFromFile

    //控件
    val optionArea = new BoxPanel(Orientation.Vertical)

    val scrollArea = new ScrollPane(optionArea) {
      border = new CompoundBorder(new TitledBorder("候选食堂"), new EmptyBorder(10, 10, 10, 10))
      maximumSize_=(new Dimension(300, 400))
      minimumSize_=(maximumSize)
    }
    showOptions

    val goBtn = new Button {
      font = new Font("微软雅黑", 2, 20)
      text = "试试手气"
      margin = new Insets(5, 5, 5, 5)
    }
    val addBtn = new Button {
      text = "添加"
    }
    val resultLable = new Label {
      text = "..."
      font = new Font("微软雅黑", 1, 30)
    }
    //标题
    title = "\"今晚去哪吃\"究极终端分期解决器v1.0"
    //主窗口
    contents = new BoxPanel(Orientation.Vertical) {

      border = new EmptyBorder(30, 30, 30, 30)
      //上部功能
      val funcElem = new BoxPanel(Orientation.Horizontal) {
        border = new EmptyBorder(0, 0, 20, 0)
        //按钮组
        val btnGroup = new BoxPanel(Orientation.Vertical) {
          border = new EmptyBorder(10, 10, 10, 10)
          //动作
          listenTo(goBtn, addBtn)
          reactions += {
            case ButtonClicked(x) => if (x == addBtn) toggleAddWindow else if (x == goBtn) goSelection
          }
          contents += goBtn
          contents += addBtn
        }
        contents += scrollArea
        contents += btnGroup
      }

      val resultElem = resultLable
      contents += funcElem
      contents += resultElem
    }


    def toggleAddWindow: Unit = {
      dialog.open()
    }

    val dialog = new Dialog {
      val newFiled = new TextField {
        columns = 10
      }
      val okBtn = new Button {
        text = "确定"
      }
      contents = new BoxPanel(Orientation.Vertical) {
        contents += newFiled
        contents += okBtn
      }

      listenTo(newFiled, okBtn)
      reactions += {
        case ButtonClicked(_) => {
          if (newFiled.text.isEmpty) Dialog.showConfirmation(top, "餐馆名不能为空")
          else DinnerOption.saveOptions(new DinnerOption(optionList.size, newFiled.text))
          showOptions
          this.close
        }
      }
    }

    def goSelection: Unit = {
      val random = new Random(System.currentTimeMillis())
      val point = Math.abs(random.nextInt % optionList.size)
      val opt = optionList(point)
      resultLable.text = opt.name
    }

    def showOptions: Unit = {
      optionList.foreach(opt => optionArea.contents += new Label(opt.toString))
      optionArea.repaint
    }
  }
}

class DinnerOption(val lineNumber: Int, val name: String) {
  override def toString: String = name
}

object DinnerOption {

  /**
    * 工厂构造方法
    *
    * @param lineNumber
    * @param name
    * @return
    */
  def apply(lineNumber: Int, name: String): DinnerOption = new DinnerOption(lineNumber, name)


  /**
    * 目标文件名
    */
  val fileName = "WhereEatDinner-Options.txt"
  /**
    * 文件操作对象
    */
  val file = getFile

  def getOptionsFromFile: List[DinnerOption] = {
    val fileReader = new FileReader(file)
    val lineReader = new LineNumberReader(fileReader)
    val options = new ListBuffer[DinnerOption]

    var end = false
    while (!end) {
      val lineStr = lineReader.readLine()
      if (lineStr == null || lineStr.isEmpty) end = true
      else options.append(new DinnerOption(lineReader.getLineNumber, lineStr))
    }

    fileReader.close()
    lineReader.close()
    options.toList
  }

  /**
    * 保存新的Option
    *
    * @param opts
    */
  def saveOptions(opts: DinnerOption*): Unit = {
    val fileWriter = new FileWriter(file, true)
    opts.filter(!_.name.isEmpty).foreach(x =>
      fileWriter.append(x.name + "\n")
    )
    fileWriter.close()
  }

  /**
    * 暂时没有发现删除 ,解决方案 ,内存List删除目标 ,然后保存刷新剩下的
    *
    * @param opts
    */
  def deleteOptions(opts: DinnerOption*): Unit = {
    val fileWriter = new FileWriter(file)
    opts.filter(!_.name.isEmpty).sortWith(_.lineNumber < _.lineNumber).foreach(x =>
      fileWriter.append(x.name + "\n")
    )
    fileWriter.close()
  }

  /**
    * 获取文件
    *
    * 原本想使用Option[T] ,将结果转化为List ,可以使用for/foreach等操作 ,会自动规避None的选项 ,而不必判断null
    * 但是对于获取指定的资源(单个的) ,使用for过后还是只会取第一个 ,与其for+get(0) ,还是判断null来的直观简便
    * 对于返回不止一个资源 ,并且可能为空的时候 ,使用Option
    *
    * @return
    */
  private def getFile: File = {
    new File(fileName)
  }
}