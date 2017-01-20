package SecondWithProgrammingInScala.Chapter27

import SecondWithProgrammingInScala.Chapter27.SimpleDatabase.FoodCategory

/**
  * 适用对象的模块化编程
  *
  * 即对程序合理的分层和组织 ,将功能划分为一个个模块 ,使之易于修改和替换 ,而对其他模块没有影响
  * 就像Java中使用接口衔接程序 ,通过替换实现方法修改底层而不影响使用
  * 加上Spring的依赖注入 ,可以很好的分离依赖关系
  *
  * Scala也可以使用Spring ,也提供了自己的备选方案
  * abstract class + trait + 组合 ,将底层实现隐藏 ,模块间通过父类/特质进行交互
  */

//需要持久化的实例类
abstract class Food(val name: String) {
  override def toString: String = name
}

class Recipe(val name: String, val ingredients: List[Food], val instructions: String) {
  override def toString: String = name
}

//额外功能抽象
trait FoodCategories {

  case class FoodCategory(name: String, foods: List[Food])

  def allCategories: List[FoodCategory]
}

//抽象数据库接口
abstract class Database extends FoodCategories {
  def allFoods: List[Food]

  def allRecipes: List[Recipe]

  def foodNamed(name: String): Option[Food] = allFoods.find(_.name == name)
}

//浏览器抽象
abstract class Browser {
  val database: Database

  def recipeUsing(food: Food) = database.allRecipes.filter(recipe => recipe.ingredients.contains(food))

  def displayCategory(category: database.FoodCategory) = println(category)

}

//实现扩展数据库类型
trait SimpleFoods {

  object Apple extends Food("Apple")

  object Orange extends Food("Orange")

  object Cream extends Food("Cream")

  object Sugar extends Food("Sugar")

  def allFoods = List(Apple, Orange, Cream, Sugar)
}

trait SimpleRecipes {
  this: SimpleFoods =>

  object FruitSalad extends Recipe("Fruit Salad", List(Apple, Orange, Cream, Sugar), "Stir it all together")

  def allRecipes: List[Recipe] = List(FruitSalad)
}

object SimpleDatabase extends Database with SimpleFoods with SimpleRecipes {

  private var categories = List(
    FoodCategory("fruit", List(Apple, Orange)),
    FoodCategory("misc", List(Cream, Sugar))
  )

  def allCategories = categories
}

object StudentDatabase extends Database {

  object FrozenFood extends Food("FrozenFood")

  object HeadItUp extends Recipe("Heat it up", List(FrozenFood), "Microwave the 'food' for 10 minutes")

  override def allFoods = List(FrozenFood)

  override def allRecipes = List(HeadItUp)

  override def allCategories = List(FoodCategory("edible", List(FrozenFood)))
}


//浏览器入口
object SimpleBrower extends Browser {
  val database = SimpleDatabase
}

object StudentBrower extends Browser {
  val database = StudentDatabase
}

