package ru.nortti.filmssearch.view.fragments

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.pending_dialog.view.*
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.Movie
import ru.nortti.filmssearch.model.remote.MovieResponse
import ru.nortti.filmssearch.utils.*
import ru.nortti.filmssearch.utils.Extensions.ToPending
import ru.nortti.filmssearch.utils.Extensions.getAlertTime
import ru.nortti.filmssearch.view.adapters.MovieAdapter
import ru.nortti.filmssearch.view.adapters.utils.ItemAnimator
import ru.nortti.filmssearch.view.adapters.utils.ItemOffsetDecoration
import ru.nortti.filmssearch.view.adapters.utils.PaginationScrollListener
import ru.nortti.filmssearch.viewModel.viewModels.FavoritesViewModel
import ru.nortti.filmssearch.viewModel.viewModels.MoviesViewModel
import ru.nortti.filmssearch.viewModel.viewModels.PendingViewModel
import ru.nortti.filmssearch.viewModel.viewModels.TransferViewModel
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


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

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var prefs: SharedPreference
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = PAGE_START
    lateinit var layoutManager: LinearLayoutManager

    private var viewModel: MoviesViewModel? = null
    private var transferViewModel : TransferViewModel? = null
    private var favoritesViewModel : FavoritesViewModel? = null
    private var pendingViewModel : PendingViewModel?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = SharedPreference(requireContext())
        transferViewModel = ViewModelProviders.of(activity!!).get(TransferViewModel::class.java)
        favoritesViewModel = ViewModelProviders.of(activity!!).get(FavoritesViewModel::class.java)
        pendingViewModel = ViewModelProviders.of(activity!!).get(PendingViewModel::class.java)
        movieAdapter = MovieAdapter(favoritesViewModel!!, object : MovieAdapter.MovieCallback {
            override fun onMovieAdded() {
                swipeRefreshLayout.isRefreshing = false
            }

            override fun onClick(movie_id: Int) {
                toDetails(movie_id)
            }

            override fun onReminder(movie: Movie) {
                showDialog(movie)
            }

        })
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (activity!!.intent.extras != null) {
            val id = activity!!.intent.extras!!.getInt(EXTRA_ID)
            toDetails(id)
            pendingViewModel!!.removeFromPendingById(id)
        }
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
                currentPage = currentPage + 1
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

        viewModel!!.onGetPagedData(currentPage)
    }

    fun toDetails(movie_id: Int) {
        transferViewModel!!.setMovieIdData(movie_id)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, DetailsFragment())
        transaction.addToBackStack(null)
        transaction.commit()
        activity!!.intent.removeExtra(EXTRA_ID)

    }


    fun showDialog(item : Movie) {
        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.pending_dialog, null)
        builder.setView(mDialogView)
        builder.setCancelable(false)
        var dialog = builder.create()
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)
        val slots = UnderscoreDigitSlotsParser().parseSlots("__.__.____ __:__")
        val mask = MaskImpl.createTerminated(slots)
        val watcher: FormatWatcher = MaskFormatWatcher(mask)
        watcher.installOn(mDialogView.date)
        mDialogView.title.text = String.format("Вы хотите отложить фильм %s \nв список \"Посмотреть позже\"",item.title)
        mDialogView.addBtn.setOnClickListener {
            if (!TextUtils.isEmpty(mDialogView.date.text)) {
                addReminder(item, mDialogView.date.text.toString())
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(),getString(R.string.not_empty_date),
                    Toast.LENGTH_SHORT).show()
            }
        }
        mDialogView.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun addReminder(movie: Movie, date : String) {
        val formatter: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm")
        val tag = Extensions.generateKey()
        val title = requireActivity().getString(R.string.notification_title)
        val text = String.format(requireActivity().getString(R.string.notification_text), movie.title)
        val alertTime = formatter.parseDateTime(date).millis - System.currentTimeMillis()
        val data = Extensions.createWorkInputData(title, text, movie.id)
        FilmWorker.scheduleReminder(alertTime, data, tag)
        pendingViewModel!!.addToPendings(movie.ToPending(formatter.parseDateTime(date).millis))
    }

}
