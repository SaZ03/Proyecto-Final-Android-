package zamora.sergio.proyectofinal.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import zamora.sergio.proyectofinal.R
import zamora.sergio.proyectofinal.data.remote.Anime
import zamora.sergio.proyectofinal.databinding.ItemAnimeBinding

class AnimeAdapter(
    private var list: List<Anime>,
    private val onClick: (Anime) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = list[position]
        with(holder.binding) {
            tvTitle.text = anime.title
            tvScore.text = if ((anime.score ?: 0.0) > 0) "★ ${anime.score}" else "★ N/A"
            Glide.with(holder.itemView.context)
                .load(anime.images.jpg.image_url)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(ivCover)
            root.setOnClickListener { onClick(anime) }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Anime>) {
        list = newList
        notifyDataSetChanged()
    }
}
