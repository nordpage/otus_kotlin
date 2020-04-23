package ru.nortti.filmssearch.view.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_details.descriptionTx
import kotlinx.android.synthetic.main.activity_details.poster
import kotlinx.android.synthetic.main.fragment_detail.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieDetail
import ru.nortti.filmssearch.utils.Extensions.getImageUrlBig
import ru.nortti.filmssearch.viewModel.viewModels.DetailsViewModel


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
                .load(getImageUrlBig(it.poster_path))
                .into(poster)


            toolbar.title = it.title
            descriptionTx.text = it.overview
        })

        viewModel.errors.observe(this.viewLifecycleOwner, Observer<Throwable>{ t ->
            Snackbar.make(requireView(), t.message.toString(), Snackbar.LENGTH_SHORT).setAction(getString(
                R.string.repeat), View.OnClickListener {
                viewModel.getDetailedData(movie_id)
            }).show()
        } )
        viewModel.customErrors.observe(this.viewLifecycleOwner, Observer<ErrorResponse> { error -> Snackbar.make(requireView(), String.format("%s %s", error.status_code, error.status_message), Snackbar.LENGTH_SHORT).setAction(getString(
            R.string.repeat), View.OnClickListener {
            viewModel.getDetailedData(movie_id)
        }).show() })
    }

    companion object {
        private val ARG_PARAM = "movie_id"

        fun newInstance(id: Int): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM, id)
            fragment.arguments = args
            return fragment
        }
    }
}
