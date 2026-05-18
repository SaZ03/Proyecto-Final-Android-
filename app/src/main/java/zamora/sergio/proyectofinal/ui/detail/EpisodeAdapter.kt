package zamora.sergio.proyectofinal.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import zamora.sergio.proyectofinal.data.remote.EpisodeItem
import zamora.sergio.proyectofinal.databinding.ItemEpisodeBinding

class EpisodeAdapter(
    private val onToggle: (episodeNumber: Int, isChecked: Boolean) -> Unit
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    private var episodes: List<EpisodeItem> = emptyList()
    private var watchedSet: Set<Int> = emptySet()

    inner class EpisodeViewHolder(val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]
        with(holder.binding) {
            tvEpisodeNumber.text = episode.mal_id.toString()
            tvEpisodeTitle.text = episode.title?.takeIf { it.isNotBlank() } ?: "Episodio ${episode.mal_id}"
            tvEpisodeDate.text = episode.aired?.take(10) ?: ""

            // Desconectar listener antes de cambiar el estado para evitar disparos falsos
            cbWatched.setOnCheckedChangeListener(null)
            cbWatched.isChecked = episode.mal_id in watchedSet
            cbWatched.setOnCheckedChangeListener { _, isChecked ->
                onToggle(episode.mal_id, isChecked)
            }
        }
    }

    override fun getItemCount() = episodes.size

    fun update(newEpisodes: List<EpisodeItem>, newWatched: Set<Int>) {
        episodes = newEpisodes
        watchedSet = newWatched
        notifyDataSetChanged()
    }
}
