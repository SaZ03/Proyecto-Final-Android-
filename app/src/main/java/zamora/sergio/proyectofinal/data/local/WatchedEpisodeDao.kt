package zamora.sergio.proyectofinal.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WatchedEpisodeDao {

    @Query("SELECT episodeNumber FROM watched_episodes WHERE animeId = :animeId")
    fun getWatchedNumbers(animeId: Int): LiveData<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<WatchedEpisode>)

    @Query("DELETE FROM watched_episodes WHERE animeId = :animeId AND episodeNumber = :episodeNumber")
    suspend fun delete(animeId: Int, episodeNumber: Int)
}
