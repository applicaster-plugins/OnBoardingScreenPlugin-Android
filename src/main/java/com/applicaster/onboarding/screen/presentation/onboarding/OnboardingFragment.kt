package com.applicaster.onboarding.screen.presentation.onboarding

import android.app.Fragment
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.applicaster.app.CustomApplication
import com.applicaster.onboarding.screen.OnboardingScreenContract.USER_RECOMMENDATION_KEY
import com.applicaster.onboarding.screen.OnboardingScreenContract.USER_RECOMMENDATION_NAMESPACE
import com.applicaster.onboarding.screen.PluginDataRepository
import com.applicaster.onboarding.screen.model.Category
import com.applicaster.onboarding.screen.model.OnBoardingItem
import com.applicaster.onboarding.screen.model.Segment
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.CategoryRecyclerViewAdapter
import com.applicaster.onboarding.screen.presentation.onboarding.adapters.SegmentRecyclerViewAdapter
import com.applicaster.onboardingscreen.R
import com.applicaster.plugin_manager.Plugin
import com.applicaster.plugin_manager.PluginManager
import com.applicaster.plugin_manager.hook.HookListener
import com.applicaster.plugin_manager.push_plugin.PushContract
import com.applicaster.plugin_manager.push_plugin.PushManager
import com.applicaster.plugin_manager.push_plugin.helper.PushPluginsType
import com.applicaster.plugin_manager.push_plugin.listeners.PushTagRegistrationI
//import com.applicaster.session.SessionStorage
import com.applicaster.util.OSUtil
import com.applicaster.util.PreferenceUtil
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_segment_list.*
import okhttp3.*
import java.io.IOException


class OnboardingFragment : Fragment(), OnListFragmentInteractionListener {

    private var client = OkHttpClient()
    private lateinit var onBoardingItem: OnBoardingItem
    private var userLocale = CustomApplication.getDefaultDeviceLocale().language

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_segment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_indicator.visibility = VISIBLE

        val request = Request.Builder().url(PluginDataRepository.INSTANCE.pluginConfig.onBoardingFeedPath).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val mainHandler = Handler(activity.mainLooper)

                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                onBoardingItem = gson.fromJson(body, OnBoardingItem::class.java)

