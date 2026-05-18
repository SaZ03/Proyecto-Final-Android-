package zamora.sergio.proyectofinal.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import zamora.sergio.proyectofinal.R
import zamora.sergio.proyectofinal.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var episodeAdapter: EpisodeAdapter

    private var synopsisExpanded = false

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

        setupSynopsisToggle()
        setupEpisodesRecycler()
        observeViewModel()

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupSynopsisToggle() {
        binding.tvShowMore.setOnClickListener {
            synopsisExpanded = !synopsisExpanded
            binding.tvSynopsis.maxLines = if (synopsisExpanded) Int.MAX_VALUE else 4
            binding.tvShowMore.text = if (synopsisExpanded) "Ver menos ▴" else "Ver más ▾"
        }
    }

    private fun setupEpisodesRecycler() {
        episodeAdapter = EpisodeAdapter { episodeNumber, isChecked ->
            viewModel.toggleWatched(episodeNumber, isChecked)
        }
        binding.rvEpisodes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEpisodes.adapter = episodeAdapter
        binding.rvEpisodes.isNestedScrollingEnabled = false
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

            val broadcastText = anime.broadcast?.string
            if (anime.status == "Currently Airing" && !broadcastText.isNullOrBlank()) {
                binding.tvBroadcast.text = "En emisión · $broadcastText"
                binding.tvBroadcast.visibility = View.VISIBLE
            } else {
                binding.tvBroadcast.visibility = View.GONE
            }

            Glide.with(this)
                .load(anime.images.jpg.image_url)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivCover)
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFav ->
            binding.btnFavorite.text = if (isFav) "Quitar de favoritos" else "Agregar a favoritos"
        }

        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            val watched = viewModel.watchedNumbers.value?.toSet() ?: emptySet()
            if (episodes.isEmpty()) {
                binding.tvEpisodesEmpty.text = "No hay episodios disponibles"
                binding.tvEpisodesEmpty.visibility = View.VISIBLE
                binding.rvEpisodes.visibility = View.GONE
            } else {
                binding.tvEpisodesEmpty.visibility = View.GONE
                binding.rvEpisodes.visibility = View.VISIBLE
                episodeAdapter.update(episodes, watched)
            }
        }

        viewModel.watchedNumbers.observe(viewLifecycleOwner) { watchedList ->
            val episodes = viewModel.episodes.value ?: return@observe
            if (episodes.isNotEmpty()) {
                episodeAdapter.update(episodes, watchedList.toSet())
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
