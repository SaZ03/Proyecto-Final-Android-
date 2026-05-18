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
    val genres: List<Genre>?,
    val status: String?,
    val broadcast: Broadcast?
)

data class AnimeImages(val jpg: AnimeImageJpg)

data class AnimeImageJpg(val image_url: String?)

data class Genre(val name: String)

data class Broadcast(val string: String?)

data class EpisodesResponse(val data: List<EpisodeItem>)

data class EpisodeItem(
    val mal_id: Int,
    val title: String?,
    val aired: String?
)
