package ru.nortti.filmssearch.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.nortti.filmssearch.*
import ru.nortti.filmssearch.view.adapters.FilmsAdapter
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.view.adapters.MovieAdapter

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {
    lateinit var prefs: SharedPreference
    lateinit var adapter: MovieAdapter

    companion object {
        private const val TAG = "FavoritesActivity"
        private const val RESULT_CODE = 0
    }

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
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    val frag = DetailsFragment.newInstance(movie_id)
                    transaction.replace(R.id.container, frag)
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
    }

}
