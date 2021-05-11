package com.company

fun main(args: Array<String>) {

}


/* В данном случае смысл дженерика <out L, out R> в том, что по ссылке Either<Exception, Number> можем получить


 */
fun createCard( ){
    val eitherWithEx: Either<Exception, Number> = Either.Left(RuntimeException())
    val num: Int = 1
    val eitherWithNum: Either<Exception, Number> = Either.Right(Int.MAX_VALUE)
    val eitherWithStar: Either<*,*> = Either.Left(12)
    val right: Either.Right<Int> = Either.Right(23)
}

fun <A> identity(a: A) = a

infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C =
    { a: A -> this(f(a)) }

infix fun <A, B, C> ((A) -> B).andThen(g: (B) -> C): (A) -> C =
    { a: A -> g(this(a)) }