                mainHandler.post {
                    styleViews()
                    setupAdapters()
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("Failed to execute request")
                val mainHandler = Handler(activity.mainLooper)
                mainHandler.post {
                    loading_indicator.visibility = GONE
                    activity.finish()
                }
            }
        })

        if (previousSelections.size == 0) {
            previousSelections.add(DISPLAYED)
        }

        confirmation_button.setOnClickListener {
            registerTags()
            PreferenceUtil.getInstance().setStringArrayPref(USER_RECOMMENDATION_KEY, previousSelections.toTypedArray())
//            SessionStorage.set(USER_RECOMMENDATION_KEY, previousSelections.toString(), USER_RECOMMENDATION_NAMESPACE)
            hookListener?.onHookFinished()
            activity.finish()
        }

    }

    private fun styleViews() {
        title_textview.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.title?.get(userLocale)
                ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.title?.get(this@OnboardingFragment.onBoardingItem.languages.first())

        subtitle_textview.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.subtitle?.get(userLocale)
                ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.subtitle?.get(this@OnboardingFragment.onBoardingItem.languages.first())

        if (previousSelections.size > 1) {
            confirmation_button.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.finishOnboarding?.get(userLocale)
                    ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.finishOnboarding?.get(this@OnboardingFragment.onBoardingItem.languages.first())
        } else {
            confirmation_button.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.skipOnboarding?.get(userLocale)
                    ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.skipOnboarding?.get(this@OnboardingFragment.onBoardingItem.languages.first())
        }

        background_layout.setBackgroundColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.backgroundColor))

        title_textview.setTextColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.titleColor))

        subtitle_textview.setTextColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.highlightColor))

        confirmation_button.setTextColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.backgroundColor))

        val drawable = confirmation_button.background as GradientDrawable
        drawable.setColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.highlightColor))
    }

    private fun setupAdapters() {

        var controller = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)

        segment_list.layoutAnimation = controller
        segment_list.adapter = SegmentRecyclerViewAdapter(onBoardingItem.categories.first().segments, previousSelections, this, activity)
        val size = (OSUtil.getScreenWidth(activity) - OSUtil.convertPixelsToDP(279)) / 4
        segment_list.addItemDecoration(GridSpacingItemDecoration(3, size))
        segment_list.scheduleLayoutAnimation()


        if (onBoardingItem.categories.size > 1) {
            category_list.adapter = CategoryRecyclerViewAdapter(onBoardingItem.categories, activity, userLocale, onBoardingItem.languages, this)
            category_list.addItemDecoration(MarginItemDecoration(OSUtil.convertPixelsToDP(10)))
        } else {
            category_list.visibility = GONE
        }

        loading_indicator.visibility = GONE
    }

    private fun registerTags() {
        var plugin = PluginManager.getInstance().getInitiatedPlugins(Plugin.Type.PUSH).firstOrNull()?.getInstance<PushContract>()
        if (plugin == null) {
            Log.e(TAG, "No push provider configured")
        } else {
            var language = onBoardingItem.languages.first()
            if (onBoardingItem.languages.contains(userLocale)) {
                language = userLocale
            }

            val localizedSelections: MutableList<String> = emptyList<String>().toMutableList()
            for (item in previousSelections) {
                if (item != DISPLAYED) {
                    localizedSelections.add("$item-$language")
                }
            }

            val localizedDeSelections: MutableList<String> = emptyList<String>().toMutableList()
            for (item in deSelectedItems) {
                if (item != DISPLAYED) {
                    localizedDeSelections.add("$item-$language")
                }
            }

            PushManager.addTagToPlugins(CustomApplication.getAppContext(), plugin.pluginType, localizedSelections, object : PushTagRegistrationI {
                override fun pushUnregistrationTagComplete(type: PushPluginsType?, unregistered: Boolean) {
                }

                override fun pushRregistrationTagComplete(type: PushPluginsType?, registered: Boolean) {
                    if (registered)
                        Log.e(TAG, "Registered Tags to provider")
                    else
                        Log.e(TAG, "Failed to Register Tags to Provider")
                }
            })

            PushManager.removeTagToPlugins(CustomApplication.getAppContext(), plugin.pluginType, localizedDeSelections, object : PushTagRegistrationI {
                override fun pushRregistrationTagComplete(type: PushPluginsType?, registered: Boolean) {

                }

                override fun pushUnregistrationTagComplete(type: PushPluginsType?, unregistered: Boolean) {
                    if (unregistered)
                        Log.e(TAG, "Unregistered Tags to provider")
                    else
                        Log.e(TAG, "Failed to Unregistered Tags to Provider")
                }
            })
        }
    }

    override fun onCategorySelected(category: Category?) {
        val controller = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        segment_list.layoutAnimation = controller
        segment_list.adapter = SegmentRecyclerViewAdapter(category!!.segments, previousSelections, this, activity)
        segment_list.scheduleLayoutAnimation()
    }

    override fun onSegmentSelected(segment: Segment?) {
        segment?.id?.let {
            previousSelections.add(it)
            deSelectedItems.remove(it)
            if (previousSelections.size > 0) {
                confirmation_button.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.finishOnboarding?.get(userLocale)
                        ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.finishOnboarding?.get(this@OnboardingFragment.onBoardingItem.languages.first())
            }
        }
    }

    override fun onSegmentUnSelected(segment: Segment?) {
        segment?.id?.let {
            previousSelections.remove(it)
            deSelectedItems.add(it)
            if (previousSelections.size <= 1) {
                confirmation_button.text = this@OnboardingFragment.onBoardingItem.onboardingTexts.skipOnboarding?.get(userLocale)
                        ?: this@OnboardingFragment.onBoardingItem.onboardingTexts.skipOnboarding?.get(this@OnboardingFragment.onBoardingItem.languages.first())
            }
        }
    }

    companion object {

        private var hookListener: HookListener? = null
        private var previousSelections: MutableList<String> = emptyList<String>().toMutableList()
        private var deSelectedItems: MutableList<String> = emptyList<String>().toMutableList()
        private const val TAG = "ONBOARDING"
        private const val DISPLAYED = "OBDISPLAYED"

        @JvmStatic
        fun newInstance(listener: HookListener?, selections: MutableList<String>) =
                OnboardingFragment().apply {
                    previousSelections = selections.toMutableList()
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

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
    fun onSegmentUnSelected(segment: Segment?)
}
