package ru.nortti.filmssearch.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_film.view.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.remote.Movie
import ru.nortti.filmssearch.utils.SharedPreference

class FilmsAdapter(var context: Context, var prefs: SharedPreference, var isFav: Boolean) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private var items: MutableList<Movie> = mutableListOf()

    private lateinit var listener: OnClickListener
    fun addFilms(films: MutableList<Movie>) {
        if(!items.isEmpty()) items = mutableListOf()
        items = films
        notifyDataSetChanged()
    }

    fun addFilm(film: Movie){
        items.add(film)
        notifyItemInserted(items.indexOf(film))
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.listener = clickListener
    }

    interface OnClickListener {
        fun onClick(movie_id: Int)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            itemView.title.text = item.title
            val drawableResourceId: Int = context.getResources()
                .getIdentifier(item.poster_path, "drawable", context.getPackageName())

            Glide
                .with(context)
                .load(drawableResourceId)
                .centerCrop()
                .into(itemView.poster);

            if (item.isActive) itemView.title.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorPrimaryLight
                )
            )
            if (prefs.getFavorites().contains(item)){
                Glide
                    .with(context)
                    .load(R.drawable.ic_heart_filled)
                    .centerCrop()
                    .into(itemView.fav);
            }
            itemView.poster.setOnLongClickListener {
                if (isFav) {
                    if  (prefs.getFavorites().contains(item)){
                        var pos = adapterPosition
                        Snackbar.make(it, String.format(context.getString(R.string.remove_from_fav), item.title), Snackbar.LENGTH_SHORT).setAction(
                            R.string.undo, View.OnClickListener {
                            prefs.addToFav(pos, item)
                            notifyItemInserted(pos)
                        }).show()

                        prefs.removeFromFav(item)
                        notifyItemRemoved(adapterPosition)
                    }
                } else {
                    if  (!prefs.getFavorites().contains(item)){
                        prefs.addToFav(item)
                        Snackbar.make(it, String.format(context.getString(R.string.add_to_fav), item.title), Snackbar.LENGTH_SHORT).show()
                        notifyItemChanged(adapterPosition)

                    }
                }
                return@setOnLongClickListener false
            }
            itemView.details.setOnClickListener {
                item.isActive = true
                notifyItemChanged(adapterPosition)
                listener.onClick(item.id)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_film, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var film = items[position]
        holder.bind(film)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}