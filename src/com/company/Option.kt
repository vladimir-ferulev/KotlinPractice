package com.company

@Suppress("UNCHECKED_CAST")
sealed class Option<out A> {

    object None : Option<Nothing>()
    class Some<out T>(val value: T) : Option<T>()

    inline fun <NA> fold(fn: () -> NA, fs: (A) -> NA): NA = when (this) {
        is None -> fn()
        is Some<A> -> fs(value)
    }

    inline fun <NA> flatMap(fs: (A) -> Option<NA>): Option<NA> = when (this) {
        is None -> this
        is Some -> fs(value)
    }

    inline fun <NA> map(f: (A) -> NA): Option<NA> =
        flatMap { Some(f(it)) }

    inline fun <A> orElse(default: A): A =
        fold({ default }, { it as A })

    inline fun <A> orElseGet(default: () -> A): A =
        fold({ default() }, { it as A })

    inline fun orNull(): A? =
        fold({ null }, ::identity)

    inline fun isEmpty(): Boolean =
        fold({ true }, { false })

    inline fun nonEmpty(): Boolean =
        fold({ false }, { true })

    inline fun exists(predicate: (A) -> Boolean): Boolean =
        fold({ false }, predicate)

    inline fun <L> toEither(fl: () -> L): Either<L, A> =
        fold({ Either.Left(fl()) }, { Either.Right(it) })

    inline fun toList(): List<A> =
        fold({ emptyList() }, { listOf(it) })

    override fun equals(other: Any?): Boolean =
        when (this) {
            is None -> other is None
            is Some -> other is Some<*> && value == other.value
        }

    override fun hashCode(): Int =
        when (this) {
            is None -> this::class.java.hashCode()
            is Some -> this.value.hashCode()
        }

    override fun toString(): String =
        when (this) {
            is None -> "None()"
            is Some -> "Some(${value})"
        }

    companion object {

        operator fun <A> invoke(a: A): Option<A> = just(a)

        fun <A> just(a: A): Option<A> = Some(a)

        fun <A> empty(): Option<A> = None

        fun <A> fromNullable(a: A?): Option<A> = if (a != null) Some(a) else None
    }
}

fun <A> A.some(): Option<A> = Option.Some(this)

fun <A> none(): Option<A> = Option.None

fun <T> T?.toOption(): Option<T> = this?.let { Option.Some(it) } ?: Option.None

fun <A> Boolean.maybe(f: () -> A): Option<A> =
    if (this) Option.Some(f()) else Option.None

fun <T> Iterable<T>.firstOrNone(): Option<T> = this.firstOrNull().toOption()

fun <T> Iterable<T>.firstOrNone(predicate: (T) -> Boolean): Option<T> = this.firstOrNull(predicate).toOption()

fun <T> Iterable<T>.singleOrNone(): Option<T> = this.singleOrNull().toOption()

fun <T> Iterable<T>.singleOrNone(predicate: (T) -> Boolean): Option<T> = this.singleOrNull(predicate).toOption()

fun <T> Iterable<T>.lastOrNone(): Option<T> = this.lastOrNull().toOption()

fun <T> Iterable<T>.lastOrNone(predicate: (T) -> Boolean): Option<T> = this.lastOrNull(predicate).toOption()

fun <T> Iterable<T>.elementAtOrNone(index: Int): Option<T> = this.elementAtOrNull(index).toOption()
