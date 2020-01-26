package ru.nortti.filmssearch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val filmViewModel by lazy { ViewModelProviders.of(this).get(FilmViewModel::class.java)}

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adapter = FilmsAdapter(this)
        rvList.layoutManager = GridLayoutManager(this, 2)
        rvList.adapter = adapter

        filmViewModel.getListFilms().observe(this, Observer {
            it?.let {
                adapter.addFilms(it)
            }
        })

        adapter.setOnClickListener(object : FilmsAdapter.OnClickListener {
            override fun onClick(item: Film) {
                val intent = Intent(this@MainActivity, DetailsActivity::class.java).apply {
                    putExtra("film", item)
                }
                startActivityForResult(intent, RESULT_CODE)
            }

        })

    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, String.format("Is checked: %s",  data!!.getBooleanExtra(DetailsActivity.CHECKED, false)))
                Log.d(TAG, String.format("Comment: %s",  data!!.getStringExtra(DetailsActivity.COMMENT)))
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
