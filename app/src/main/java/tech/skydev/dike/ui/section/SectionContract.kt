package tech.skydev.dike.ui.section

import tech.skydev.dike.BasePresenter
import tech.skydev.dike.BaseView
import tech.skydev.dike.data.Section

/**
 * Created by Hash Skyd on 3/26/2017.
 */
interface SectionContract {
    interface View: BaseView<Presenter> {
        fun showSections(sections: ArrayList<Section>)
    }
    interface Presenter: BasePresenter {
        fun loadSections()
    }
}