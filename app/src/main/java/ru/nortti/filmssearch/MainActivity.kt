package ru.nortti.filmssearch

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.nortti.filmssearch.Constants.LANGUAGE
import ru.nortti.filmssearch.Constants.THEME
import ru.nortti.filmssearch.Constants.setAppTheme
import ru.nortti.filmssearch.Constants.setLocale
import ru.nortti.filmssearch.utils.ItemAnimator
import ru.nortti.filmssearch.utils.ItemOffsetDecoration

class MainActivity : AppCompatActivity() {
    lateinit var adapter: FilmsAdapter
    lateinit var prefs: SharedPreference
    private val filmViewModel by lazy { ViewModelProviders.of(this).get(FilmViewModel::class.java) }
    private var cells = 2

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = SharedPreference(this)

        adapter = FilmsAdapter(this, prefs, false)
        cells = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> throw IllegalStateException()
        }
        var layoutManager = GridLayoutManager(this, cells)
        rvList.layoutManager = layoutManager
        rvList.adapter = adapter
        rvList.addItemDecoration(ItemOffsetDecoration(20))
        rvList.itemAnimator = ItemAnimator(this)

        filmViewModel.getListFilms().observe(this, Observer {
            it?.let {
                initAdapter(it)
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

    private fun initAdapter(list: List<Film>) {
        list.forEach {
            adapter.addFilm(it)
        }
    }

    override fun onResume() {
        super.onResume()
        setLocale(this, prefs.getValueString(LANGUAGE)!!)
        setAppTheme(prefs.getValueString(THEME)!!)
        adapter.notifyDataSetChanged()
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
                Log.d(
                    TAG,
                    String.format(
                        "Is checked: %s",
                        data!!.getBooleanExtra(DetailsActivity.CHECKED, false)
                    )
                )
                Log.d(
                    TAG,
                    String.format("Comment: %s", data!!.getStringExtra(DetailsActivity.COMMENT))
                )
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.favs -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
                return true
            }
            R.id.language -> {
                if (prefs.getValueString(LANGUAGE).equals("RU")) {
                    prefs.save(LANGUAGE, "EN")

                } else {
                    prefs.save(LANGUAGE, "RU")
                }
                this.recreate()
                return true
            }
            R.id.theme -> {
                if (prefs.getValueString(THEME).equals("DAY")) {
                    prefs.save(THEME, "NIGHT")
                } else {
                    prefs.save(THEME, "DAY")
                }
                this.recreate()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)

        // Set the alert dialog title
        builder.setTitle(resources.getString(R.string.exit))

        // Display a message on alert dialog
        builder.setMessage(resources.getString(R.string.want_exit))

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
            finish()

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton(resources.getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

}


