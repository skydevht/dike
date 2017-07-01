package tech.skydev.dike.ui

import android.os.Bundle
import com.mukesh.MarkdownView
import tech.skydev.dike.R

class ArticleActivity : BaseActivity() {

    lateinit var articleView: MarkdownView


    var article: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        articleView = findViewById(R.id.article_view) as MarkdownView
        articleView.settings.builtInZoomControls = true
        articleView.settings.displayZoomControls = false

        article = if (savedInstanceState == null) intent.getStringExtra(DocumentActivity.ID_KEY) else savedInstanceState.getString(DocumentActivity.ID_KEY)
    }

    override fun onStart() {
        super.onStart()
        if (!article.isNullOrBlank())
            articleView.loadMarkdownFromAssets(article)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(PATH_KEY, article)
    }

    companion object {
        val PATH_KEY = "path_key"
    }
}
