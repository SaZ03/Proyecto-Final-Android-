package zamora.sergio.proyectofinal.data

import androidx.lifecycle.LiveData
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.local.FavoriteAnime
import zamora.sergio.proyectofinal.data.local.WatchedEpisode
import zamora.sergio.proyectofinal.data.remote.AnimeDetailResponse
import zamora.sergio.proyectofinal.data.remote.AnimeSearchResponse
import zamora.sergio.proyectofinal.data.remote.EpisodeItem
import zamora.sergio.proyectofinal.data.remote.RetrofitClient

class AnimeRepository(private val db: AnimeDatabase) {

    // --- Favorites ---
    fun getFavoritesByTitle(): LiveData<List<FavoriteAnime>> = db.favoriteAnimeDao().getAllByTitle()
    fun getFavoritesByScore(): LiveData<List<FavoriteAnime>> = db.favoriteAnimeDao().getAllByScore()
    fun getFavoritesByDate(): LiveData<List<FavoriteAnime>> = db.favoriteAnimeDao().getAllByDate()

    suspend fun isFavorite(id: Int): Boolean = db.favoriteAnimeDao().getById(id) != null

    suspend fun getFavoriteById(id: Int): FavoriteAnime? = db.favoriteAnimeDao().getById(id)

    suspend fun addFavorite(anime: FavoriteAnime) = db.favoriteAnimeDao().insert(anime)

    suspend fun removeFavorite(id: Int) = db.favoriteAnimeDao().deleteById(id)

    // --- Remote ---
    suspend fun searchAnime(query: String): AnimeSearchResponse =
        RetrofitClient.apiService.searchAnime(query)

    suspend fun getAnimeDetail(id: Int): AnimeDetailResponse =
        RetrofitClient.apiService.getAnimeDetail(id)

    suspend fun getEpisodes(animeId: Int): List<EpisodeItem> =
        RetrofitClient.apiService.getAnimeEpisodes(animeId).data

    // --- Watched episodes ---
    fun getWatchedNumbers(animeId: Int): LiveData<List<Int>> =
        db.watchedEpisodeDao().getWatchedNumbers(animeId)

    suspend fun markWatchedUpTo(animeId: Int, episodeNumber: Int) {
        val list = (1..episodeNumber).map { WatchedEpisode(animeId, it) }
        db.watchedEpisodeDao().insertAll(list)
    }

    suspend fun unmarkWatched(animeId: Int, episodeNumber: Int) =
        db.watchedEpisodeDao().delete(animeId, episodeNumber)
}
