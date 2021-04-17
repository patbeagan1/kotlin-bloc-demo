package util

import io.reactivex.rxjava3.core.Observable

fun <T : Any> T.observable(): Observable<T> = Observable.just(this)

sealed class Result<out A, out B> {
    class Success<A>(val ok: A) : Result<A, Nothing>()
    class Failure<B>(val er: B) : Result<Nothing, B>()
}

sealed class Either<out A, out B> {
    class Left<A>(val ok: A) : Either<A, Nothing>()
    class Right<B>(val er: B) : Either<Nothing, B>()
}
