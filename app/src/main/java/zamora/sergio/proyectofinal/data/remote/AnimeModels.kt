package zamora.sergio.proyectofinal.data.remote

data class AnimeSearchResponse(val data: List<Anime>)

data class AnimeDetailResponse(val data: Anime)

data class Anime(
    val mal_id: Int,
    val title: String,
    val images: AnimeImages,
    val synopsis: String?,
    val score: Double?,
    val episodes: Int?,
    val year: Int?,
    val genres: List<Genre>?
)

data class AnimeImages(val jpg: AnimeImageJpg)

data class AnimeImageJpg(val image_url: String?)

data class Genre(val name: String)
