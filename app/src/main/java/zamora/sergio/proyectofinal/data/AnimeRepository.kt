package zamora.sergio.proyectofinal.data

import androidx.lifecycle.LiveData
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.local.FavoriteAnime
import zamora.sergio.proyectofinal.data.remote.AnimeDetailResponse
import zamora.sergio.proyectofinal.data.remote.AnimeSearchResponse
import zamora.sergio.proyectofinal.data.remote.RetrofitClient

class AnimeRepository(private val db: AnimeDatabase) {

    fun getFavorites(): LiveData<List<FavoriteAnime>> = db.favoriteAnimeDao().getAll()

    suspend fun isFavorite(id: Int): Boolean = db.favoriteAnimeDao().getById(id) != null

    suspend fun addFavorite(anime: FavoriteAnime) = db.favoriteAnimeDao().insert(anime)

    suspend fun removeFavorite(id: Int) = db.favoriteAnimeDao().deleteById(id)

    suspend fun searchAnime(query: String): AnimeSearchResponse =
        RetrofitClient.apiService.searchAnime(query)

    suspend fun getAnimeDetail(id: Int): AnimeDetailResponse =
        RetrofitClient.apiService.getAnimeDetail(id)
}
