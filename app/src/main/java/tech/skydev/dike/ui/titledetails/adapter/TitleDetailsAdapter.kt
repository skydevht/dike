package tech.skydev.dike.ui.titledetails.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Article
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */

class TitleDetailsAdapter(internal var data: Titre?) : RecyclerView.Adapter<TitleDetailsAdapter.ViewHolder>() {

    lateinit var models: ArrayList<Model>
    var mListener: ClickListener? = null

    init {
        models = setData(data)
    }

    private fun setData(data: Titre?): ArrayList<Model> {
        if (data == null) {
            return ArrayList<Model>(0)
        }
        val newModels: ArrayList<Model> = ArrayList<Model>(0)
        newModels.add(Model(data.name?:"", TITLE_VIEW_TYPE, null))
        for (chapter in data.chapters!!) {
            chapter.id?.let {
                newModels.add(Model("CH "+chapter.id!!+" - "+chapter.name!!, CHAPTER_VIEW_TYPE, chapter))
            }
            for (section in chapter.sections!!) {
                section.id?.let {
                    newModels.add(Model("Section "+section.id!!, SECTION_VIEW_TYPE, section))
                }
                for (article in section.articles!!) {
                        newModels.add(Model(article.order, ARTICLE_VIEW_TYPE, article))
                }
            }
        }
        return newModels
    }

    open val TITLE_VIEW_TYPE = 0
    open val CHAPTER_VIEW_TYPE = 1
    open val SECTION_VIEW_TYPE = 2
    open val ARTICLE_VIEW_TYPE = 3

    fun replaceItems(data: Titre) {
        models = setData(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout: Int = when(viewType) {
            TITLE_VIEW_TYPE, CHAPTER_VIEW_TYPE -> R.layout.cell_titledetails_chapter

            SECTION_VIEW_TYPE -> R.layout.cell_titledetails_section

            else ->  R.layout.cell_default
        }
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindToModel(getItem(position), position)
        holder.itemView.setOnClickListener {
            if (getItemViewType(position) == ARTICLE_VIEW_TYPE) {
                mListener?.onArticleClicked(getItem(position).data as Article)
            }
        }
    }

    private fun getItem(position: Int): Model {
        return models[position]
    }

    override fun getItemCount(): Int {
        return models.size
    }

    //region ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mTextView: TextView

        init {
            mTextView = itemView.findViewById(R.id.name) as TextView
        }

        fun bindToModel(model: Model, pos: Int) {
            if (model.itemType == this@TitleDetailsAdapter.TITLE_VIEW_TYPE) mTextView.textSize = 24f
            mTextView.setText(model.text)
        }
    }

    //endregion

    data class Model(val text: String, val itemType: Int, val data: Any?)

    open abstract class ClickListener {
         abstract fun onArticleClicked(article: Article)
    }
}
