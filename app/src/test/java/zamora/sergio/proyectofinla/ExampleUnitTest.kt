package zamora.sergio.proyectofinal

import org.junit.Assert.*
import org.junit.Test
import zamora.sergio.proyectofinal.data.local.FavoriteAnime

class AnimeTrackerUnitTest {

    // --- Formateo de score (misma lógica que en FavoritesAdapter y DetailFragment) ---

    @Test
    fun score_showsNA_whenZero() {
        val score = 0.0
        val result = if (score > 0) "★ $score" else "★ N/A"
        assertEquals("★ N/A", result)
    }

    @Test
    fun score_showsValue_whenPositive() {
        val score = 8.5
        val result = if (score > 0) "★ $score" else "★ N/A"
        assertEquals("★ 8.5", result)
    }

    // --- Formateo de episodios ---

    @Test
    fun episodes_showsQuestionMark_whenZero() {
        val episodes = 0
        val result = if (episodes > 0) "$episodes eps" else "? eps"
        assertEquals("? eps", result)
    }

    @Test
    fun episodes_showsCount_whenPositive() {
        val episodes = 24
        val result = if (episodes > 0) "$episodes eps" else "? eps"
        assertEquals("24 eps", result)
    }

    // --- Validación de búsqueda (misma lógica que en SearchViewModel) ---

    @Test
    fun searchQuery_isBlank_whenOnlySpaces() {
        val query = "   "
        assertTrue(query.isBlank())
    }

    @Test
    fun searchQuery_isNotBlank_whenHasContent() {
        val query = "Naruto"
        assertFalse(query.isBlank())
    }

    // --- Entidad FavoriteAnime ---

    @Test
    fun favoriteAnime_storesAllFieldsCorrectly() {
        val anime = FavoriteAnime(
            id = 20,
            title = "Naruto",
            imageUrl = "https://cdn.myanimelist.net/images/anime/13/17405.jpg",
            synopsis = "Historia de un ninja.",
            score = 7.98,
            episodes = 220
        )
        assertEquals(20, anime.id)
        assertEquals("Naruto", anime.title)
        assertEquals(220, anime.episodes)
        assertEquals(7.98, anime.score, 0.001)
    }

    @Test
    fun favoriteAnime_scoreZero_isValid() {
        val anime = FavoriteAnime(
            id = 1,
            title = "Anime sin puntuacion",
            imageUrl = "",
            synopsis = "",
            score = 0.0,
            episodes = 0
        )
        assertEquals(0.0, anime.score, 0.001)
    }
}
