package zamora.sergio.proyectofinal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import zamora.sergio.proyectofinal.R
import zamora.sergio.proyectofinal.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()

    companion object {
        fun newInstance(animeId: Int): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putInt("ANIME_ID", animeId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animeId = arguments?.getInt("ANIME_ID") ?: return
        viewModel.loadAnime(animeId)

        observeViewModel()

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.scrollContent.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.anime.observe(viewLifecycleOwner) { anime ->
            anime ?: return@observe
            requireActivity().title = anime.title
            binding.tvTitle.text = anime.title
            binding.tvScore.text = if ((anime.score ?: 0.0) > 0) "Puntuación: ★ ${anime.score}" else "Puntuación: N/A"
            binding.tvEpisodes.text = if ((anime.episodes ?: 0) > 0) "Episodios: ${anime.episodes}" else "Episodios: ?"
            binding.tvYear.text = if (anime.year != null) "Año: ${anime.year}" else "Año: Desconocido"
            binding.tvGenres.text = "Géneros: " + (anime.genres?.joinToString(", ") { it.name } ?: "N/A")
            binding.tvSynopsis.text = anime.synopsis ?: "Sin sinopsis disponible."
            Glide.with(this)
                .load(anime.images.jpg.image_url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivCover)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFav ->
            binding.btnFavorite.text = if (isFav) "Quitar de favoritos" else "Agregar a favoritos"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
