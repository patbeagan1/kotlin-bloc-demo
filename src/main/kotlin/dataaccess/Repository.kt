package dataaccess

import io.reactivex.rxjava3.core.Observable
import logic.Favorite
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
    override fun favorite(id: Int): Observable<Favorite.StateFavorites> =
        Favorite.StateFavorites.StateFavoritesSuccess(id).observable()

    override fun unfavorite(id: Int): Observable<Favorite.StateFavorites> {
        // simulating a bad connection
        val nextInt = Random.nextInt()
        return if (nextInt % 2 == 0) {
            println(nextInt)
            Favorite.StateFavorites.StateFavoritesError("issue").observable()
        } else {
            Favorite.StateFavorites.StateFavoritesSuccess(id).observable()
        }
    }
}

interface FavoritesRepository {
    fun favorite(id: Int): Observable<Favorite.StateFavorites>
    fun unfavorite(id: Int): Observable<Favorite.StateFavorites>
}