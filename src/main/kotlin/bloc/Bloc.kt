package bloc

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class Bloc<A, S : Bloc.BlocState> {
    private val _out: PublishSubject<S> = PublishSubject.create()
    val out: Observable<S> = _out.hide()
    private val compositeDisposable = CompositeDisposable()
    protected abstract fun processInner(action: A): Observable<S>
    fun process(action: A) {
        processInner(action).subscribe(
            {
                _out.onNext(it)
            }, {
                BlocError(it)
            }).addTo(compositeDisposable)
    }

    fun clear() {
        compositeDisposable.clear()
        _out.onComplete()
    }

    interface BlocState
    class BlocError(t: Throwable)
}

