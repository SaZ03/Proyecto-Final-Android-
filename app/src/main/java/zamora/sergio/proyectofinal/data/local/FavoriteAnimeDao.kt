package zamora.sergio.proyectofinal.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteAnimeDao {

    @Query("SELECT * FROM favorites ORDER BY title ASC")
    fun getAll(): LiveData<List<FavoriteAnime>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getById(id: Int): FavoriteAnime?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anime: FavoriteAnime)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteById(id: Int)
}
