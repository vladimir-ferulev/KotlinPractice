package com.company

// sealed - нельзя создать экземпляр данного класса, кроме создания подклассов внутри sealed класса
// out L, out R - значит можно сделать так Either<Exception, Number> = Either.Right(Int.MAX_VALUE)
sealed class Either<out L, out R> {

    // внутренние классы, которые наследуются от внешнего класса и параметризируются в заивисимости от перданного значения
    // сами ничего не содержат, кроме value, все остальное - класс Either. параметризация происходит от уже известного типа в
    // родителе <Nothing, R> и R - это тип переданного значения value
    class Left<L>(val value: L) : Either<L, Nothing>()
    class Right<R>(val value: R) : Either<Nothing, R>()

    // inline - тело функции будет встроено в месте вызова. Не будет создан дополнительный объект передаваемых функций
    // fold - функция высшего порядка, так как принимает в качестве параметров функции
    // передаются две полноценные функции для случаев успеха/неудачи или других состояний, но вызывается только одна
    // в зависимости от того у какого типа вызываем Left или Right
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

    // companion object - это вроде статических членов в Java. По имени класса Either можно вызывать данные функции
    companion object {

        // принимает функцию, которая ничего не принимает, но возвращает тип Right (успех)
        // вызываем catch и передаем функцию, которая возвращает то же самое значение что и приняла
        // и функцию, которая ничего не принимает и возвращает объект типа при успехе
        inline fun <R> catch(fr: () -> R): Either<Throwable, R> =
            catch(::identity, fr)

        // то же самое, только suspend
        suspend inline fun <R> catchSuspend(fr: () -> R): Either<Throwable, R> =
            catchSuspend(::identity, fr)

        // пробуем выполнить функцию fr(), если получается то возвращаем Right(value), если нет и выбрасывается исключение
        // то возвращаем Left(exception), exception передали в функции fl
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

// функция расширения класса Either. Параметризуется тремя типами, которые берутся из аргументов
/*
Either<HttpException, List<IsupChannel>> = Either<HttpException, InfoApiResponse>.map { response -> response.data
                                            .filterIsInstance<IsupChannelEntity>()
                                            .map { it.toStruct() }
                                            }


Either<Throw, upChannel>.flatMap { isupChannels ->
            when (batch) {
                false -> isupChannels.right()
                else -> getCardTrunkHdlcsForIsupChannels(endpoint, isupChannels)
                        .map { isupChannels + it }
            }
        }
*/

inline fun <L, R, NR> Either<L, R>.flatMap(fr: (R) -> Either<L, NR>): Either<L, NR> =
    flatMapRight(fr)

inline fun <L, R, NR> Either<L, R>.flatMapRight(fr: (R) -> Either<L, NR>): Either<L, NR> = when (this) {
    is Either.Left -> this
    is Either.Right -> fr(this.value)
}

inline fun <L, R, NL> Either<L, R>.flatMapLeft(fl: (L) -> Either<NL, R>): Either<NL, R> = when (this) {
    is Either.Left -> fl(this.value)
    is Either.Right -> this
}

/*
Преобразовать правую часть Either, передаем функцию преобразвания
Either<HttpException, List<IsupChannel>> = Either<HttpException, InfoApiResponse>.map { response -> response.data
    .filterIsInstance<IsupChannelEntity>()
            .map { it.toStruct() }
}
*/
inline fun <L, R, NR> Either<L, R>.map(fr: (R) -> NR): Either<L, NR> =
    mapRight(fr)

inline fun <L, R, NR> Either<L, R>.mapRight(fr: (R) -> NR): Either<L, NR> =
    fold({ Either.Left(it) }, { Either.Right(fr(it)) })

inline fun <L, R, NL> Either<L, R>.mapLeft(fl: (L) -> NL): Either<NL, R> =
    fold({ Either.Left(fl(it)) }, { Either.Right(it) })

inline fun <L, R, NL, NR> Either<L, R>.bimap(fl: (L) -> NL, fr: (R) -> NR): Either<NL, NR> =
    fold({ Either.Left(fl(it)) }, { Either.Right(fr(it)) })

inline fun <L, R> Either<L, R>.getOrElse(default: R): R =
    fold({ default }, ::identity)

inline fun <L, R> Either<L, R>.isRight(): Boolean =
    fold({ false }, { true })

inline fun <L, R> Either<L, R>.exists(predicate: (R) -> Boolean): Boolean =
    fold({ false }, predicate)

inline fun <L, R> Either<L, R>.toOption(): Option<R> =
    fold({ Option.empty() }, { Option.just(it) })

fun <A> A.left(): Either<A, Nothing> =
    Either.Left(this)

fun <A> A.right(): Either<Nothing, A> =
    Either.Right(this)
