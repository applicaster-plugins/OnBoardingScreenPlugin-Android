package com.applicaster.onboarding.screen.presentation;

import android.content.Context;

/**
 * Interface representing a View that will load some data
 */
public interface LoadDataView {

    /**
     * Show a view with a progress bar indicating a loading process
     */
    void showLoading();

    /**
     * Hide a loading view
     */
    void hideLoading();

    /**
     * Show an error message
     *
     * @param message A string representing the error
     */
    void showError(String message);


    /**
     * Get a {@link Context}
     */
    Context context();
}
