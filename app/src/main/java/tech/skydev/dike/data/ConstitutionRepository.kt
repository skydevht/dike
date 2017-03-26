package tech.skydev.dike.data

import android.content.Context
import android.os.AsyncTask
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.config.Constant
import tech.skydev.dike.util.FileUtil
import java.lang.reflect.Type
import java.util.ArrayList

/**
 * Created by Hash Skyd on 3/26/2017.
 */

class ConstitutionRepository(var context: Context) {


    var gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    val sectionsFile: String = Constant.CONST_1987_A_DIR + "/sections.json"

    fun allSections(callback: DataCallback<ArrayList<Section>>) {
        val task: AsyncTask<Void, Void, ArrayList<Section>> = object : AsyncTask <Void, Void, ArrayList<Section>> () {
            override fun doInBackground(vararg params: Void?): ArrayList<Section> {
                var sections: ArrayList<Section> = ArrayList(0)
                try {
                    val sectionGson: String = FileUtil.loadTextFileFromAssets(context, sectionsFile);
                    val collectionType: Type = (object : TypeToken<ArrayList<Section>>() {}).getType()
                    sections = gson.fromJson(sectionGson, collectionType)

                } catch(e: Exception) {
                    callback.onError(e)
                } finally {
                    return sections
                };
            }

            override fun onPostExecute(result: ArrayList<Section>?) {
                callback.onSuccess(result);
            }
        }
        task.execute();
    }
}
