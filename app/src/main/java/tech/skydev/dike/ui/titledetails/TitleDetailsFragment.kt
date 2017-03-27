package tech.skydev.dike.ui.titledetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Titre
import tech.skydev.dike.util.ConversionUtil
import tech.skydev.dike.widget.GridSpacingItemDecoration

/**
 * A placeholder fragment containing a simple view.
 */
class TitleDetailsFragment : Fragment(), TitleDetailsContract.View {


    var mAdapter: TitleDetailsAdapter? = TitleDetailsAdapter(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_titledetails, container, false)
        val recycler: RecyclerView = rootView?.findViewById(R.id.titledetails_list) as RecyclerView
        val layoutManager: GridLayoutManager = GridLayoutManager(context, 3)
        layoutManager.spanSizeLookup= object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(mAdapter?.getItemViewType(position)) {
                    mAdapter?.TITLE_VIEW_TYPE, mAdapter?.CHAPTER_VIEW_TYPE,
                    mAdapter?.SECTION_VIEW_TYPE -> 3
                    else -> 1
                }
            }

        }
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager =layoutManager
        recycler.adapter = mAdapter

        recycler.addItemDecoration(GridSpacingItemDecoration(ConversionUtil.dpToPx(context, 8), true))
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter!!.start()
    }



    var mPresenter: TitleDetailsContract.Presenter? = null


    override fun setPresenter(presenter: TitleDetailsContract.Presenter) {
        this.mPresenter = presenter;
    }

    override fun showTitle(titre: Titre) {
        mAdapter?.replaceItems(titre)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Titre "+ titre.id
        bar?.subtitle = titre.name
    }

    companion object {
        fun newInstance(): TitleDetailsFragment {
            return TitleDetailsFragment()
        }
    }
}
