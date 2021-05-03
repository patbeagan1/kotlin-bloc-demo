package dataaccess

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import logic.BlocFavorite
import util.observable
import kotlin.random.Random

interface FruitRepository : AppleRepository, BananaRepository {
    fun getCucumberFlow(): Flow<String>
}

class FruitRepositoryImpl : FruitRepository {
    override fun getCucumberFlow(): Flow<String> = flowOf("cucumber")
    override fun getApple(): Observable<String> = Observable.just("apple")
    override fun getAppleFlow(): Flow<String> = flowOf("flow apple")
    override fun getBanana(): Observable<String> = Observable.just("banana")
    override fun getBananaFlow(): Flow<String> = flowOf("flow banana")
}

interface AppleRepository {
    fun getApple(): Observable<String>
    fun getAppleFlow(): Flow<String>
}

interface BananaRepository {
    fun getBanana(): Observable<String>
    fun getBananaFlow(): Flow<String>
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