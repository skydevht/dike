package tech.skydev.dike.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.parceler.Parcels
import tech.skydev.dike.Injector
import tech.skydev.dike.R
import tech.skydev.dike.model.Document
import tech.skydev.dike.model.Section
import tech.skydev.dike.service.DocumentService

class DocumentActivity : BaseActivity() {

    lateinit var recycler: RecyclerView
    val adapter = TocAdapter()
    val service = Injector.documentService
    var id: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)

        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        id = if (savedInstanceState == null) intent.getStringExtra(ID_KEY) else savedInstanceState.getString(ID_KEY)
    }

    override fun onStart() {
        super.onStart()
        service?.loadDocument(id!!, object : DocumentService.Callback<Document> {
            override fun onSuccess(result: Document) {
                title = result.name
                adapter.path = result.path!!
                adapter.models = result.sections!! as ArrayList<Section>
                adapter.notifyDataSetChanged()

            }

            override fun onError(ex: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(ID_KEY, id)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.getActionView() as SearchView
        searchView.queryHint = "Rechercher dans un article"
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        val ID_KEY = "path_key"
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
