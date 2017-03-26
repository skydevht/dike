package tech.skydev.dike.ui.section

import tech.skydev.dike.data.ConstitutionRepository
import tech.skydev.dike.data.DataCallback
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
class SectionPresenter(val constitutionRepository: ConstitutionRepository,
                           val sectionView: SectionContract.View): SectionContract.Presenter {
    override fun start() {
        loadSections()
    }

    init {
        sectionView.setPresenter(this)
    }


    override fun loadSections() {
        constitutionRepository.allTitres(object : DataCallback<ArrayList<Titre>> {
            override fun onSuccess(result: ArrayList<Titre>?) {
                sectionView.showSections(result ?: ArrayList<Titre>(0))
            }

            override fun onError(t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}