package util

import bloc.Bloc
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> T.observable(): Observable<T> = Observable.just(this)

sealed class EitherResult<out A, out B>
class Success<A>(val ok: A) : EitherResult<A, Nothing>()
class Failure<B>(val er: B) : EitherResult<Nothing, B>()

sealed class Either<out A, out B> {
    class Left<A>(val ok: A) : Either<A, Nothing>()
    class Right<B>(val er: B) : Either<Nothing, B>()
}

fun <S : Bloc.BlocState> bindBloc(
    list: List<Observable<out EitherResult<S, Bloc.BlocError>>>
): Observable<EitherResult<S, Bloc.BlocError>> =
    Observable.merge(list).observeOn(Schedulers.computation())

