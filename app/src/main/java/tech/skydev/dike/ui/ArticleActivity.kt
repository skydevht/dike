package tech.skydev.dike.ui

import android.os.Bundle
import android.widget.Button
import com.mukesh.MarkdownView
import org.parceler.Parcels
import tech.skydev.dike.R
import tech.skydev.dike.model.Content

class ArticleActivity : BaseActivity() {

    lateinit var articleView: MarkdownView
    lateinit var prevButton: Button
    lateinit var nextButton: Button

    var articles: ArrayList<Content>? = null
    var article_id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        articleView = findViewById(R.id.article_view) as MarkdownView
        articleView.settings.builtInZoomControls = true
        articleView.settings.displayZoomControls = false

        prevButton = findViewById(R.id.previous) as Button
        nextButton = findViewById(R.id.next) as Button

        prevButton.setOnClickListener {
            if (article_id > 0) article_id--
            if (article_id == 0) prevButton.isEnabled = false
            if (!nextButton.isEnabled) nextButton.isEnabled = true
            loadArticle()
        }
        nextButton.setOnClickListener {
            if (article_id < articles?.size ?: -2) article_id++
            if (article_id == (articles?.size ?: -1) - 1) nextButton.isEnabled = false
            if (!prevButton.isEnabled) prevButton.isEnabled = true
            loadArticle()
        }


        article_id = if (savedInstanceState == null) intent.getIntExtra(ART_ID_KEY, -1) else savedInstanceState.getInt(ART_ID_KEY, -1)
        articles = Parcels.unwrap(if (savedInstanceState == null) intent.getParcelableExtra(ART_KEY) else savedInstanceState.getParcelable(ART_KEY))

        if (articles == null) {
            prevButton.isEnabled = false
            nextButton.isEnabled = false
        } else {
            if (article_id <= 0)
                prevButton.isEnabled = false
            else if (article_id >= (articles?.size ?: -1) - 1)
                nextButton.isEnabled = false
        }
    }

    override fun onStart() {
        super.onStart()
        loadArticle()
    }

    private fun loadArticle() {
        if (article_id > -1 && articles != null)
            title = articles?.get(article_id)?.name
        articleView.loadMarkdownFromAssets(articles?.get(article_id)?.path)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(ART_ID_KEY, article_id)
        outState?.putParcelable(ART_KEY, Parcels.wrap(articles))
    }

    companion object {
        val ART_KEY = "articles_key"
        val ART_ID_KEY = "article_id_key"
    }
}
