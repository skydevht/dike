package tech.skydev.dike.ui.article

import tech.skydev.dike.data.ConstitutionRepository
import tech.skydev.dike.data.DataCallback
import tech.skydev.dike.data.model.Article

/**
 * Created by Hash Skyd on 3/28/2017.
 */
class ArticlePresenter(val constitutionRepository: ConstitutionRepository, val articleView: ArticleContract.View): ArticleContract.Presenter {

    init {
        articleView.setPresenter(this)
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadArticle(titleId: String, articleId: Int) {
        constitutionRepository.getArticle(titleId, articleId, object: DataCallback<Array<Article?>> {
            override fun onSuccess(result: Array<Article?>) {
                articleView.showArticle(result[1]!!)
                articleView.updateNavigation(result);
            }

            override fun onError(t: Throwable) {
            }

        })
    }

    override fun loadArticles(titleId: String) {
        constitutionRepository.getArticles(titleId, object : DataCallback<ArrayList<Article>> {
            override fun onSuccess(result: ArrayList<Article>) {
                articleView.setArticles(result)
            }

            override fun onError(t: Throwable) {
            }

        })
    }
}