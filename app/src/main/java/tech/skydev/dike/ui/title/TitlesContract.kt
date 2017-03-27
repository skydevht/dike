package tech.skydev.dike.ui.title

import tech.skydev.dike.BasePresenter
import tech.skydev.dike.BaseView
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface TitlesContract {
    interface View: BaseView<Presenter> {
        fun showTitles(titres: ArrayList<Titre>)
    }
    interface Presenter: BasePresenter {
        fun loadSections()
    }
}