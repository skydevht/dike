package tech.skydev.dike.service

import android.content.Context
import android.os.AsyncTask
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.config.Constant
import tech.skydev.dike.model.Document
import tech.skydev.dike.util.FileUtil
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by hash on 6/27/17.
 */
class DocumentService(val context: Context) {

    var gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    val documents = ArrayList<Document>()

    fun loadAllDocuments(callback: Callback<List<Document>>?) {
        if (documents.isEmpty())
            object : AsyncTask<Void, Void, List<Document>>() {
                override fun doInBackground(vararg params: Void?): List<Document> {
                    val documentsJson = FileUtil.loadTextFileFromAssets(context, Constant.DOCUMENTS_LIST)
                    val collectionType: Type = (object : TypeToken<ArrayList<Document>>() {}).getType()
                    val documents = gson.fromJson<List<Document>>(documentsJson, collectionType)
                    return documents
                }

                override fun onPostExecute(result: List<Document>) {
                    super.onPostExecute(result)
                    documents.addAll(result)
                    callback?.onSuccess(result)
                }

            }.execute()
        else callback?.onSuccess(documents)
    }

    fun loadDocument(id: String, callback: Callback<Document>?) {
        if (documents.isEmpty()) loadAllDocuments(null)
        val document = documents.filter { it.id == id }.firstOrNull()
        if (document != null)
            object : AsyncTask<String, Void, Document?>() {

                override fun doInBackground(vararg params: String?): Document? {
                    val docPath = if (params.isNotEmpty()) params[0] else null
                    if (!docPath.isNullOrBlank()) {
                        try {
                            val toc = FileUtil.loadTextFileFromAssets(context, "${docPath}/toc.json")
                            val doc = gson.fromJson(toc, Document::class.java)
                            return doc
                        } catch (ex: IOException) {
                            Timber.e(ex, "Error loading the toc at ${docPath}")
                            return null
                        }
                    } else
                        return null
                }

                override fun onPostExecute(result: Document?) {
                    if (result != null && result.sections != null) {
                        result.id = document.id
                        result.path = document.path
                        callback?.onSuccess(result)
                    }
                }
            }.execute(document.path)
        else callback?.onError(Exception()) // TODO Explicit Exception
    }

    interface Callback<T> {
        fun onSuccess(result: T)
        fun onError(ex: Exception)
    }
}