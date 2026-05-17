package zamora.sergio.proyectofinal.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import zamora.sergio.proyectofinal.R
import zamora.sergio.proyectofinal.data.local.FavoriteAnime
import zamora.sergio.proyectofinal.databinding.ItemFavoriteBinding

class FavoritesAdapter(
    private var list: List<FavoriteAnime>,
    private val onDelete: (FavoriteAnime) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val anime = list[position]
        with(holder.binding) {
            tvTitle.text = anime.title
            tvScore.text = if (anime.score > 0) "★ ${anime.score}" else "★ N/A"
            tvEpisodes.text = if (anime.episodes > 0) "${anime.episodes} eps" else "? eps"
            Glide.with(holder.itemView.context)
                .load(anime.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(ivCover)
            btnDelete.setOnClickListener { onDelete(anime) }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<FavoriteAnime>) {
        list = newList
        notifyDataSetChanged()
    }
}
