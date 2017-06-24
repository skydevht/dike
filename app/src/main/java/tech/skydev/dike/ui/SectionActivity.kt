package tech.skydev.dike.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.parceler.Parcels
import tech.skydev.dike.R
import tech.skydev.dike.model.Section

class SectionActivity : AppCompatActivity() {

    var section: Section? = null
    var path: String? = null

    lateinit var recycler: RecyclerView
    val adapter = ContentAdapter();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)

        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        path = if (savedInstanceState == null) intent.getStringExtra(PATH_KEY) else savedInstanceState.getString(PATH_KEY)
        section = Parcels.unwrap(if (savedInstanceState == null)
            intent.getParcelableExtra(SECTION_KEY) else savedInstanceState.getParcelable(SECTION_KEY))
    }

    override fun onStart() {
        super.onStart()
        adapter.setData(section)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable(SECTION_KEY, Parcels.wrap(section))
        outState?.putString(PATH_KEY, path)
    }

    companion object {
        val SECTION_KEY = "section-key"
        val PATH_KEY = "path_key"
    }

    inner class ContentAdapter() : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

        var models: ArrayList<SectionItem> = ArrayList()

        fun setData(section: Section?) {
            models.clear()
            if (section != null) processSection(section)
            notifyDataSetChanged()
        }

        private fun processSection(section: Section?) {
            if (section != null) {
                if (section.children != null && section.children?.isNotEmpty()?:false) section.children?.forEach { processSection(it) }
                section.contents?.forEach {
                    val model = SectionItem(it.name, it.path, it.order)
                    //TODO classification
                    models.add(model)
                }
            }
        }

        override fun getItemCount(): Int {
            return models.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = models[position]
            holder.titleView.text = model.name
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context, ArticleActivity::class.java)
                intent.putExtra(ArticleActivity.PATH_KEY, "$path/${model.path}")
                it.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            return ViewHolder(inflater.inflate(R.layout.row_section_content, parent, false))
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var titleView: TextView

            init {
                titleView = itemView.findViewById(R.id.content_title) as TextView
            }

        }

        inner class SectionItem(val name: String?,  val path: String?, val order: Int = 0)
    }
}
