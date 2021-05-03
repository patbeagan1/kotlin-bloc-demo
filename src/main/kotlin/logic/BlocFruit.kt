package logic

import bloc.Bloc
import bloc.FlowBloc
import dataaccess.FruitRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import logic.BlocFruit.ActionObservable.A
import logic.BlocFruit.ActionObservable.B

class BlocFruit(private val fruitRepository: FruitRepository) : Bloc<BlocFruit.ActionObservable, BlocFruit.StateObservable>() {
    enum class ActionObservable { A, B }

    data class StateObservable(var s: String) : State

    override fun processInner(action: ActionObservable): Observable<StateObservable> = when (action) {
        A -> fruitRepository.getApple().map { StateObservable(it) }
        B -> fruitRepository.getBanana().map { StateObservable(it) }
    }
}

class BlocFruitFlow(private val fruitRepository: FruitRepository, coroutineDispatcher: CoroutineDispatcher) :
    FlowBloc<BlocFruitFlow.Action, BlocFruitFlow.State>(coroutineDispatcher) {
    sealed class Action {
        object A : Action()
        object B : Action()
        class C(val s: String) : Action()
    }

    data class State(var s: String) : logic.State

    override suspend fun onEvent(event: Action): State = when (event) {
        Action.A -> fruitRepository.getAppleFlow().map { State(it) }.first()
        Action.B -> fruitRepository.getBananaFlow().map { State(it) }.first()
        is Action.C -> fruitRepository.getCucumberFlow().map { State(event.s + it) }.first()
    }
}