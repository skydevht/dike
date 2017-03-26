package tech.skydev.dike.data

import android.content.Context
import android.os.AsyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.util.FileUtil
import java.lang.reflect.Type

/**
 * Created by Hash Skyd on 3/26/2017.
 */
abstract class ReadJsonFromAssetsTask<T>
constructor(val context: Context,
            val gson: Gson,
            val filename: String,
            val callback: DataCallback<T>,
            val collection: Boolean) : AsyncTask<Void, Void, T?>() {

    override fun doInBackground(vararg params: Void?): T? {
        var model: T? = null
        try {
            val sectionGson: String = FileUtil.loadTextFileFromAssets(context, filename);
            if (collection) {
                val collectionType: Type = (object : TypeToken<ArrayList<T>>() {}).getType()
                model = gson.fromJson(sectionGson, collectionType)
            } else {
                model = gson.fromJson(sectionGson, getJsonType())
            }

        } catch(e: Exception) {
            callback.onError(e)
        } finally {
            return model
        };
    }

    abstract fun getJsonType(): Class<T>

    override fun onPostExecute(result: T?) {
        callback.onSuccess(result);
    }
}