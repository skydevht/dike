package tech.skydev.dike

import android.content.Context
import tech.skydev.dike.data.ConstitutionRepository

/**
 * Created by Hash Skyd on 3/26/2017.
 */
object Injection {

    var constRepo: ConstitutionRepository? = null

    fun provideConstitutionRepository(context: Context): ConstitutionRepository {
        if (constRepo != null) {
            return constRepo!!
        } else {
            constRepo = ConstitutionRepository(context)
            return constRepo!!
        }
    }

}