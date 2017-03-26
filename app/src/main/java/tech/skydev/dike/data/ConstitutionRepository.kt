package tech.skydev.dike.data

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import tech.skydev.dike.config.Constant
import tech.skydev.dike.data.model.Chapter
import tech.skydev.dike.data.model.DikeJsonDeserialiser
import tech.skydev.dike.data.model.Section
import tech.skydev.dike.data.model.Titre

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
        val task: ReadJsonFromAssetsTask<ArrayList<Titre>>  = object : ReadJsonFromAssetsTask<ArrayList<Titre>>(
                context, gson, sectionsFile, callback, true
        ) {
            override fun getJsonType(): Class<ArrayList<Titre>> {
                //Will not be called if the data is an array
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        task.execute();
    }

    fun getTitre(id: String, callback: DataCallback<Titre>) {
        val filename: String = Constant.CONST_1987_A_DIR+"/titres/titre_"+id+".json"

        val task: ReadJsonFromAssetsTask<Titre>  = object : ReadJsonFromAssetsTask<Titre>(
                context, gson, filename, callback, false
        ) {
            override fun getJsonType(): Class<Titre> {
                return Titre::class.java
            }

        }
        task.execute();

    }
}
