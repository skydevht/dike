package tech.skydev.dike.ui.title

import tech.skydev.dike.data.ConstitutionRepository
import tech.skydev.dike.data.DataCallback
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
class TitlesPresenter(val constitutionRepository: ConstitutionRepository,
                      val titlesView: TitlesContract.View): TitlesContract.Presenter {
    override fun start() {
        loadSections()
    }

    init {
        titlesView.setPresenter(this)
    }


    override fun loadSections() {
        constitutionRepository.allTitres(object : DataCallback<ArrayList<Titre>> {
            override fun onSuccess(result: ArrayList<Titre>?) {
                titlesView.showTitles(result ?: ArrayList<Titre>(0))
            }

            override fun onError(t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}