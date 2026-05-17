package zamora.sergio.proyectofinal.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteAnime(
    @PrimaryKey val id: Int,
    val title: String,
    val imageUrl: String,
    val synopsis: String,
    val score: Double,
    val episodes: Int
)
