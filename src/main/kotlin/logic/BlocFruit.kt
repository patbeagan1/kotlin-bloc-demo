package logic

import bloc.Bloc
import dataaccess.FruitRepository
import io.reactivex.rxjava3.core.Observable
import logic.BlocFruit.Action2.A
import logic.BlocFruit.Action2.B

class BlocFruit(private val fruitRepository: FruitRepository) : Bloc<BlocFruit.Action2, BlocFruit.State2>() {
    enum class Action2 { A, B }

    data class State2(var s: String) : State

    override fun processInner(action: Action2): Observable<State2> = when (action) {
        A -> fruitRepository.getApple().map { State2(it) }
        B -> fruitRepository.getBanana().map { State2(it) }
    }
}