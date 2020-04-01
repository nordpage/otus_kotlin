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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.nortti.filmssearch.Constants.LANGUAGE
import ru.nortti.filmssearch.Constants.THEME
import ru.nortti.filmssearch.Constants.setAppTheme
import ru.nortti.filmssearch.Constants.setLocale
import ru.nortti.filmssearch.fragments.FavoriteFragment
import ru.nortti.filmssearch.fragments.MainFragment
import ru.nortti.filmssearch.fragments.SettingsFragment
import ru.nortti.filmssearch.utils.ItemAnimator
import ru.nortti.filmssearch.utils.ItemOffsetDecoration

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val RESULT_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(MainFragment())
        navigationView.inflateMenu(R.menu.bottom_menu)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.films-> {
                    loadFragment(MainFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.favorites-> {
                    loadFragment(FavoriteFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.settings-> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

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

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        return when (item.itemId) {
//            R.id.favs -> {
//                startActivity(Intent(this, FavoritesActivity::class.java))
//                return true
//            }
//            R.id.language -> {
//                if (prefs.getValueString(LANGUAGE).equals("RU")) {
//                    prefs.save(LANGUAGE, "EN")
//
//                } else {
//                    prefs.save(LANGUAGE, "RU")
//                }
//                this.recreate()
//                return true
//            }
//            R.id.theme -> {
//                if (prefs.getValueString(THEME).equals("DAY")) {
//                    prefs.save(THEME, "NIGHT")
//                } else {
//                    prefs.save(THEME, "DAY")
//                }
//                this.recreate()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

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

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}


