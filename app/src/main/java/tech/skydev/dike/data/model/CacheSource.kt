package tech.skydev.dike.data.model

/**
 * Created by Hash Skyd on 4/4/2017.
 */
class CacheSource {
    var titres : HashMap<String, Titre> = HashMap()

    fun getTitre(titreId: String): Titre? {
        return titres[titreId]
    }

    fun setTitre(titre: Titre) {
        titres.put(titre.id!!, titre)
    }
}