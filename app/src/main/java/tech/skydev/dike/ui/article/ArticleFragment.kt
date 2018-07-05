package tech.skydev.dike.ui.article

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.base.BaseFragment
import tech.skydev.dike.data.model.Article
import tech.skydev.dike.widget.ArticleView

/**
 * Created by Hash Skyd on 3/27/2017.
 */
class ArticleFragment() : BaseFragment(), ArticleContract.View {


    private var mArticleView: ArticleView? = null
    private var mPreviousArticleView: View? = null
    private var mCurrentArticleView: View? = null
    private var mNextArticleView: View? = null

    private var mArticles: ArrayList<Article>? = null

    private var mArticleId: Int = -1

    lateinit private var mTitleId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mTitleId = arguments!!.getString(TITLE_ID_KEY, "")
            mArticleId = arguments!!.getInt(ARTICLE_ID_KEY, -1)
        } else {
            mTitleId = savedInstanceState.getString(TITLE_ID_KEY, "")
            mArticleId = savedInstanceState.getInt(ARTICLE_ID_KEY, -1)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_article, container, false)
        mArticleView = rootView.findViewById(R.id.article_view) as ArticleView
        mArticleView?.swipeListener = ArticleViewListener()
        mPreviousArticleView = rootView.findViewById(R.id.previous_article)
        mPreviousArticleView?.setOnClickListener {
            if (mArticles != null) {
                mArticleId--
                if (mArticleId <= 0) mArticleId = 1
                showCurrentArticle()
            }
        }
        mCurrentArticleView = rootView.findViewById(R.id.current_article)
        mNextArticleView = rootView.findViewById(R.id.next_article)
        mNextArticleView?.setOnClickListener {
            if (mArticles != null) {
                mArticleId++
                if (mArticleId >= mArticles!!.size) mArticleId = mArticles!!.size
                showCurrentArticle()
            }
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadArticles(mTitleId)
    }

    override fun setArticles(articles: ArrayList<Article>) {
        mArticles = articles
        showCurrentArticle()
    }

    fun showCurrentArticle() {
        val articles: Array<Article?> = arrayOfNulls(3)
        if (mArticles != null) {
            for (article in mArticles!!) {
                if (article.id == mArticleId) {
                    mArticleView?.article = article
                    val name = if (article.order == "0") "Préambule" else "Article ${article.order}"
                    (activity as AppCompatActivity).supportActionBar?.title = name
                    (activity as AppCompatActivity).supportActionBar?.subtitle = null
                    articles[1] = article
                } else if (article.id == mArticleId - 1) {
                    articles[0] = article
                } else if (article.id == mArticleId + 1) {
                    articles[2] = article
                }
            }
        }
        updateNavigation(articles)
    }

    lateinit var mPresenter: ArticleContract.Presenter

    override fun setPresenter(presenter: ArticleContract.Presenter) {
        mPresenter = presenter;
    }

    override fun showArticle(article: Article) {
        mArticleView?.article = article
        mArticleId = article.id
    }

    var mClickeIndicatorId: Int = -1

    override fun updateNavigation(articles: Array<Article?>) {
        for (i in 0..2) {
            val article: Article? = articles[i]
            val view: View = when (i) {
                0 -> mPreviousArticleView
                1 -> mCurrentArticleView
                2 -> mNextArticleView
                else -> View(context)
            }!!
            if (article == null) {
                // current article is first or last
                view.visibility = View.INVISIBLE
            } else {
                view.visibility = View.VISIBLE // restore view visibility
                val textView = view.findViewById(R.id.name) as TextView
                textView.text = article.order
                if (i == 1) {
                    // current View
                    view.setBackgroundColor(context!!.resources.getColor(R.color.colorAccent))
                    textView.setTextColor(context!!.resources.getColor(R.color.colorPrimaryDark))
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARTICLE_ID_KEY, mArticleId)
        outState.putString(TITLE_ID_KEY, mTitleId)
    }

    companion object {
        const val TAG: String = "article-fragment"
        const val ARTICLE_ID_KEY: String = "article_id"
        const val TITLE_ID_KEY: String = "title_id"

        fun newInstance(titleId: String, articleId: Int): ArticleFragment {
            val bundle = Bundle()
            val fragment = ArticleFragment()
            bundle.putInt(ARTICLE_ID_KEY, articleId)
            bundle.putString(TITLE_ID_KEY, titleId)
            fragment.arguments = bundle
            return fragment
        }
    }

    inner class ArticleViewListener : ArticleView.OnSwipeListener {
        override fun onSwipeLeft() {
            if (mArticles != null) {
                mArticleId++
                if (mArticleId >= mArticles!!.size) mArticleId = mArticles!!.size
                showCurrentArticle()
            }
        }

        override fun onSwipeRight() {
            if (mArticles != null) {
                mArticleId--
                if (mArticleId <= 0) mArticleId = 1
                showCurrentArticle()
            }
        }
    }
}