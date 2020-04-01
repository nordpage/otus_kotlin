package ru.nortti.filmssearch.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_favorite.*
import ru.nortti.filmssearch.*

/**
 * A simple [Fragment] subclass.
 */
class FavoriteFragment : Fragment() {
    lateinit var prefs: SharedPreference
    private var cells = 2
    lateinit var adapter: FilmsAdapter

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

        adapter = FilmsAdapter(requireContext(), prefs, true)
        cells = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> throw IllegalStateException()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = GridLayoutManager(requireContext(), cells)
        rvList.adapter = adapter
        adapter.addFilms(prefs.getFavorites())
        adapter.setOnClickListener(object : FilmsAdapter.OnClickListener {
            override fun onClick(item: Film) {
                val data = Bundle()
                data.putParcelable("film", item)
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                val frag = DetailFragment()
                frag.arguments = data
                transaction.replace(R.id.container, frag)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        }
        )
    }

}
