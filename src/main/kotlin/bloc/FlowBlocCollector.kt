package bloc

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import util.EitherResult
import util.Scope

class FlowBlocCollector<A, S : Bloc.BlocState>(
    private vararg val blocs: FlowBloc<A, S>,
    private val scope: CoroutineScope = Scope.default(),
    onState: suspend (EitherResult<S, Bloc.BlocError>) -> Unit
) {
    init {
        blocs.forEach { flow ->
            scope.launch {
                flow.out.collect(onState)
            }
        }
    }

    fun clear() {
        scope.cancel()
        blocs.forEach { it.clear() }
    }
}