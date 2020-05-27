package ru.nortti.filmssearch.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_pending.*

import ru.nortti.filmssearch.R
import ru.nortti.filmssearch.model.local.models.Pending
import ru.nortti.filmssearch.view.adapters.PendingAdapter
import ru.nortti.filmssearch.viewModel.viewModels.PendingViewModel

/**
 * A simple [Fragment] subclass.
 */
class PendingFragment : Fragment() {

    private var viewModel : PendingViewModel ? = null
    private var adapter : PendingAdapter ? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel =ViewModelProviders.of(activity!!).get(PendingViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel!!.pendingList.observe(this.viewLifecycleOwner, Observer<List<Pending>> {

           if  (it.isNotEmpty()) {
               adapter = PendingAdapter(it.toMutableList(), object : PendingAdapter.PendingCallback {

               })
               rvList.adapter = adapter
           } else {
               info.text = String.format(getString(R.string.list_is_empty), getString(R.string.watch_later))
           }
        })
    }

}
