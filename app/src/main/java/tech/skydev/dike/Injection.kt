package tech.skydev.dike

import android.content.Context
import tech.skydev.dike.data.ConstitutionRepository

/**
 * Created by Hash Skyd on 3/26/2017.
 */
object Injection {
    fun provideConstitutionRepository(context: Context): ConstitutionRepository {
        return ConstitutionRepository(context)
    }

}