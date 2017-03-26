package tech.skydev.dike.ui.section

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList

import tech.skydev.dike.R
import tech.skydev.dike.data.Section

/**
 * Created by Hash Skyd on 3/26/2017.
 */

class SectionAdapter(internal var models: ArrayList<Section>) : RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    private val PREAMBULE_VIEW_TYPE = 0
    private val NORMAL_VIEW_TYPE = 1

    fun replaceItems(models: ArrayList<Section>) {
        this.models = models
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) PREAMBULE_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout: Int
        var holder: ViewHolder
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == PREAMBULE_VIEW_TYPE) {
            layout = R.layout.cell_section_preambule
            holder = PreambuleViewHolder(inflater.inflate(layout, parent, false))
        } else {
            layout = R.layout.cell_section
            holder = NormalViewHolder(inflater.inflate(layout, parent, false))
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindToModel(getItem(position), position)
    }

    private fun getItem(position: Int): Section {
        return models[position]
    }

    override fun getItemCount(): Int {
        return models.size
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bindToModel(model: Section, pos: Int)
    }

    internal inner class PreambuleViewHolder(itemView: View) : ViewHolder(itemView) {

        var sectionNameTV: TextView

        init {
            sectionNameTV = itemView.findViewById(R.id.section_name) as TextView
        }

        override fun bindToModel(model: Section, pos: Int) {
            sectionNameTV.text = model.name
        }
    }

    internal inner class NormalViewHolder(itemView: View) : ViewHolder(itemView) {

        private val sectionIdTV: TextView
        private val sectionNameTV: TextView

        init {
            sectionNameTV = itemView.findViewById(R.id.section_name) as TextView
            sectionIdTV = itemView.findViewById(R.id.section_id) as TextView
        }

        override fun bindToModel(model: Section, pos: Int) {
            sectionNameTV.text = model.name
            sectionIdTV.text = model.id
        }

    }
}
