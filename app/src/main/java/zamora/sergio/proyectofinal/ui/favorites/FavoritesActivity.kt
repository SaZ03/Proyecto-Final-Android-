package zamora.sergio.proyectofinal.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import zamora.sergio.proyectofinal.databinding.ActivityFavoritesBinding
import zamora.sergio.proyectofinal.ui.detail.DetailActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Mis favoritos"

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
            emptyList(),
            onClick = { anime ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("ANIME_ID", anime.id)
                startActivity(intent)
            },
            onDelete = { anime ->
                AlertDialog.Builder(this)
                    .setTitle("Quitar favorito")
                    .setMessage("¿Quitar ${anime.title} de tus favoritos?")
                    .setPositiveButton("Sí") { _, _ -> viewModel.removeFavorite(anime.id) }
                    .setNegativeButton("No", null)
                    .show()
            }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favorites.observe(this) { list ->
            adapter.updateList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
