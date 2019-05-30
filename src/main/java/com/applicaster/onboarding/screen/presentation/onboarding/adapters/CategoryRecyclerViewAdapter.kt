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
import com.applicaster.onboarding.screen.PluginDataRepository
import com.applicaster.onboarding.screen.model.Category
import com.applicaster.onboarding.screen.presentation.onboarding.OnListFragmentInteractionListener
import com.applicaster.onboarding.screen.utils.BounceInterpolator
import com.applicaster.onboardingscreen.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryRecyclerViewAdapter(
        private val mValues: List<Category>,
        private val activity: Activity,
        private val userLocale: String,
        private val languages: List<String>,
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

        holder.categoryTextView.text = item.title?.get(userLocale) ?:
                item.title?.get(languages.first())
        Glide.with(activity).load(item.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ob_category_placeholder).error(R.drawable.ob_category_placeholder))
                .into(holder.categoryImageView)

        if (selectedPosition == position) {
            holder.categoryTextView.setTextColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.backgroundColor))
            holder.categoryBackgroundLayer.setBackgroundColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.highlightColor))
        } else {
            holder.categoryTextView.setTextColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.titleColor))
            holder.categoryBackgroundLayer.setBackgroundColor(Color.parseColor(PluginDataRepository.INSTANCE.pluginConfig.categoryBackgroundColor))
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
