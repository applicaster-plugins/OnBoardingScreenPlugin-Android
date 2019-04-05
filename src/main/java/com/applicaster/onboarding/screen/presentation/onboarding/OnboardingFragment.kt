package com.applicaster.onboarding.screen.presentation.onboarding

import android.app.Fragment
import android.graphics.Rect
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.applicaster.onboarding.screen.model.Category
import com.applicaster.onboarding.screen.model.OnBoardingItem
import com.applicaster.onboarding.screen.model.Segment
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.CategoryRecyclerViewAdapter
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.SegmentRecyclerViewAdapter
import com.applicaster.onboardingscreen.R
import com.applicaster.plugin_manager.hook.HookListener
import com.applicaster.util.OSUtil
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_segment_list.*
import okhttp3.*
import java.io.IOException


class OnboardingFragment : Fragment(), OnListFragmentInteractionListener {

    private var client = OkHttpClient()

    private var onBoardingItem: OnBoardingItem? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_segment_list, container, false)


        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_indicator.visibility = View.VISIBLE

        val request = Request.Builder().url("https://api.myjson.com/bins/8q70g").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val mainHandler = Handler(activity.mainLooper)

                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                onBoardingItem = gson.fromJson(body, OnBoardingItem::class.java)

                mainHandler.post {
                    setupAdapters()
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
                loading_indicator.visibility = View.GONE
            }
        })

        confirmation_button.setOnClickListener {
            hookListener.onHookFinished()
        }

    }

    fun setupAdapters() {

        var controller = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)

        segment_list.layoutAnimation = controller
        segment_list.adapter = SegmentRecyclerViewAdapter(onBoardingItem!!.categories.first().segments, this, activity)
        val size = (OSUtil.getScreenWidth(activity) - OSUtil.convertPixelsToDP(279)) / 4
        segment_list.addItemDecoration(GridSpacingItemDecoration(3, size))
        segment_list.scheduleLayoutAnimation()


        category_list.adapter = CategoryRecyclerViewAdapter(onBoardingItem!!.categories, activity, this)
        category_list.addItemDecoration(MarginItemDecoration(OSUtil.convertPixelsToDP(10)))

        loading_indicator.visibility = View.GONE
    }

    override fun onCategorySelected(category: Category?) {
        var controller = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        segment_list.layoutAnimation = controller
        segment_list.adapter = SegmentRecyclerViewAdapter(category!!.segments, this, activity)
        segment_list.scheduleLayoutAnimation()
    }

    override fun onSegmentSelected(segment: Segment?) {

    }

    companion object {

        private lateinit var hookListener: HookListener

        @JvmStatic
        fun newInstance(listener: HookListener) =
                OnboardingFragment().apply {
                    hookListener = listener
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

interface OnListFragmentInteractionListener {
    fun onCategorySelected(category: Category?)
    fun onSegmentSelected(segment: Segment?)
}
