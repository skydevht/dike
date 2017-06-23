package tech.skydev.dike.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tech.skydev.dike.R
import tech.skydev.dike.config.Constant
import tech.skydev.dike.model.Document
import tech.skydev.dike.util.FileUtil
import java.lang.reflect.Type

class BookShelfActivity : AppCompatActivity() {

    lateinit var recycler : RecyclerView
    val adapter = Adapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_shelf)
        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        LoadDocumentsTask().execute()
    }

    inner class LoadDocumentsTask: AsyncTask<Void, Void, List<Document>>() {
        var gson: Gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()


        override fun doInBackground(vararg params: Void?): List<Document> {
            val documentsJson = FileUtil.loadTextFileFromAssets(this@BookShelfActivity, Constant.DOCUMENTS_LIST)
            val collectionType: Type = (object : TypeToken<ArrayList<Document>>() {}).getType()
            val documents = gson.fromJson<List<Document>>(documentsJson, collectionType)
            return documents
        }

        override fun onPostExecute(result: List<Document>) {
            super.onPostExecute(result)
            adapter.models = result as ArrayList<Document>
            adapter.notifyDataSetChanged()
        }

    }

    class Adapter(val context: Context, var models : ArrayList<Document> = ArrayList()): RecyclerView.Adapter<Adapter.ViewHolder>() {

        override fun getItemCount(): Int {
            return models.size
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            return ViewHolder(inflater.inflate(R.layout.row_bookshelf_document, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = models.get(position)
            holder.nameTV.text = model.name
            val coverStream = context.assets.open("${model.path}/cover.jpg")
            val coverDrawable = Drawable.createFromStream(coverStream, null)
            holder.coverIV.setImageDrawable(coverDrawable)
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, DocumentActivity::class.java)
                intent.putExtra(DocumentActivity.PATH_KEY, model.path)
                it.context.startActivity(intent)
            }
        }

        class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
            lateinit var coverIV : ImageView
            lateinit var nameTV : TextView

            init {
                coverIV = itemView.findViewById(R.id.document_cover) as ImageView
                nameTV = itemView.findViewById(R.id.document_name) as TextView
            }
        }
    }
}
