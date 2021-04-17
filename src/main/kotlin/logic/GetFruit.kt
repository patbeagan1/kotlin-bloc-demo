package logic

import bloc.Bloc
import dataaccess.FruitRepository
import io.reactivex.rxjava3.core.Observable
import logic.GetFruit.Action2.A
import logic.GetFruit.Action2.B

class GetFruit(private val fruitRepository: FruitRepository) : Bloc<GetFruit.Action2, GetFruit.State2>() {
    enum class Action2 {
        A, B
    }

    data class State2(var s: String) : State

    override fun processInner(action: Action2): Observable<State2> = when (action) {
        A -> fruitRepository.getApple().map { State2(it) }
        B -> fruitRepository.getBanana().map { State2(it) }
    }
}