package tech.skydev.dike.ui

import android.os.Bundle
import android.view.MotionEvent
import org.parceler.Parcels
import tech.skydev.dike.R
import tech.skydev.dike.markdownview.MarkdownView
import tech.skydev.dike.model.Content
import tech.skydev.dike.util.SimpleGestureFilter


class ArticleActivity : BaseActivity(), SimpleGestureFilter.SimpleGestureListener {

    lateinit var articleView: MarkdownView

    var articles: ArrayList<Content>? = null
    var article_id: Int = -1

    private var detector: SimpleGestureFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        articleView = findViewById(R.id.article_view) as MarkdownView
        articleView.settings.builtInZoomControls = true
        articleView.settings.displayZoomControls = false


        article_id = if (savedInstanceState == null) intent.getIntExtra(ART_ID_KEY, -1) else savedInstanceState.getInt(ART_ID_KEY, -1)
        articles = Parcels.unwrap(if (savedInstanceState == null) intent.getParcelableExtra(ART_KEY) else savedInstanceState.getParcelable(ART_KEY))

        detector = SimpleGestureFilter(this, this)
        detector?.mode = SimpleGestureFilter.MODE_TRANSPARENT
    }

    override fun dispatchTouchEvent(me: MotionEvent): Boolean {
        this.detector?.onTouchEvent(me)
        return super.dispatchTouchEvent(me)
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

    override fun onSwipe(direction: Int) {
        when (direction) {
            SimpleGestureFilter.SWIPE_LEFT -> {
                if (article_id < articles!!.size -1) {
                    article_id++
                    loadArticle()
                }
            }
            SimpleGestureFilter.SWIPE_RIGHT -> {
                if (article_id > 0) {
                    article_id--
                    loadArticle()
                }
            }
        }
    }

    override fun onDoubleTap() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val ART_KEY = "articles_key"
        val ART_ID_KEY = "article_id_key"
    }
}
