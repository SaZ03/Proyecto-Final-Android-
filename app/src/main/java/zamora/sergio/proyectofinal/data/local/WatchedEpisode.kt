package zamora.sergio.proyectofinal.data.local

import androidx.room.Entity

@Entity(tableName = "watched_episodes", primaryKeys = ["animeId", "episodeNumber"])
data class WatchedEpisode(
    val animeId: Int,
    val episodeNumber: Int
)
