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
}

sealed class Either<out L, out R> {

    class Left<L>(val value: L) : Either<L, Nothing>()
    class Right<R>(val value: R) : Either<Nothing, R>()

    inline fun <T> fold(fl: (L) -> T, fr: (R) -> T): T = when (this) {
        is Left -> fl(this.value)
        is Right -> fr(this.value)
    }

    override fun equals(other: Any?): Boolean =
        when (this) {
            is Left -> other is Left<*> && value == other.value
            is Right -> other is Right<*> && value == other.value
        }

    override fun hashCode(): Int =
        when (this) {
            is Left -> this.value.hashCode()
            is Right -> this.value.hashCode()
        }

    override fun toString(): String =
        when (this) {
            is Left -> "Left(${value})"
            is Right -> "Right(${value})"
        }

    companion object {

        inline fun <R> catch(fr: () -> R): Either<Throwable, R> =
            catch(::identity, fr)

        suspend inline fun <R> catchSuspend(fr: () -> R): Either<Throwable, R> =
            catchSuspend(::identity, fr)

        inline fun <L, R> catch(fl: (Throwable) -> L, fr: () -> R): Either<L, R> =
            try {
                Right(fr())
            } catch (throwable: Throwable) {
                Left(fl(throwable))
            }

        suspend inline fun <L, R> catchSuspend(fl: (Throwable) -> L, fr: () -> R): Either<L, R> =
            try {
                Right(fr())
            } catch (throwable: Throwable) {
                Left(fl(throwable))
            }
    }
}

fun <A> identity(a: A) = a

infix fun <A, B, C> ((B) -> C).compose(f: (A) -> B): (A) -> C =
    { a: A -> this(f(a)) }

infix fun <A, B, C> ((A) -> B).andThen(g: (B) -> C): (A) -> C =
    { a: A -> g(this(a)) }