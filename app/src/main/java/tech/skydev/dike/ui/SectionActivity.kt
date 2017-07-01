package tech.skydev.dike.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.parceler.Parcels
import tech.skydev.dike.R
import tech.skydev.dike.model.Content
import tech.skydev.dike.model.Section

class SectionActivity : BaseActivity() {

    var section: Section? = null
    var path: String? = null

    lateinit var recycler: RecyclerView
    val adapter = ContentAdapter(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)

        recycler = findViewById(R.id.list) as RecyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        adapter.path = if (savedInstanceState == null) intent.getStringExtra(PATH_KEY) else savedInstanceState.getString(PATH_KEY)
        section = Parcels.unwrap(if (savedInstanceState == null)
            intent.getParcelableExtra(SECTION_KEY) else savedInstanceState.getParcelable(SECTION_KEY))
        title = section?.name
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

    class ContentAdapter(val context: Context) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

        var models: ArrayList<SectionItem> = ArrayList()
        val contents: ArrayList<Content> = ArrayList()
        val BODY_TEXT = 5
        var path: String? = null

        fun setData(section: Section?) {
            models.clear()
            if (section != null) processSection(section)
            notifyDataSetChanged()
        }

        private fun processSection(section: Section?, level: Int = 0) {
            if (section != null) {
                val sectionItem = SectionItem(section.name, null, section.order, level)
                models.add(sectionItem)
                section.contents?.forEach {
                    val model = SectionItem(it.name, it.path, it.order, BODY_TEXT)
                    //TODO classification
                    models.add(model)
                    contents.add(it)
                }
                if (section.children != null && section.children?.isNotEmpty() ?: false) section.children?.forEach { processSection(it, level + 1) }
            }
        }


        override fun getItemCount(): Int {
            return models.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = models[position]
            holder.titleView.text = model.name
            holder.titleView.textSize = when (model.level) {
                0 -> 23.616f
                1 -> 22.128f
                2 -> 20.736f
                3 -> 19.44f
                4 -> 18.208f
                else -> 16f
            }
            if (model.level < BODY_TEXT) holder.titleView.setTextColor(context.resources.getColor(R.color.colorAccent))
            else holder.titleView.setTextColor(Color.parseColor("#000000"))
            //if (model.level < BODY_TEXT) holder.titleView.setTypeface(null, Typeface.BOLD) else holder.titleView.setTypeface(null, Typeface.NORMAL)
            if (model.level >= BODY_TEXT)
                holder.itemView.setOnClickListener {
                    val intent = Intent(it.context, ArticleActivity::class.java)
                    intent.putExtra(ArticleActivity.PATH_KEY, "$path/${model.path}")
                    it.context.startActivity(intent)
                }
            else holder.itemView.setOnClickListener(null)
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

        inner class SectionItem(val name: String?, val path: String?, val order: Int = 0, val level: Int = 0)
    }
}
