package ru.nortti.filmssearch.view.fragments

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import ru.nortti.filmssearch.*
import ru.nortti.filmssearch.view.adapters.MovieAdapter
import ru.nortti.filmssearch.view.adapters.utils.PaginationScrollListener
import ru.nortti.filmssearch.viewModel.viewModels.MoviesViewModel
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.*
import ru.nortti.filmssearch.view.adapters.utils.ItemAnimator
import ru.nortti.filmssearch.view.adapters.utils.ItemOffsetDecoration

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

    lateinit var movieAdapter: MovieAdapter
    lateinit var prefs: SharedPreference
    private var isLoading = false
    private var isLastPage = false
    private var current_page = PAGE_START
    lateinit var layoutManager: LinearLayoutManager

    private var viewModel: MoviesViewModel? = null

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = SharedPreference(requireContext())
        movieAdapter = MovieAdapter(requireContext(), prefs, object : MovieAdapter.MovieCallback {
            override fun onMovieAdded() {
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onClick(movie_id: Int) {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                val frag = DetailsFragment.newInstance(movie_id)
                transaction.replace(R.id.container, frag)
                transaction.addToBackStack(null)
                transaction.commit()
            }

        })

        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(activity!!).get(MoviesViewModel::class.java)
        viewModel!!.movies.observe(this.viewLifecycleOwner, Observer<MovieResponse> { movie -> movieAdapter.setMovies(movie.results)})
        viewModel!!.errors.observe(this.viewLifecycleOwner, Observer<Throwable>{ t ->
            Snackbar.make(requireView(), t.message.toString(), Snackbar.LENGTH_SHORT).setAction(getString(
                R.string.repeat), View.OnClickListener {
                viewModel!!.onGetData()
            }).show()
        } )
        viewModel!!.customErrors.observe(this.viewLifecycleOwner, Observer<ErrorResponse> { error -> Snackbar.make(requireView(), String.format("%s %s", error.status_code, error.status_message), Snackbar.LENGTH_SHORT).setAction(getString(
                    R.string.repeat), View.OnClickListener {
            viewModel!!.onGetData()
        }).show() })


        rvList.layoutManager = layoutManager
        rvList.adapter = movieAdapter
        rvList.addItemDecoration(
            ItemOffsetDecoration(
                20
            )
        )
        rvList.itemAnimator =
            ItemAnimator(requireContext())
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
        viewModel!!.onGetData()

        swipeRefreshLayout.setOnRefreshListener {
            viewModel!!.movies.observe(this.viewLifecycleOwner, Observer<MovieResponse> { movie -> movieAdapter.setMovies(movie.results) })
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()
        Extensions.setLocale(requireContext(), prefs.getValueString(
            LANGUAGE)!!)
        Extensions.setAppTheme(prefs.getValueString(
            THEME)!!)
        movieAdapter.notifyDataSetChanged()
    }

    fun loadNextPage() {

        viewModel!!.onGetPagedData(current_page)
    }


}
