package tech.skydev.dike.ui.titledetails.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Titre

/**
 * Created by Hash Skyd on 3/26/2017.
 */

abstract class TitleSideAdapter(internal var models: ArrayList<Titre>) : RecyclerView.Adapter<TitleSideAdapter.ViewHolder>() {

    val PREAMBULE_VIEW_TYPE = 0
    val NORMAL_VIEW_TYPE = 1

    open var selectedId: String? = null

    fun replaceItems(models: ArrayList<Titre>) {
        this.models = models
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) PREAMBULE_VIEW_TYPE else NORMAL_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layout: Int = R.layout.cell_default
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindToModel(getItem(position), position)
        holder.itemView.setOnClickListener { onCellClick(getItem(position), position) }
    }

    abstract fun onCellClick(model: Titre, pos: Int)

    private fun getItem(position: Int): Titre {
        return models.get(position)
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

        fun bindToModel(model: Titre, pos: Int) {
            if (this@TitleSideAdapter.getItemViewType(pos) == this@TitleSideAdapter.PREAMBULE_VIEW_TYPE) {
                mTextView.setText("P")
            } else {
                mTextView.setText(model.id)
            }

            if (this@TitleSideAdapter.selectedId!! == model.id) {
                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.foreground))
                mTextView.setTextColor(itemView.context.resources.getColor((R.color.colorPrimaryDark)))
            } else {
                itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.colorPrimaryDark))
                mTextView.setTextColor(itemView.context.resources.getColor(R.color.colorAccent))
            }
        }
    }
}
