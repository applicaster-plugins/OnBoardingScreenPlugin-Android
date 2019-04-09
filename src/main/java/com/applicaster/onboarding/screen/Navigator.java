package com.applicaster.onboarding.screen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.applicaster.onboarding.screen.presentation.onboarding.OnboardingActivity;
import com.applicaster.plugin_manager.hook.HookListener;

import java.util.List;

import static com.applicaster.util.OSUtil.getPackageName;

public class Navigator {

    private static final String CLEAR_ACTIVITY_STACK = "clear_activity_stack";

    public void reloadApplication(@NonNull Context context) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public void goToOnboardingScreen(Context context, HookListener listener, List<String> previouslySelected) {
        context.startActivity(OnboardingActivity.Companion.getCallingIntent(context, listener, previouslySelected));
    }
}
