package zamora.sergio.proyectofinal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteAnime::class, WatchedEpisode::class], version = 3, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun favoriteAnimeDao(): FavoriteAnimeDao
    abstract fun watchedEpisodeDao(): WatchedEpisodeDao

    companion object {
        @Volatile
        private var instance: AnimeDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE favorites ADD COLUMN addedAt INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS watched_episodes (animeId INTEGER NOT NULL, episodeNumber INTEGER NOT NULL, PRIMARY KEY(animeId, episodeNumber))"
                )
            }
        }

        fun getInstance(context: Context): AnimeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime_db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { instance = it }
            }
    }
}
