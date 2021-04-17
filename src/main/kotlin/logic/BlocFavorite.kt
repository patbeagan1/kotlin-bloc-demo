package logic

import bloc.Bloc
import dataaccess.FavoritesRepository
import io.reactivex.rxjava3.core.Observable

class BlocFavorite(private val r: FavoritesRepository) : Bloc<BlocFavorite.ActionFavorites, BlocFavorite.StateFavorites>() {
    sealed class ActionFavorites {
        class ActionFavorite(val id: Int) : ActionFavorites()
        class ActionUnFavorite(val id: Int) : ActionFavorites()
    }

    sealed class StateFavorites : State {
        class StateFavoritesSuccess(val id: Int) : StateFavorites()
        class StateFavoritesError(val s: String) : StateFavorites()
    }

    override fun processInner(action: ActionFavorites): Observable<StateFavorites> = when (action) {
        is ActionFavorites.ActionFavorite -> r.favorite(action.id)
        is ActionFavorites.ActionUnFavorite -> r.unfavorite(action.id)
    }

    fun favorite(i: Int) = process(ActionFavorites.ActionFavorite(i))
    fun unfavorite(i: Int) = process(ActionFavorites.ActionUnFavorite(i))
}