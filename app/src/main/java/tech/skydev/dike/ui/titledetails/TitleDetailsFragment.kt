package tech.skydev.dike.ui.titledetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Titre
import tech.skydev.dike.ui.titledetails.adapter.TitleDetailsAdapter
import tech.skydev.dike.ui.titledetails.adapter.TitleSideAdapter
import tech.skydev.dike.util.ConversionUtil
import tech.skydev.dike.widget.GridSpacingItemDecoration

/**
 * A placeholder fragment containing a simple view.
 */
class TitleDetailsFragment : Fragment(), TitleDetailsContract.View {



    var mAdapter: TitleDetailsAdapter = TitleDetailsAdapter(null)
    var mSideAdapter: TitleSideAdapter = object : TitleSideAdapter(ArrayList<Titre>(0)) {
        override fun onCellClick(model: Titre, pos: Int) {
            //
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mTitleId = arguments.getString("titleId")!!
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_titledetails, container, false)
        initMainRecycler(rootView)
        val sideRecycler = rootView.findViewById(R.id.section_list) as RecyclerView
        sideRecycler.layoutManager = LinearLayoutManager(context)
        sideRecycler.adapter = mSideAdapter
        return rootView
    }

    private fun initMainRecycler(rootView: View?) {
        val recycler: RecyclerView = rootView?.findViewById(R.id.titledetails_list) as RecyclerView
        val layoutManager: GridLayoutManager = GridLayoutManager(context, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mAdapter.getItemViewType(position)) {
                    mAdapter.TITLE_VIEW_TYPE, mAdapter.CHAPTER_VIEW_TYPE,
                    mAdapter.SECTION_VIEW_TYPE -> 3
                    else -> 1
                }
            }

        }
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = mAdapter

        recycler.addItemDecoration(GridSpacingItemDecoration(ConversionUtil.dpToPx(context, 8), true))
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
        mAdapter.replaceItems(titre)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Titre "+ titre.id
        bar?.subtitle = titre.name
    }

    lateinit var  mTitleId: String

    override fun showTitles(titres: ArrayList<Titre>) {
        mSideAdapter.replaceItems(titres)
        mSideAdapter.selectedId = mTitleId
    }

    companion object {
        fun newInstance(id: String): TitleDetailsFragment {
            val args: Bundle = Bundle()
            args.putString("titleId", id)
            val titleDetailsFragment = TitleDetailsFragment()
            titleDetailsFragment.arguments = args
            return titleDetailsFragment
        }
    }
}
