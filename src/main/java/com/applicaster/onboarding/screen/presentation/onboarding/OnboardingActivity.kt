package com.applicaster.onboarding.screen.presentation.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.applicaster.onboardingscreen.R
import com.applicaster.plugin_manager.hook.HookListener


class OnboardingActivity : Activity() {
    companion object {
        private var hookListener: HookListener? = null
        fun getCallingIntent(context: Context, listener: HookListener): Intent {
            hookListener = listener
            return Intent(context, OnboardingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val fragmentTransaction = fragmentManager.beginTransaction()
        hookListener?.let {
            val loadingFragment = OnboardingFragment.newInstance(it)
            fragmentTransaction.add(R.id.fragment_container, loadingFragment, loadingFragment.javaClass.canonicalName).commit()
        }

    }
}
