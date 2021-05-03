package bloc

import bloc.Bloc.BlocError
import bloc.Bloc.BlocState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import util.EitherResult
import util.Success

abstract class FlowBloc<A, S : BlocState>(
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(coroutineDispatcher)
    private val _in: Channel<A> = Channel()
    private val _out: MutableSharedFlow<EitherResult<S, BlocError>> = MutableSharedFlow()
    val out: Flow<EitherResult<S, BlocError>> = _out.asSharedFlow()
    protected abstract suspend fun onEvent(event: A): S

    init {
        scope.launch {
            for (i in _in) {
                // todo catch the failure case with a try/catch
                _out.emit(Success(onEvent(i)))
            }
        }
    }

    fun process(action: A) = scope.launch { _in.send(action) }
    fun clear() = scope.cancel()
}
