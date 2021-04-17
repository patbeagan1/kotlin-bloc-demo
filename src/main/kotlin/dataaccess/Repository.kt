package dataaccess

import io.reactivex.rxjava3.core.Observable
import logic.BlocFavorite
import util.observable
import kotlin.random.Random

interface FruitRepository : AppleRepository, BananaRepository
class FruitRepositoryImpl : FruitRepository {
    override fun getApple(): Observable<String> = Observable.just("apple")
    override fun getBanana(): Observable<String> = Observable.just("banana")
}

interface AppleRepository {
    fun getApple(): Observable<String>
}

interface BananaRepository {
    fun getBanana(): Observable<String>
}

class FavoritesRepositoryImpl : FavoritesRepository {
    override fun favorite(id: Int): Observable<BlocFavorite.StateFavorites> =
        BlocFavorite.StateFavorites.StateFavoritesSuccess(id).observable()

    override fun unfavorite(id: Int): Observable<BlocFavorite.StateFavorites> {
        // simulating a bad connection
        val nextInt = Random.nextInt()
        return if (nextInt % 2 == 0) {
            println(nextInt)
            BlocFavorite.StateFavorites.StateFavoritesError("issue").observable()
        } else {
            BlocFavorite.StateFavorites.StateFavoritesSuccess(id).observable()
        }
    }
}

interface FavoritesRepository {
    fun favorite(id: Int): Observable<BlocFavorite.StateFavorites>
    fun unfavorite(id: Int): Observable<BlocFavorite.StateFavorites>
}