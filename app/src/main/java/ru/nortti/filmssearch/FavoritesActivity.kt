package ru.nortti.filmssearch

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.activity_main.*

class FavoritesActivity : AppCompatActivity() {
    lateinit var prefs: SharedPreference
    private var cells = 2

    companion object {
        private const val TAG = "FavoritesActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        prefs = SharedPreference(this)

        var adapter = FilmsAdapter(this, prefs, true)
        cells = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> throw IllegalStateException()
        }
        rvList.layoutManager = GridLayoutManager(this, cells)
        rvList.adapter = adapter
        adapter.addFilms(prefs.getFavorites())
        adapter.setOnClickListener(object : FilmsAdapter.OnClickListener {
            override fun onClick(item: Film) {
                val intent = Intent(this@FavoritesActivity, DetailsActivity::class.java).apply {
                    putExtra("film", item)
                }
                startActivityForResult(intent, RESULT_CODE)
            }

        }
        )
    }
}
