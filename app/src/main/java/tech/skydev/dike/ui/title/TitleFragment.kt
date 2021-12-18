package tech.skydev.dike.ui.title

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.skydev.dike.R
import tech.skydev.dike.base.BaseFragment
import tech.skydev.dike.data.model.Titre
import tech.skydev.dike.ui.MainActivity
import tech.skydev.dike.ui.Navigation
import tech.skydev.dike.util.ConversionUtil
import tech.skydev.dike.widget.GridSpacingItemDecoration

/**
 * A placeholder fragment containing a simple view.
 */
class TitleFragment : BaseFragment(), TitlesContract.View {

    override lateinit var context2: Context

    var mAdapter: TitleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = object: TitleAdapter(ArrayList(0)) {
            override fun onCellClick(model: Titre, pos: Int) {
                val navigation: Navigation = activity as MainActivity
                if (getItemViewType(pos) == PREAMBULE_VIEW_TYPE) {
                    navigation.showArticle(model.id!!, 1)
                }
                else {

                    navigation.showTitleScreen(model.id!!)
                }
            }

        }
        context2 = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_titles, container, false)
        val recycler: RecyclerView = rootView?.findViewById(R.id.section_list) as RecyclerView
        val colSpan = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
        val layoutManager: GridLayoutManager =
            GridLayoutManager(context, colSpan)
        layoutManager.spanSizeLookup= object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val size = if (position == 0) {
                    colSpan
                } else {
                    1
                }
                return size
            }

        }
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager =layoutManager
        recycler.adapter = mAdapter

        recycler.addItemDecoration(GridSpacingItemDecoration(ConversionUtil.dpToPx(context!!, 8), true))
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter!!.start()
    }

    override fun onResume() {
        super.onResume()
        val bar: ActionBar? = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Constitution Haitienne"
        bar?.subtitle = "1987 - Amendée"
    }

    var mPresenter: TitlesContract.Presenter? = null


    override fun setPresenter(presenter: TitlesContract.Presenter) {
        this.mPresenter = presenter;
    }

    override fun showTitles(titres: ArrayList<Titre>) {
        val bundle = Bundle()
        mAdapter?.replaceItems(titres)
    }

    companion object {
        val TAG: String = "title-fragment"
        fun newInstance(): TitleFragment {
            return TitleFragment()
        }
    }
}
