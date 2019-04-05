package com.applicaster.onboarding.screen.presentation.onboarding.adapters

import android.app.Activity
import android.graphics.Color
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.applicaster.onboarding.screen.model.Category
import com.applicaster.onboarding.screen.presentation.onboarding.OnListFragmentInteractionListener
import com.applicaster.onboarding.screen.utils.BounceInterpolator
import com.applicaster.onboardingscreen.R
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryRecyclerViewAdapter(
        private val mValues: List<Category>,
        private val activity: Activity,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        holder.categoryTextView.text = item.title.en

        if (selectedPosition == position) {
            holder.categoryTextView.setTextColor(activity.resources.getColor(R.color.white))
            holder.categoryBackgroundLayer.setBackgroundColor(Color.parseColor("#00C499"))
            holder.categoryImageView.setImageDrawable(activity.resources.getDrawable(R.drawable.liga_test_white))
        } else {
            holder.categoryTextView.setTextColor(Color.parseColor("#908F95"))
            holder.categoryBackgroundLayer.setBackgroundColor(Color.parseColor("#EFEEF4"))
            holder.categoryImageView.setImageDrawable(activity.resources.getDrawable(R.drawable.liga_test))
        }

        holder.categoryCardView.setOnClickListener {
            val bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
            val interpolator = BounceInterpolator(0.2, 20.00)
            notifyDataSetChanged()
            bounce.interpolator = interpolator
            holder.categoryCardView.startAnimation(bounce)
            selectedPosition = position
            mListener?.onCategorySelected(item)
        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val categoryTextView: TextView = mView.category_textview
        val categoryBackgroundLayer: LinearLayout = mView.category_background_layer
        val categoryImageView: ImageView = mView.category_imageview
        val categoryCardView: CardView = mView.category_cardview
    }
}
