package tech.skydev.dike.ui.titledetails

import tech.skydev.dike.data.ConstitutionRepository
import tech.skydev.dike.data.DataCallback
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */
class TitleDetailsPresenter(val constitutionRepository: ConstitutionRepository,
                            val titleDetailsView: TitleDetailsContract.View): TitleDetailsContract.Presenter {


    override fun start() {
        loadTitles()
    }

    init {
        titleDetailsView.setPresenter(this)
    }


    override fun loadTitle(titleId: String) {
        constitutionRepository.getTitre(titleId, object : DataCallback<Titre> {
            override fun onSuccess(result: Titre) {
                titleDetailsView.showTitle(result)
            }

            override fun onError(t: Throwable) {
                titleDetailsView.showMessage("Erreur lors du chargement des sections")
            }
        })
    }

    override fun loadTitles() {
        constitutionRepository.allTitres(object : DataCallback<ArrayList<Titre>> {
            override fun onSuccess(result: ArrayList<Titre>) {
                titleDetailsView.showTitles(result)
            }

            override fun onError(t: Throwable) {
                titleDetailsView.showMessage("Erreur lors du chargement des titres")
            }
        })
    }
}