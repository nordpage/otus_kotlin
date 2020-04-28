package ru.nortti.filmssearch.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_favorites.view.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.utils.Extensions.getImageUrl

class FavoritesAdapter(var callback : FavoritesCallback) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var favorites : MutableList<Favorite> = mutableListOf()

    fun setFavorites(list : List<Favorite>) {
        list.forEach {
            addFavorite(it)
        }
    }
    inner class ViewHolder(itemView : View)  : RecyclerView.ViewHolder(itemView) {

            fun bind(item : Favorite) {

                itemView.title.text = item.title

                Glide
                    .with(itemView.context)
                    .load(getImageUrl(item.poster))
                    .centerCrop()
                    .into(itemView.poster)

                itemView.details.setOnClickListener {
                    callback.onClick(item.id)
                }

                itemView.poster.setOnLongClickListener {
                    callback.onRemove(item.id)
                    favorites.remove(item)
                    notifyItemChanged(adapterPosition)
                    return@setOnLongClickListener false
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorites, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = favorites[position]
        holder.bind(item)
    }

    interface FavoritesCallback {
        fun onClick(movie_id: Int)
        fun onRemove(movie_id: Int)
    }

    private fun addFavorite(favorite: Favorite){
        favorites.add(favorite)
        notifyItemInserted(favorites.size - 1)
    }
}