package ru.nortti.filmssearch.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.nortti.filmssearch.*
import ru.nortti.filmssearch.utils.ItemAnimator
import ru.nortti.filmssearch.utils.ItemOffsetDecoration

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    lateinit var adapter: FilmsAdapter
    lateinit var prefs: SharedPreference
    private val filmViewModel by lazy { ViewModelProviders.of(this).get(FilmViewModel::class.java) }
    private var cells = 2
    lateinit var layoutManager: GridLayoutManager

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = SharedPreference(requireContext())

        adapter = FilmsAdapter(requireContext(), prefs, false)
        cells = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> throw IllegalStateException()
        }
        layoutManager = GridLayoutManager(requireContext(), cells)

        filmViewModel.getListFilms().observe(this, Observer {
            it?.let {
                initAdapter(it)
            }
        })

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
        })


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = layoutManager
        rvList.adapter = adapter
        rvList.addItemDecoration(ItemOffsetDecoration(20))
        rvList.itemAnimator = ItemAnimator(requireContext())

    }
    private fun initAdapter(list: List<Film>) {
        list.forEach {
            adapter.addFilm(it)
        }
    }

    override fun onResume() {
        super.onResume()
        Constants.setLocale(requireContext(), prefs.getValueString(Constants.LANGUAGE)!!)
        Constants.setAppTheme(prefs.getValueString(Constants.THEME)!!)
        adapter.notifyDataSetChanged()
    }


}
