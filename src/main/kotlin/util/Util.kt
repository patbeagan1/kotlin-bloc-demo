package util

import io.reactivex.rxjava3.core.Observable

fun <T : Any> T.observable(): Observable<T> = Observable.just(this)
fun Any?.print() = println(this)

sealed class EitherResult<out A, out B> {
    companion object {
        fun <A> success(a: A) = Success(a)
        fun <A> failure(a: A) = Failure(a)
    }
}

class Success<A>(val ok: A) : EitherResult<A, Nothing>()
class Failure<B>(val er: B) : EitherResult<Nothing, B>()

sealed class Either<out A, out B> {
    class Left<A>(val value: A) : Either<A, Nothing>()
    class Right<B>(val value: B) : Either<Nothing, B>()
}