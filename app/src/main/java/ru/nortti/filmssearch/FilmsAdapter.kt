package ru.nortti.filmssearch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*

class FilmsAdapter(var context: Context): RecyclerView.Adapter<FilmsAdapter.ViewHolder>() {

    private var items : List<Film> = emptyList()

    private lateinit var listener: FilmsAdapter.OnClickListener
    fun addFilms(films: List<Film>) {
        items = films
        notifyDataSetChanged()
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

            if (item.isActive) itemView.title.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryLight))
            itemView.details.setOnClickListener {
                item.isActive = true
                notifyItemChanged(adapterPosition)
                if (listener != null) {
                    listener.onClick(item)
                }

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
}