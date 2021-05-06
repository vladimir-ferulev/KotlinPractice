package com.company

typealias MyHandler = (Int, String) -> Unit
typealias Dispatch = (action: Action) -> Unit

fun main(args: Array<String>) {
//    var dog: Dog? = null
//    val ageNull = dog?.apply(fun Dog.() {
//        this.age
//    })   // let не выполнится, так как dog == null. В переменную установится null
//    println(ageNull)
//    dog = Dog(10, "Name")
//    var l = dog.apply(fun Dog.() {
//        this.age = 5
//    })       // it - это сам объект dog. В age будет устанволен результат выполнения переданной функции
//    println(l)
//
//    with (dog, fun Dog.() {
//        this.age = 5
//    })

//    var list = listOf(1, 4, 1, 5)
//    listOfNotNull(list.isDistinct().takeUnless(fun(it: Boolean): Boolean {
//        return it
//    })?.run { println(this)})
//

//        .run(fun Boolean.(): Any {
//        return lang("MtpL3NiValidationUnique")
//    })) +
//            nis.map { MtpL3ValidationNi(it.toString(), lang) }.flatten()

}

fun <T> List<T>.isDistinct(): Boolean =
    size == distinct().size

data class Dog(var age: Int, var name: String)

class InnerDog(val num: Int)

fun call42(num: Int, name: String, func: (Int) -> String) = func(42)

fun function(vararg name: String) {
    println(name)
}

fun meth(func: () -> Unit): Int {
    func()
    return 1
}

fun dis(disp: Dispatch) {

}

class Action {

}


// там, где в качестве параметра требуется функция, можно передать анонимную функцию fun(str: String) {...} - без названия,
// главное, чтобы подходила сигнатура
// также можно передать ссылку на функцию, если она уже где то написана и подходит по сигнатуре
// либо пишем лямбду. Тогда можем опускать круглые скобки

// meth(fun(str: String): Int = str.length)
//   <=>
// meth { str -> str.length }      - без круглых скобок
// meth { it.length }            - если параметр один, то можем не использовать конструкцию "str ->"  и просто использовать it
// meth(String::length)         - если есть подходящий метод или поле с соответствующей сигнатурой

// meth { str -> anon(str) }
// <=>
// meth(::anon)     - ссылка на функцию. Значит, что функция anon подходит по сигнатуре и уже содержит необходимую реализацию
// мы передаем функцию, которая принимает определенные параметры и возвращает определенный тип данных. На этапе вызова функции
// нужно передать просто реализацию функции. В Java нужно было передать объект класса, который имплементирует функциональный
// интерфейс. Здесь же не нужно передавать объект, а сразу передаем функцию, которую можно использовать по названию переменной
//
// Если помимо передачи функции есть передача простого аргумента
// meth (1, {str -> str.length} )
// <=>
// meth (1) {str -> str.length}
// meth (1, {str -> str.length}, 2)  - нельзя написать по-другому
// meth ({str -> str.length}, 2) - тоже только такая запись
//
// Если в лямбда-выражении нужно выполнить несколько инструкции, то мы их не оборачиваем в фигурные скобки, как это делали в Java
// meth {str ->
//    println("Hello")
//    str.length            - или return@meth str.length
// }
// Последнее выражение должно возвращать данные необходимого типа


// в связи с тем, что в отличии от Java в Kotlin функция может существовать отдельно от класса, она также может содержаться
// в переменной


