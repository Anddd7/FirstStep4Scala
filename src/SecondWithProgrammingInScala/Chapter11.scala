package SecondWithProgrammingInScala

/*
#scala类型结构

- **Any**
- 公共基类 ,定义了基本的equals(==/!=的实现)/##(hashCode)/toString等方法

- **AnyVal**
- 所有内建类型的父类 ,Byte,Short,Char,Int,Long,Float,Double,Boolean(对应JAVA基本类型),Unit(void)
类型间使用隐式转换 ,用implicit声明一个转换函数并import到目标点 ,遇到类型不匹配会调用相关的转换函数

- **AnyRef**
- 所有引用类型的基类 ,类似于Java中的java.lang.Object

- **Null Nothing**
- 底层类型
- Null表示null引用(AnyRef的子类)
- Nothing是Null的子类 ,没有任何实例 ,通常在程序的非正常中止时作为"无"的返回值

```
def divide(x:Int,y:Int):Int=
if(y!=0) x/y
else error("Cannot divide by Zero")

def error(message:String) :Nothing =
throw new RuntimeException(message)
```

如果在Java中你需要在错误时候返回一个默认结果 ,因为函数需要返回Int
这里则可以直接返回Nothing(子类) ,并由外界函数检查
*/
