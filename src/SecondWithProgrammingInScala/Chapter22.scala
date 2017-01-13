package SecondWithProgrammingInScala

/**
  * List的实现
  * 默认List -> scala.collection.immutable.List
  *
  * 本章解析了List的源码 ,在源码的内部使用了var while 循环 ,而非全部的val 递归
  * 因为循环的效率是大于等于递归的
  * scala的"函数化"编程其实是  '通过合理的封装,使得开发流程是函数化的' ,这样会使得业务代码非常清晰
  *
  * 而外部(相对于List等工具类的源码来说)使用val
  * 提高重复使用率(指向内存堆中同一个数据结构)的同时 ,保证数据不会轻易改变(共享状态下会影响多处)
  * "设计者同样希望维持纯粹.不可变的数据结构 ;也都希望能够高效.渐进式的构造这个数据结构"
  */

