package tech.skydev.dike

import android.content.Context
import tech.skydev.dike.service.DocumentService

/**
 * Created by hash on 6/27/17.
 */
object Injector {

    var documentService: DocumentService? = null

    fun setup(context: Context) {
        documentService = DocumentService(context)
    }
}