package zamora.sergio.proyectofinal.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import zamora.sergio.proyectofinal.R
import zamora.sergio.proyectofinal.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val animeId = intent.getIntExtra("ANIME_ID", -1)
        if (animeId == -1) {
            finish()
            return
        }

        if (savedInstanceState == null) {
            val fragment = DetailFragment.newInstance(animeId)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
