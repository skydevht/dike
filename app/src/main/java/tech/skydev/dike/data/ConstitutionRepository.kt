package tech.skydev.dike.data

import android.content.Context
import android.os.AsyncTask
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.config.Constant
import tech.skydev.dike.data.model.*
import tech.skydev.dike.util.FileUtil
import java.lang.reflect.Type

/**
 * Created by Hash Skyd on 3/26/2017.
 */

class ConstitutionRepository(var context: Context) {


    var gson: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Titre::class.java, DikeJsonDeserialiser.TitreDeserialiser())
            .registerTypeAdapter(Chapter::class.java, DikeJsonDeserialiser.ChapterDeserialiser())
            .registerTypeAdapter(Section::class.java, DikeJsonDeserialiser.SectionDeserialiser())
            .create();
    val sectionsFile: String = Constant.CONST_1987_A_DIR + "/sections.json"

    fun allTitres(callback: DataCallback<ArrayList<Titre>>) {
        val task: AsyncTask<Void, Void, ArrayList<Titre>> = object : AsyncTask <Void, Void, ArrayList<Titre>>() {
            override fun doInBackground(vararg params: Void?): ArrayList<Titre> {
                var titres: ArrayList<Titre> = ArrayList(0)
                try {
                    val sectionGson: String = FileUtil.loadTextFileFromAssets(context, sectionsFile);
                    val collectionType: Type = (object : TypeToken<ArrayList<Titre>>() {}).getType()
                    titres = gson.fromJson(sectionGson, collectionType)

                } catch(e: Exception) {
                    callback.onError(e)
                } finally {
                    return titres
                };
            }

            override fun onPostExecute(result: ArrayList<Titre>?) {
                callback.onSuccess(result);
            }
        }

        task.execute()
    }
    fun getTitre(id: String, callback: DataCallback<Titre>) {
        val filename: String = Constant.CONST_1987_A_DIR + "/titres/titre_" + id + ".json"

        val task: AsyncTask<Void, Void, Titre?> = object : AsyncTask <Void, Void, Titre?>() {
            override fun doInBackground(vararg params: Void?): Titre? {
                try {
                    val sectionGson: String = FileUtil.loadTextFileFromAssets(context, filename);
                    val titre = gson.fromJson(sectionGson, Titre::class.java)
                    return titre

                } catch(e: Exception) {
                    callback.onError(e)
                }
                return null;
            }

            override fun onPostExecute(result: Titre?) {
                callback.onSuccess(result);
            }
        }
        task.execute()
    }

    fun getArticle(titreId: String, articleId: Int, callback: DataCallback<Article>) {
        getTitre(titreId, object : DataCallback<Titre> {

            override fun onSuccess(result: Titre?) {
                var resultArticle: Article? = null
                result?.chapters!!.flatMap { it.sections!! }.flatMap { it.articles!! }
                        .forEach {
                             if (it.id == articleId) {
                                 resultArticle = it
                             }
                        }
                callback.onSuccess(resultArticle)
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }

        })
    }
}
