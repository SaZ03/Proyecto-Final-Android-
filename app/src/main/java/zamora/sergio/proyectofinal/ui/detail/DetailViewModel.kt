package zamora.sergio.proyectofinal.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zamora.sergio.proyectofinal.data.AnimeRepository
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.local.FavoriteAnime
import zamora.sergio.proyectofinal.data.remote.Anime
import zamora.sergio.proyectofinal.data.remote.AnimeImageJpg
import zamora.sergio.proyectofinal.data.remote.AnimeImages

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnimeRepository(AnimeDatabase.getInstance(application))

    private val _anime = MutableLiveData<Anime?>()
    val anime: LiveData<Anime?> = _anime

    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadAnime(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = repository.getAnimeDetail(id)
                _anime.value = response.data
                _isFavorite.value = repository.isFavorite(id)
            } catch (e: Exception) {
                val local = repository.getFavoriteById(id)
                if (local != null) {
                    _anime.value = Anime(
                        mal_id = local.id,
                        title = local.title,
                        images = AnimeImages(AnimeImageJpg(local.imageUrl)),
                        synopsis = local.synopsis,
                        score = local.score,
                        episodes = local.episodes,
                        year = null,
                        genres = null
                    )
                    _isFavorite.value = true
                    _error.value = "Sin conexión — mostrando datos guardados"
                } else {
                    _anime.value = null
                    _error.value = "No se pudo cargar el anime."
                }
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorite() {
        val current = _anime.value ?: return
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                repository.removeFavorite(current.mal_id)
                _isFavorite.value = false
            } else {
                repository.addFavorite(
                    FavoriteAnime(
                        id = current.mal_id,
                        title = current.title,
                        imageUrl = current.images.jpg.image_url ?: "",
                        synopsis = current.synopsis ?: "Sin sinopsis.",
                        score = current.score ?: 0.0,
                        episodes = current.episodes ?: 0
                    )
                )
                _isFavorite.value = true
            }
        }
    }
}
