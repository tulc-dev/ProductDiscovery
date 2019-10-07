package com.tulc.product.discovery.extensions

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.observers.ConsumerSingleObserver
import io.reactivex.schedulers.Schedulers


fun <T> Single<T>.subscribeMainThread(
    onSuccess: Consumer<in T>,
    onError: Consumer<in Throwable>
): Disposable {
    val observer = ConsumerSingleObserver<T>(onSuccess, onError)

    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer)
    return observer
}
