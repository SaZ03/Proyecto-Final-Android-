package zamora.sergio.proyectofinal.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoriteAnime::class], version = 2, exportSchema = false)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun favoriteAnimeDao(): FavoriteAnimeDao

    companion object {
        @Volatile
        private var instance: AnimeDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE favorites ADD COLUMN addedAt INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getInstance(context: Context): AnimeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnimeDatabase::class.java,
                    "anime_db"
                ).addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}
