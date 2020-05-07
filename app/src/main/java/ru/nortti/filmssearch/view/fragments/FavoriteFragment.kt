package ru.nortti.filmssearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.view.adapters.MovieAdapter
import ru.nortti.filmssearch.viewModel.viewModels.TransferViewModel

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {
    private lateinit var prefs: SharedPreference
    private lateinit var adapter: MovieAdapter
    private var transferViewModel : TransferViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = SharedPreference(requireContext())

        adapter = MovieAdapter(
            requireContext(),
            prefs,
            object : MovieAdapter.MovieCallback {
                override fun onMovieAdded() {

                }

                override fun onClick(movie_id: Int) {
                    transferViewModel!!.setMovieIdData(movie_id)
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, DetailsFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

            }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvList.adapter = adapter
        adapter.setMovies(prefs.getFavorites())

        transferViewModel = ViewModelProviders.of(activity!!).get(TransferViewModel::class.java)

    }

}
