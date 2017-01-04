package SecondWithProgrammingInScala

/**
  * 断言和单元测试
  * 基本的使用方法
  */
object Basic {
  def main(args: Array[String]): Unit = {
    //assert语句
    val a = 1
    val b = 2
    //断言失败会抛出java.lang.AssertionError: assertion failed
    //assert(a>b)
    assert(a < b)

    //ensuring函数 可适用于各种类型 ,使用了隐式转换 ,通过传入一个判断函数来检查
    if (a > b) {
      Array("Yes", "a<b")
    } else {
      Array("No")
    } ensuring (_.length > 1)
  }
}

/**
  * 使用Junit
  */

import junit.framework.TestCase
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail

class JunitTest extends TestCase {
  def testA(): Unit = {
    assertEquals(1, 1)
    try {
      1 / 0
      fail()
    } catch {
      case e: Exception => println(e)
    }
  }
}

/**
  * 使用较新的ScalaTest 3.01( scala版本要对应 )
  * 这里只简单的应用
  */

import org.scalatest.FunSuite

class SetFuncSuite extends FunSuite {
  //差集
  test("Test difference") {
    val a = Set("a", "b", "a", "c")
    val b = Set("b", "d")
    assert(a -- b === Set("a", "c"))
  }

  //交集
  test("Test intersection") {
    val a = Set("a", "b", "a", "c")
    val b = Set("b", "d")
    assert(a.intersect(b) === Set("b"))
  }

  //并集
  test("Test union") {
    val a = Set("a", "b", "a", "c")
    val b = Set("b", "d")
    assert(a ++ b === Set("a", "b", "c", "d"))
  }
}

/**
  * [ScalaTest使用](http://www.scalatest.org/quick_start)
  * 行为驱动开发(BDD behavior-driver development)的测试风格中
  * 使得测试代码趋于语言式
  */

import collection.mutable.Stack
import org.scalatest._

class ExampleSpec extends FlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be(2)
    stack.pop() should be(1)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a[NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}

/**
  * ScalaTest提供了较为详尽的测试方法 ,还有不同风格(类似Spec)的测试方法
  * 需要单独去理解学习
  */


