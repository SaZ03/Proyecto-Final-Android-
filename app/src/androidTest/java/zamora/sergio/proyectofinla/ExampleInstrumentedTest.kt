package zamora.sergio.proyectofinal

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import zamora.sergio.proyectofinal.data.local.AnimeDatabase
import zamora.sergio.proyectofinal.data.local.FavoriteAnime
import zamora.sergio.proyectofinal.data.local.FavoriteAnimeDao

@RunWith(AndroidJUnit4::class)
class AnimeDatabaseTest {

    private lateinit var db: AnimeDatabase
    private lateinit var dao: FavoriteAnimeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AnimeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.favoriteAnimeDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertFavorite_andRetrieveById() = runBlocking {
        val anime = FavoriteAnime(1, "Attack on Titan", "url", "Synopsis", 9.0, 25)
        dao.insert(anime)
        val retrieved = dao.getById(1)
        assertNotNull(retrieved)
        assertEquals("Attack on Titan", retrieved?.title)
        assertEquals(9.0, retrieved?.score ?: 0.0, 0.001)
    }

    @Test
    fun deleteById_removesFavorite() = runBlocking {
        val anime = FavoriteAnime(2, "One Piece", "url", "Piratas", 8.7, 1000)
        dao.insert(anime)
        dao.deleteById(2)
        val retrieved = dao.getById(2)
        assertNull(retrieved)
    }

    @Test
    fun insertMultiple_allCanBeRetrieved() = runBlocking {
        dao.insert(FavoriteAnime(3, "Bleach", "url", "Soul Reaper", 7.9, 366))
        dao.insert(FavoriteAnime(4, "FMA Brotherhood", "url", "Alquimia", 9.1, 64))
        assertNotNull(dao.getById(3))
        assertNotNull(dao.getById(4))
    }

    @Test
    fun insertSameId_replacesExisting() = runBlocking {
        dao.insert(FavoriteAnime(5, "Original", "url", "Synopsis", 7.0, 12))
        dao.insert(FavoriteAnime(5, "Reemplazado", "url", "Synopsis", 8.0, 12))
        val retrieved = dao.getById(5)
        assertEquals("Reemplazado", retrieved?.title)
    }
}
