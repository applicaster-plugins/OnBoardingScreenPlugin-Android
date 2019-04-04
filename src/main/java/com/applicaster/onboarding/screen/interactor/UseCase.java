package com.applicaster.onboarding.screen.interactor;


import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Interface for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * In reality a Usecase should provide threading possibilities through usage of RxJava but to keep the plugin simple
 * for now, we simply work with synchronous calls for now.
 */
interface UseCase<T, Params> {

    // In reality this
    T execute(Params params);
}

