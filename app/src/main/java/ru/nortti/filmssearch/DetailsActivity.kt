package ru.nortti.filmssearch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {

    var film: Film = Film()

    companion object {
        private const val TAG = "DetailsActivity"
        const val CHECKED = "is_checked"
        const val COMMENT = "comment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val intent = getIntent();
        film = intent.getParcelableExtra("film")!!
        val drawableResourceId: Int = getResources()
            .getIdentifier(film.image, "drawable", getPackageName())
        Glide
            .with(this)
            .load(drawableResourceId)
            .into(poster)


        titleTx.text = film.name
        descriptionTx.text = film.desctiption

        shareBut.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, film.name)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(CHECKED, likeCheckbox.isChecked)
        intent.putExtra(COMMENT,comment.text.toString())
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

}
