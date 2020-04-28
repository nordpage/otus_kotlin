package ru.nortti.filmssearch.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_film.view.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.remote.Movie
import ru.nortti.filmssearch.utils.Extensions.getImageUrl
import ru.nortti.filmssearch.utils.SharedPreference

class MovieAdapter(var context: Context, var prefs: SharedPreference, var callback: MovieCallback) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var movies: MutableList<Movie> = mutableListOf()

    fun  setMovies(list: List<Movie>){
        list.forEach {
            addMovie(it)
        }
        callback.onMovieAdded()
    }

    fun addMovie(movie: Movie){
        movies.add(movie)
        notifyItemInserted(movies.size - 1)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) {
            itemView.title.text = item.title
            Glide
                .with(itemView.context)
                .load(getImageUrl(item.poster_path))
                .centerCrop()
                .into(itemView.poster)

            itemView.ratingTx.text = String.format(itemView.context.getString(R.string.rating), item.vote_average, item.vote_count)

            itemView.details.setOnClickListener {
                callback.onClick(item.id.toInt())
            }
            if (prefs.getFavorites().contains(item)){
                Glide
                    .with(context)
                    .load(R.drawable.ic_heart_filled)
                    .centerCrop()
                    .into(itemView.fav);
            }
            itemView.poster.setOnLongClickListener {
                    val pos = movies.indexOf(item)
                    if  (prefs.getFavorites().contains(item)){
                        Snackbar.make(it, String.format(context.getString(R.string.remove_from_fav), item.title), Snackbar.LENGTH_SHORT).setAction(
                            R.string.undo, View.OnClickListener {
                                prefs.addToFav(pos, item)
                                notifyItemInserted(pos)
                            }).show()

                        prefs.removeFromFav(item)
                        if  (pos > -1) {
                            movies.removeAt(pos)
                            notifyItemRemoved(position)
                        }
                    } else {
                        prefs.addToFav(item)
                        Snackbar.make(it, String.format(context.getString(R.string.add_to_fav), item.title), Snackbar.LENGTH_SHORT).show()
                        notifyItemChanged(pos)
                    }

                return@setOnLongClickListener false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_film, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    interface MovieCallback {
        fun onMovieAdded()
        fun onClick(movie_id: Int)
    }
}