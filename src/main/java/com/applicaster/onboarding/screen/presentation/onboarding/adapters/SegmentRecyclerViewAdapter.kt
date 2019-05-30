package com.applicaster.onboarding.screen.presentation.onboarding.adapters


import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.applicaster.onboarding.screen.PluginDataRepository
import com.applicaster.onboarding.screen.model.Segment
import com.applicaster.onboarding.screen.presentation.onboarding.OnListFragmentInteractionListener
import com.applicaster.onboarding.screen.utils.BounceInterpolator
import com.applicaster.onboardingscreen.R
import com.applicaster.util.OSUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_segment.view.*


class SegmentRecyclerViewAdapter(
        private val mValues: List<Segment>,
        private val previouslySelected: List<String>?,
        private val mListener: OnListFragmentInteractionListener?,
        private val activity: Activity)
    : RecyclerView.Adapter<SegmentRecyclerViewAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_segment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        previouslySelected?.let {
            for (id in it) {
                if (item.id.equals(id, true)) {
                    highlightItem(holder, item)
                }
            }
        }

        Glide.with(activity).load(item.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ob_16_9_vertical_placeholder).error(R.drawable.ob_16_9_vertical_placeholder))
                .into(holder.segmentImageView)

        holder.segmentCardview.setOnClickListener {
            if (holder.selectIcon.isSelected) {
                holder.selectIcon.isSelected = false
                holder.selectIcon.setImageDrawable(activity.resources.getDrawable(R.drawable.ob_like_icon_unselected))
                mListener?.onSegmentUnSelected(item)

                val border = holder.borderLayout.background as GradientDrawable
                border.setStroke(0, Color.TRANSPARENT)
            } else {
                selectItem(holder, item)
            }
            val bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
            val interpolator = BounceInterpolator(0.2, 20.00)
            bounce.interpolator = interpolator
            holder.segmentCardview.startAnimation(bounce)
        }
    }

    override fun getItemCount(): Int = mValues.size

    private fun selectItem(holder: ViewHolder, item: Segment) {
        holder.selectIcon.isSelected = true
        holder.selectIcon.setImageDrawable(activity.resources.getDrawable(R.drawable.ob_like_icon_selected))
        mListener?.onSegmentSelected(item)

        if (PluginDataRepository.INSTANCE.pluginConfig.isApplyBorder) {
            val border = holder.borderLayout.background as GradientDrawable
            border.setStroke(OSUtil.convertPixelsToDP(2), Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.highlightColor))
        }
    }

    private fun highlightItem(holder: ViewHolder, item: Segment) {
        holder.selectIcon.isSelected = true
        holder.selectIcon.setImageDrawable(activity.resources.getDrawable(R.drawable.ob_like_icon_selected))

        if (PluginDataRepository.INSTANCE.pluginConfig.isApplyBorder) {
            val border = holder.borderLayout.background as GradientDrawable
            border.setStroke(OSUtil.convertPixelsToDP(2), Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.highlightColor))
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val segmentImageView: ImageView = mView.segment_imageview
        val selectIcon: ImageView = mView.selection_icon_imageview
        val segmentCardview: CardView = mView.segement_cardview
        val borderLayout: View = mView.border_layout
    }
}
