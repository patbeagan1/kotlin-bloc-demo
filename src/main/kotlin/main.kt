import bloc.Bloc
import bloc.BlocBinding
import dataaccess.FavoritesRepositoryImpl
import dataaccess.FruitRepositoryImpl
import logic.BlocFavorite
import logic.BlocFruit
import logic.BlocFruit.Action2.A
import logic.BlocFruit.Action2.B
import util.Failure
import util.Success
import util.bindBloc

fun main() {
    val r = FruitRepositoryImpl()
    val blocFruit = BlocFruit(r)
    val blocFruit2 = BlocFruit(r)
    val blocFavorite = BlocFavorite(FavoritesRepositoryImpl())

    listOf(
        blocFruit.out,
        blocFruit2.out,
        blocFavorite.out
    ).let { bind(it) }

    blocFruit.process(A)
    blocFruit.process(A)
    blocFruit2.process(B)
    blocFavorite.unfavorite(2)
    blocFavorite.favorite(5)
}

private fun <S : Bloc.BlocState> bind(listOf: BlocBinding<S>) {
    bindBloc(listOf).subscribe { result ->
        when (result) {
            is Failure -> println("Error!")
            is Success -> {
                when (val it = result.ok) {
                    is BlocFruit.State2 -> println(it.s)
                    is BlocFavorite.StateFavorites.StateFavoritesError -> println("Err" + it.s)
                    is BlocFavorite.StateFavorites.StateFavoritesSuccess -> println(it.id)
                }
            }
        }
    }
}