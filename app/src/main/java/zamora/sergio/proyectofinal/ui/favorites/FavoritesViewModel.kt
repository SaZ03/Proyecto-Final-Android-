package zamora.sergio.proyectofinal.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zamora.sergio.proyectofinal.data.AnimeRepository
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.local.FavoriteAnime

enum class SortOrder { TITLE, SCORE, DATE }

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnimeRepository(AnimeDatabase.getInstance(application))

    val sortOrder = MutableLiveData(SortOrder.TITLE)

    val favorites: LiveData<List<FavoriteAnime>> = sortOrder.switchMap { order ->
        when (order) {
            SortOrder.TITLE -> repository.getFavoritesByTitle()
            SortOrder.SCORE -> repository.getFavoritesByScore()
            SortOrder.DATE  -> repository.getFavoritesByDate()
        }
    }

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            repository.removeFavorite(id)
        }
    }
}
