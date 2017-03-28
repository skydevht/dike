package tech.skydev.dike.ui.article

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.base.BaseFragment
import tech.skydev.dike.data.model.Article

/**
 * Created by Hash Skyd on 3/27/2017.
 */
class ArticleFragment(): BaseFragment(), ArticleContract.View {



    private var mArticleNameTV: TextView? = null
    private var mArticleTextTV: TextView? = null

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
        val rootView = inflater!!.inflate(R.layout.fragment_article, container, false )
        mArticleNameTV = rootView.findViewById(R.id.article_name) as TextView
        mArticleTextTV = rootView.findViewById(R.id.article_text) as TextView
        return rootView
    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadArticle(mTitleId, mArticleId)
    }

    lateinit var mPresenter: ArticleContract.Presenter

    override fun setPresenter(presenter: ArticleContract.Presenter) {
        mPresenter = presenter;
    }

    override fun showArticle(article: Article) {
        mArticleNameTV?.text = "Article ${article.order}"
        mArticleTextTV?.text = Html.fromHtml(article.text)
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

}