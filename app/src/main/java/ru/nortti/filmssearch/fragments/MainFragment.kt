package ru.nortti.filmssearch.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nortti.filmssearch.*
import ru.nortti.filmssearch.Constants.API_KEY
import ru.nortti.filmssearch.Constants.LANGUAGE
import ru.nortti.filmssearch.Constants.PAGE_START
import ru.nortti.filmssearch.Constants.TOTAL_PAGES
import ru.nortti.filmssearch.adapters.MovieAdapter
import ru.nortti.filmssearch.adapters.PaginationScrollListener
import ru.nortti.filmssearch.network.ApiInterface
import ru.nortti.filmssearch.network.models.MovieResponce
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
    lateinit var movieAdapter: MovieAdapter
    lateinit var prefs: SharedPreference
    private val filmViewModel by lazy { ViewModelProviders.of(this).get(FilmViewModel::class.java) }
    private var cells = 2
    private var isLoading = false
    private var isLastPage = false
    private var current_page = PAGE_START
    lateinit var layoutManager: LinearLayoutManager
    var callback: Callback<MovieResponce>? = null
    var result: Call<MovieResponce>? = null
    var pagination: Call<MovieResponce>? = null
    var apiService: ApiInterface? = null

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = SharedPreference(requireContext())
        movieAdapter = MovieAdapter(requireContext())

        adapter = FilmsAdapter(requireContext(), prefs, false)
        cells = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> throw IllegalStateException()
        }
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        filmViewModel.getListFilms().observe(this, Observer {
            it?.let {
                //  initAdapter(it)
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


        apiService = App.getInstance().getService()
        result =
            apiService!!.getTopRatedMovies(API_KEY, prefs.getValueString(LANGUAGE)!!)
        callback = object : Callback<MovieResponce> {
            override fun onFailure(call: Call<MovieResponce>, t: Throwable) {
                Log.e("Error", t.toString())
            }

            override fun onResponse(call: Call<MovieResponce>, response: Response<MovieResponce>) {
                val list = response.body()!!.results
                movieAdapter.setMovies(list)
            }

        }
    }


    fun loadInitPage() {
        result!!.enqueue(callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvList.layoutManager = layoutManager
        rvList.adapter = movieAdapter
        rvList.addItemDecoration(ItemOffsetDecoration(20))
        rvList.itemAnimator = ItemAnimator(requireContext())
        rvList.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                swipeRefreshLayout.isRefreshing = isLoading
                current_page = current_page + 1
                Log.d("MainFragment", "loadNextPage: $current_page")
                Handler().postDelayed(object : Runnable {
                    override fun run() {
                        loadNextPage()
                    }

                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })
        loadInitPage()

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed(object: Runnable{
                override fun run() {
                    result =
                        apiService!!.getTopRatedMovies(API_KEY, prefs.getValueString(LANGUAGE)!!)
                    result!!.enqueue(callback)
                    swipeRefreshLayout.isRefreshing = false
                }

            }, 1500)
        }
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

    fun loadNextPage() {
        pagination = apiService!!.getTopRatedMoviesWithPagination(
            API_KEY, prefs.getValueString(
                LANGUAGE
            )!!, current_page
        )
        pagination!!.enqueue(object : Callback<MovieResponce>{
            override fun onFailure(call: Call<MovieResponce>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieResponce>, response: Response<MovieResponce>) {
                isLoading = false
                swipeRefreshLayout.isRefreshing = isLoading
                movieAdapter.setMovies(response.body()!!.results)
                if (current_page == TOTAL_PAGES) isLastPage = true

            }

        })
    }


}
