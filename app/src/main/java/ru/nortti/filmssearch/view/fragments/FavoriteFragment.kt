package ru.nortti.filmssearch.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.nortti.filmssearch.*
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.view.adapters.FavoritesAdapter
import ru.nortti.filmssearch.viewModel.viewModels.FavoritesViewModel
import ru.nortti.filmssearch.viewModel.viewModels.TransferViewModel

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {
    private lateinit var prefs: SharedPreference
    private lateinit var adapter: FavoritesAdapter
    private var transferViewModel : TransferViewModel? = null
    private var viewModel: FavoritesViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        adapter = FavoritesAdapter(
                object : FavoritesAdapter.FavoritesCallback {
                    override fun onClick(movie_id: Int) {
                        transferViewModel!!.setMovieIdData(movie_id)

                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.container, DetailsFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }

                    override fun onRemove(movie_id: Int) {
                        viewModel!!.deleteById(movie_id)
                        adapter.notifyDataSetChanged()
                    }

                }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = GridLayoutManager(requireContext(), 2)
        rvList.adapter = adapter

        viewModel = ViewModelProviders.of(activity!!).get(FavoritesViewModel::class.java)
        viewModel!!.favorites.observe(this.viewLifecycleOwner, Observer<List<Favorite>> { list -> adapter.setFavorites(list)})

        transferViewModel = ViewModelProviders.of(activity!!).get(TransferViewModel::class.java)

    }

}
