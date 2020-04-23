package ru.nortti.filmssearch.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*
import ru.nortti.filmssearch.Constants.getImageUrl
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.network.models.Movie

class MovieAdapter(var context: Context, var callback: MovieCallback) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

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

    fun removeMovie(movie: Movie) {
        var position = movies.indexOf(movie)
        if  (position > -1) {
            movies.removeAt(position)
            notifyItemRemoved(position)
        }
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