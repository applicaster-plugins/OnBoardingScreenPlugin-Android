package com.applicaster.onboarding.screen.presentation.onboarding.adapters


import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.applicaster.onboarding.screen.model.Segment
import com.applicaster.onboarding.screen.presentation.onboarding.OnListFragmentInteractionListener
import com.applicaster.onboardingscreen.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_segment.view.*

class SegmentRecyclerViewAdapter(
        private val mValues: List<Segment>,
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

        Glide.with(activity).load(item.imageUrl).into(holder.segmentImageView)

        holder.mView.setOnClickListener {
            if (holder.selectIcon.isSelected) {
                holder.selectIcon.isSelected = false
                holder.selectIcon.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_favorite_unselected))
            } else {
                holder.selectIcon.isSelected = true
                holder.selectIcon.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_favorite_selected))
            }
            Toast.makeText(activity,"Subscribed to " + item.title.en, Toast.LENGTH_LONG).show()
            mListener?.onListFragmentInteraction(item as Any)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val segmentImageView: ImageView = mView.segment_imageview
        val selectIcon: ImageView = mView.selection_icon_imageview
    }
}
