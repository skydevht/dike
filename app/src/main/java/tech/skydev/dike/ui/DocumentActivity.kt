package tech.skydev.dike.ui

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.parceler.Parcels
import tech.skydev.dike.R
import tech.skydev.dike.model.Document
import tech.skydev.dike.model.Section
import tech.skydev.dike.util.FileUtil
import timber.log.Timber
import java.io.IOException

class DocumentActivity : AppCompatActivity() {

    lateinit var recycler: RecyclerView
    val adapter = TocAdapter()
    var path: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        path = if (savedInstanceState == null) intent.getStringExtra(PATH_KEY) else savedInstanceState.getString(PATH_KEY)
    }

    override fun onStart() {
        super.onStart()
        LoadDocumentTask().execute(path)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(PATH_KEY, path)
    }

    companion object {
        val PATH_KEY = "path_key"
    }

    inner class LoadDocumentTask : AsyncTask<String, Void, Document?>() {
        var gson: Gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        override fun doInBackground(vararg params: String?): Document? {
            val docPath = if (params.isNotEmpty()) params[0] else null
            if (!docPath.isNullOrBlank()) {
                try {
                    val toc = FileUtil.loadTextFileFromAssets(this@DocumentActivity, "${docPath}/toc.json")
                    val document = gson.fromJson(toc, Document::class.java)
                    return document
                } catch (ex : IOException) {
                    Timber.e(ex, "Error loading the toc at ${docPath}")
                    return null
                }
            } else
                return null
        }

        override fun onPostExecute(result: Document?) {
            if (result != null && result.sections != null) {
                adapter.models = result.sections!! as ArrayList<Section>
                adapter.path = path?:""
                adapter.notifyDataSetChanged()
                title = result.name
            }
        }
    }

    class TocAdapter(var models: ArrayList<Section> = ArrayList()) : RecyclerView.Adapter<TocAdapter.ViewHolder>() {

        override fun getItemCount(): Int {
            return models.size
        }

        var path = ""

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = models[position]
            holder.nameView.text = model.name
            holder.titleView.text = model.type
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, SectionActivity::class.java)
                intent.putExtra(SectionActivity.PATH_KEY, path)
                intent.putExtra(SectionActivity.SECTION_KEY, Parcels.wrap(model))
                it.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            return ViewHolder(inflater.inflate(R.layout.row_main_section, parent, false))
        }


        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameView: TextView
            var titleView: TextView

            init {
                nameView = itemView.findViewById(R.id.section_name) as TextView
                titleView = itemView.findViewById(R.id.section_title) as TextView
            }

        }
    }
}
