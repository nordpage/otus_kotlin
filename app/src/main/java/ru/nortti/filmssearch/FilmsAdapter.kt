package ru.nortti.filmssearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(var context: Context, var prefs: SharedPreference, var isFav: Boolean) : RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private var items: MutableList<Film> = mutableListOf()

    private lateinit var listener: FilmsAdapter.OnClickListener
    fun addFilms(films: MutableList<Film>) {
        if(!items.isEmpty()) items = mutableListOf()
        items = films
        notifyDataSetChanged()
    }

    fun addFilm(film: Film){
        items.add(film)
        notifyItemInserted(items.indexOf(film))
    }

    fun setOnClickListener(clickListener: OnClickListener) {
        this.listener = clickListener
    }

    interface OnClickListener {
        fun onClick(item: Film)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Film) {
            itemView.title.text = item.name
            val drawableResourceId: Int = context.getResources()
                .getIdentifier(item.image, "drawable", context.getPackageName())

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
            if (prefs!!.getFavorites().contains(item)){
                Glide
                    .with(context)
                    .load(R.drawable.ic_heart_filled)
                    .centerCrop()
                    .into(itemView.fav);
            }
            itemView.poster.setOnLongClickListener {
                if (isFav) {
                    if  (prefs!!.getFavorites().contains(item)){
                        prefs!!.removeFromFav(item)
                        notifyItemRemoved(adapterPosition)
                    }
                } else {
                    if  (!prefs!!.getFavorites().contains(item)){
                        prefs!!.addToFav(item)
                        notifyItemChanged(adapterPosition)

                    }
                }
                return@setOnLongClickListener false
            }
            itemView.details.setOnClickListener {
                item.isActive = true
                notifyItemChanged(adapterPosition)
                listener.onClick(item)

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