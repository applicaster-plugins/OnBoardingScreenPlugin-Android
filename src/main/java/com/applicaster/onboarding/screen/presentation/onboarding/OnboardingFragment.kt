package com.applicaster.onboarding.screen.presentation.onboarding

import android.app.Fragment
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.CategoryRecyclerViewAdapter
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.SegmentRecyclerViewAdapter
import com.applicaster.onboarding.screen.presentation.onboarding.dummy.DummyContent
import com.applicaster.onboarding.screen.presentation.onboarding.dummy.DummyContent.DummyItem
import com.applicaster.onboardingscreen.R
import com.applicaster.util.OSUtil
import kotlinx.android.synthetic.main.fragment_segment_list.*

class OnboardingFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_segment_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        segment_list.adapter = SegmentRecyclerViewAdapter(DummyContent.ITEMS, listener)

        val size = (OSUtil.getScreenWidth(activity) - OSUtil.convertPixelsToDP(279)) / 4

        segment_list.addItemDecoration(GridSpacingItemDecoration(3, size))


        category_list.adapter = CategoryRecyclerViewAdapter(DummyContent.ITEMS, listener)

        category_list.addItemDecoration(MarginItemDecoration(OSUtil.convertPixelsToDP(10)))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                OnboardingFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }

    class MarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            with(outRect) {

                right = spaceHeight
                if (parent.getChildAdapterPosition(view) > 0) {
                    left = spaceHeight
                }
            }
        }
    }

    inner class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
