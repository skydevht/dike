package tech.skydev.dike.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tech.skydev.dike.Injector
import tech.skydev.dike.R
import tech.skydev.dike.model.Document
import tech.skydev.dike.service.DocumentService

class BookShelfActivity : BaseActivity() {

    lateinit var recycler : RecyclerView
    val adapter = Adapter(this)
    val service = Injector.documentService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_shelf)
        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun onStart() {
        super.onStart()
        service?.loadAllDocuments(object : DocumentService.Callback<List<Document>> {
            override fun onSuccess(result: List<Document>) {
                adapter.models = result as ArrayList<Document>
                adapter.notifyDataSetChanged()
            }

            override fun onError(ex: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
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
                intent.putExtra(DocumentActivity.ID_KEY, model.id)
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
