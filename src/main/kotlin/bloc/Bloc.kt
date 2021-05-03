package bloc

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import util.EitherResult
import util.Failure
import util.Success
import util.observable

abstract class Bloc<A, S : Bloc.BlocState> {
    private val _out: PublishSubject<EitherResult<S, BlocError>> = PublishSubject.create()
    val out: Observable<EitherResult<S, BlocError>> = _out.hide()
    private val compositeDisposable = CompositeDisposable()
    protected abstract fun processInner(action: A): Observable<S>
    fun process(action: A) {
        processInner(action).subscribe(
            {
                _out.onNext(Success(it))
            }, {
                _out.onErrorResumeWith(Failure(BlocError(it)).observable())
            }).addTo(compositeDisposable)
    }

    fun clear() {
        compositeDisposable.clear()
        _out.onComplete()
    }

    interface BlocState
    class BlocError(t: Throwable) : BlocState
}

fun collector(
    vararg listOf: Bloc<*, *>,
    scheduler: Scheduler = Schedulers.computation(),
    onEvent: (t: EitherResult<*, Bloc.BlocError>) -> Unit,
) {
    Observable
        .merge(listOf.map { it.out })
        .observeOn(scheduler)
        .subscribe(onEvent)
}

