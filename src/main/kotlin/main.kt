import logic.GetFruit.Action2.A
import logic.GetFruit.Action2.B
import dataaccess.FavoritesRepositoryImpl
import dataaccess.FruitRepositoryImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import logic.Favorite
import logic.GetFruit

fun main() {
    val r = FruitRepositoryImpl()
    val dbloc = GetFruit(r)
    val ab = GetFruit(r)
    val f = Favorite(FavoritesRepositoryImpl())
    Observable.merge(listOf(dbloc.out, ab.out, f.out))
        .observeOn(Schedulers.computation()).subscribe {
            when (it) {
                is GetFruit.State2 -> println(it.s)
                is Favorite.StateFavorites.StateFavoritesError -> println("Err" + it.s)
                is Favorite.StateFavorites.StateFavoritesSuccess -> println(it.id)
            }
        }

    dbloc.process(A)
    dbloc.process(A)
    ab.process(B)
    f.unfavorite(2)
    f.favorite(5)
}
