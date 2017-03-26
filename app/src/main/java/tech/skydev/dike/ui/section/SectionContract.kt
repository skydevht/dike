package tech.skydev.dike.ui.section

import tech.skydev.dike.BasePresenter
import tech.skydev.dike.BaseView
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface SectionContract {
    interface View: BaseView<Presenter> {
        fun showSections(titres: ArrayList<Titre>)
    }
    interface Presenter: BasePresenter {
        fun loadSections()
    }
}