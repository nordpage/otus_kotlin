package ru.nortti.filmssearch.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_film.view.*
import kotlinx.android.synthetic.main.item_film.view.poster
import kotlinx.android.synthetic.main.item_film.view.title
import kotlinx.android.synthetic.main.item_pending.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.local.models.Pending
import ru.nortti.filmssearch.utils.Extensions

class PendingAdapter(var list: MutableList<Pending>, var callback: PendingAdapter.PendingCallback) : RecyclerView.Adapter<PendingAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pending, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list.get(position)
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item : Pending){

            Glide
                .with(itemView.context)
                .load(Extensions.getImageUrl(item.poster))
                .centerCrop()
                .into(itemView.poster)

            itemView.title.text = item.title

            val formatter : DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")
            val ld : DateTime = DateTime(item.time)
            itemView.alarmTitle.text = ld.toString(formatter)
        }
    }

    interface PendingCallback {

    }
}