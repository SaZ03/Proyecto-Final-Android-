package zamora.sergio.proyectofinal.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20
    ): AnimeSearchResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(@Path("id") id: Int): AnimeDetailResponse

    @GET("anime/{id}/episodes")
    suspend fun getAnimeEpisodes(
        @Path("id") id: Int,
        @Query("page") page: Int = 1
    ): EpisodesResponse
}
