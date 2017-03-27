package tech.skydev.dike.ui.titledetails

import tech.skydev.dike.BasePresenter
import tech.skydev.dike.BaseView
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface TitleDetailsContract {
    interface View: BaseView<Presenter> {
        fun showTitle(titre: Titre)
        fun showTitles(titres: ArrayList<Titre>)
    }
    interface Presenter: BasePresenter {
        fun loadTitle()
        fun loadTitles()
    }
}