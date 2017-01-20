package SecondWithProgrammingInScala

/**
  * Actor和并发
  *
  * Java采用同步 synchronized ,锁的机制能有效的控制共享数据的操作 ,但是也带来一些死锁和效率上的问题 ,非常依赖程序猿的经验
  * Scala提供了一套模型 :actor ,不共享数据 ,依赖消息传递 (scala原生的actor已替换成akka.actor)
  */

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.duration.Duration

//样本类 匹配参数
case class Greeting(who: String)

//抽取器 抽取参数
object Talk {
  def unapply(question: String): Option[String] = question match {
    case "hello" => Option("hi")
    case "nice to meet you" => Option("nice to meet you ,too")
    case _ => Option("what?")
  }
}

//构造Actor类 ,定义收到消息的反应
class HelloActor extends Actor {
  def receive = {
    case Greeting(who) => println("Welcome ," + who)
    case Talk(answer) => println(answer)
    case _ => "enh?"
  }
}

object ActorTest {
  def main(args: Array[String]): Unit = {
    //构建Actor中心系统 ,Akka系统消耗比较大，一个应用最好只构建一个
    val system = ActorSystem("HelloSystem")
    //注册接收器 和 映射的处理类
    val helloActor = system.actorOf(Props[HelloActor], name = "helloActor")

    helloActor ! Greeting("liaoad")
    helloActor ! "hello"
    helloActor ! "how r you"
    helloActor ! "nice to meet you"

  }
}

/**
  * akka 主从actor工作系统
  *
  * 使用多个工作器 计算pi的值
  */

import akka.actor._
import akka.routing.{RoundRobinGroup, RoundRobinPool}

//发送的信息
sealed trait PiMessage

case object Calculate extends PiMessage

//工作内容 ,主->从(工作器)
case class Work(start: Int, nrOfElements: Int) extends PiMessage

//工作结果 ,从(工作器)->主
case class Result(value: Double) extends PiMessage

//监听数据 ,主->从(监听器)
case class PiApproximation(pi: Double, duration: Duration)

//工作Actor
class Worker extends Actor {
  //接受工作 ,回复工作时间
  def receive = {
    case Work(start, nrOfElements) =>
      sender ! Result(calculatePiFor(start, nrOfElements))
  }

  //计算工作时间
  def calculatePiFor(start: Int, nrOfElements: Int): Double = {
    println("开始计算pi ,当前节点: start[%s]".format(start))
    var acc = 0.0
    for (i <- start until (start + nrOfElements))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
  }
}

//主机Actor
class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {
  //任务内容总数
  var pi: Double = _
  //已完成数
  var nrOfResults: Int = _
  //开始时间
  val start: Long = System.currentTimeMillis

  //设置路由池
  val workerRouter = context.actorOf(
    RoundRobinPool(nrOfWorkers).props(Props[Worker]), name = "workerRouter")

  def receive = {
    //执行计算任务 -> 分配任务给worker
    case Calculate =>
      for (i <- 0 until nrOfMessages) workerRouter ! Work(i * nrOfElements, nrOfElements)
    //执行汇总任务 -> 计算结果汇报给listener
    case Result(value) =>
      println("节点计算结束 ,当前值: pi[%s] ,value[%s]".format(pi, value))

      pi += value
      nrOfResults += 1
      //任务已完成
      if (nrOfResults == nrOfMessages) {
        listener ! PiApproximation(pi, duration = Duration(System.currentTimeMillis - start, TimeUnit.MICROSECONDS))
        context.stop(self)
      }
  }
}

//监听器
class Listener extends Actor {
  //接受消息 打印结果
  def receive = {
    case PiApproximation(pi, duration) ⇒
      println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s"
        .format(pi, duration))
      context.system.terminate()
  }
}

object Pi extends App {

  //启动任务管理器
  calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)

  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    //创建akka系统
    val system = ActorSystem("PiSystem")
    //创建观察者(因为要注入到主actor中)
    val listener = system.actorOf(Props[Listener], name = "listener")
    //创建主机(主机通过路由 创建从机)
    val master = system.actorOf(Props(new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener)), name = "master")

    //执行计算任务
    master ! Calculate
  }

  /**
    * 由结果可见 ,通过路由池(类似线程池) ,主机将消息有序的分配给各个工作器
    * 因为actor结构 ,采用消息的分发和收集实现并行 ,消息的接受是有序的 ,达成了同步
    * 各个Woker是交替开始和结束的 ,但只有汇总的时候Pi值才有序的进行变化
    *
    * akka是一个比较有效的并行同步工具 ,现在也已经推广到了Java中
    */
}