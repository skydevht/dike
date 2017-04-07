package tech.skydev.dike.ui.article

import tech.skydev.dike.BasePresenter
import tech.skydev.dike.BaseView
import tech.skydev.dike.data.model.Article

/**
 * Created by Hash Skyd on 3/27/2017.
 */
interface ArticleContract {
    interface View: BaseView<Presenter> {
        fun showArticle(article: Article)
        fun updateNavigation(articles: Array<Article?>)
        fun setArticles(articles: ArrayList<Article>)
    }
    interface Presenter: BasePresenter {
        fun loadArticle(titleId: String, articleId: Int)
        fun loadArticles(titleId: String)
    }
}