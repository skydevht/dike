package tech.skydev.dike.ui.article

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.base.BaseFragment
import tech.skydev.dike.data.model.Article
import tech.skydev.dike.util.ViewAnimationUtils
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
            mTitleId = arguments.getString(TITLE_ID_KEY, "")
            mArticleId = arguments.getInt(ARTICLE_ID_KEY, -1)
        } else {
            mTitleId = savedInstanceState.getString(TITLE_ID_KEY, "")
            mArticleId = savedInstanceState.getInt(ARTICLE_ID_KEY, -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_article, container, false)
        mArticleView = rootView.findViewById(R.id.article_view) as ArticleView
        mArticleView?.swipeListener = ArticleViewListener()
        mPreviousArticleView = rootView.findViewById(R.id.previous_article)
        mCurrentArticleView = rootView.findViewById(R.id.current_article)
        mNextArticleView = rootView.findViewById(R.id.next_article)
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
        if (mArticles != null) {
            for (article in mArticles!!) {
                if (article.id == mArticleId) {
                    mArticleView?.article = article
                    break
                }
            }
        }
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
                view.isClickable = false
            } else {
                view.visibility = View.VISIBLE // restore view visibility
                val textView = view.findViewById(R.id.name) as TextView

                val darken = (i == 1)
                if (mClickeIndicatorId == -1 && i == 1) {
                    ViewAnimationUtils.changeCellColor(view, !darken)
                }
                if (mClickeIndicatorId == i || (i == 1 && mClickeIndicatorId != -1)) {
                    // animate only the clicked object
                    ViewAnimationUtils.changeCellColor(view, darken, object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {}

                        override fun onAnimationEnd(animation: Animator?) {
                            ViewAnimationUtils.changeCellColor(view, !darken)
                            mClickeIndicatorId = -1
                            textView.text = article.order
                            view.setOnClickListener {
                                mClickeIndicatorId = i
                                mPresenter.loadArticle(mTitleId, article.id)
                            }

                        }

                        override fun onAnimationCancel(animation: Animator?) {}

                        override fun onAnimationStart(animation: Animator?) {}
                    })
                } else {
                    textView.text = article.order
                    view.setOnClickListener {
                        mClickeIndicatorId = i
                        mPresenter.loadArticle(mTitleId, article.id)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(ARTICLE_ID_KEY, mArticleId)
        outState?.putString(TITLE_ID_KEY, mTitleId)
    }

    companion object {
        val TAG: String = "article-fragment"
        val ARTICLE_ID_KEY: String = "article_id"
        val TITLE_ID_KEY: String = "title_id"

        fun newInstance(titleId: String, articleId: Int): ArticleFragment {
            val bundle: Bundle = Bundle()
            val fragment = ArticleFragment()
            bundle.putInt(ARTICLE_ID_KEY, articleId)
            bundle.putString(TITLE_ID_KEY, titleId)
            fragment.arguments = bundle
            return fragment;
        }
    }

    inner class ArticleViewListener: ArticleView.OnSwipeListener {
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
                if (mArticleId >= mArticles!!.size) mArticleId = mArticles!!.size
                showCurrentArticle()
            }
        }
    }
}