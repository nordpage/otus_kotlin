package ru.nortti.filmssearch.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.descriptionTx
import kotlinx.android.synthetic.main.activity_details.poster
import kotlinx.android.synthetic.main.fragment_detail.*
import ru.nortti.filmssearch.Constants.getImageUrl
import ru.nortti.filmssearch.Constants.getImageUrlBig

import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.arch.models.DetailsViewModel
import ru.nortti.filmssearch.network.models.MovieDetail
import ru.nortti.filmssearch.network.models.MovieResponce

class DetailsFragment : Fragment() {

    var movie_id: Int = 0
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            movie_id = arguments!!.getInt(ARG_PARAM)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        viewModel.getDetailedData(movie_id)
        viewModel.movies.observe(this.viewLifecycleOwner, Observer<MovieDetail> {

            Glide
                .with(this)
                .load(getImageUrlBig(it.posterPath))
                .into(poster)

            toolbar.title = it.title
            descriptionTx.text = it.overview
        })
    }

    companion object {
        private val ARG_PARAM = "movie_id"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param myObject as MyObject.
         * @return A new instance of fragment MyFragment.
         */
        fun newInstance(id: Int): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, id)
            fragment.arguments = args
            return fragment
        }
    }
}
