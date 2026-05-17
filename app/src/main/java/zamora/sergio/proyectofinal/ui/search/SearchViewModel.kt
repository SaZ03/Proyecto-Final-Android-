package zamora.sergio.proyectofinal.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zamora.sergio.proyectofinal.data.AnimeRepository
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.remote.Anime

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AnimeRepository(AnimeDatabase.getInstance(application))

    private val _results = MutableLiveData<List<Anime>>()
    val results: LiveData<List<Anime>> = _results

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun search(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = repository.searchAnime(query)
                _results.value = response.data
            } catch (e: Exception) {
                _error.value = "No se pudo conectar. Revisa tu internet."
                _results.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
