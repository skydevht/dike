package tech.skydev.dike.data

import android.content.Context
import android.os.AsyncTask
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.config.Constant
import tech.skydev.dike.data.exception.DataNotFoundException
import tech.skydev.dike.data.model.*
import tech.skydev.dike.util.FileUtil
import java.lang.reflect.Type

/**
 * Created by Hash Skyd on 3/26/2017.
 */

class ConstitutionRepository(var context: Context) {

    var titres : HashMap<String, Titre> = HashMap()

    fun getTitreFromCache(titreId: String): Titre? {
        return titres[titreId]
    }

    fun addTitreToCache(titre: Titre) {
        titres.put(titre.id!!, titre)
    }


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
                if (result == null) {
                    callback.onError(DataNotFoundException())
                } else {
                    callback.onSuccess(result)
                }
            }
        }

        task.execute()
    }

    fun getTitre(id: String, callback: DataCallback<Titre>) {
        val titre = getTitreFromCache(id)
        if (titre == null) {
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
                    if (result == null) {
                        callback.onError(DataNotFoundException())
                    } else {
                        addTitreToCache(result)
                        callback.onSuccess(result)
                    }
                }
            }
            task.execute()
        } else {
            callback.onSuccess(titre)
        }


    }

    fun getArticles(titreId: String, callback: DataCallback<ArrayList<Article>>) {
        getTitre(titreId, object : DataCallback<Titre> {

            override fun onSuccess(result: Titre) {
                val resultArticles = result.chapters!!.flatMap { it.sections!! }.flatMap { it.articles!! } as ArrayList
                if (resultArticles == null) {
                    callback.onError(DataNotFoundException())
                } else {
                    callback.onSuccess(resultArticles)
                }
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }

        })
    }


    fun getArticle(titreId: String, articleId: Int, callback: DataCallback<Array<Article?>>) {
        getTitre(titreId, object : DataCallback<Titre> {

            override fun onSuccess(result: Titre) {
                val resultArticles: Array<Article?> = arrayOfNulls(3)
                result?.chapters!!.flatMap { it.sections!! }.flatMap { it.articles!! }
                        .forEach {
                            if (it.id == articleId - 1) {
                                resultArticles[0] = it //previous article
                            } else if (it.id == articleId) {
                                resultArticles[1] = it // the current article
                            } else if (it.id == articleId + 1) {
                                resultArticles[2] = it // next article
                            }
                        }
                if (resultArticles[1] == null) {
                    callback.onError(DataNotFoundException())
                } else {
                    callback.onSuccess(resultArticles)
                }
            }

            override fun onError(t: Throwable) {
                callback.onError(t)
            }

        })
    }
}
