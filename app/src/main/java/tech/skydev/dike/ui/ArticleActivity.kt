package tech.skydev.dike.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mukesh.MarkdownView
import tech.skydev.dike.R

class ArticleActivity : AppCompatActivity() {

    lateinit var articleView: MarkdownView


    var path: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        articleView = findViewById(R.id.article_view) as MarkdownView
        articleView.settings.builtInZoomControls = true
        articleView.settings.displayZoomControls = false

        path = if (savedInstanceState == null) intent.getStringExtra(DocumentActivity.PATH_KEY) else savedInstanceState.getString(DocumentActivity.PATH_KEY)
    }

    override fun onStart() {
        super.onStart()
        if (!path.isNullOrBlank())
            articleView.loadMarkdownFromAssets(path)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(PATH_KEY, path)
    }

    companion object {
        val PATH_KEY = "path_key"
    }
}
