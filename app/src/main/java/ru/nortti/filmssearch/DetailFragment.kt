package ru.nortti.filmssearch

import android.content.Intent.getIntent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.descriptionTx
import kotlinx.android.synthetic.main.activity_details.poster
import kotlinx.android.synthetic.main.activity_details.titleTx
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    var film: Film = Film()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            film = getArguments()!!.getParcelable<Film>("film")!!
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawableResourceId: Int = getResources()
            .getIdentifier(film.image, "drawable", requireActivity().getPackageName())
        Glide
            .with(this)
            .load(drawableResourceId)
            .into(poster)

        toolbar.title = film.name
        descriptionTx.text = film.desctiption
    }

}
