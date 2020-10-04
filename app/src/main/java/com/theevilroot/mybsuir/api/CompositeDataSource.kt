package com.theevilroot.mybsuir.api

import com.theevilroot.mybsuir.auth.AuthProvider
import com.theevilroot.mybsuir.profile.ProfileModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

open class CompositeDataSource<T> (
    private val networkDataSource: () -> Observable<T>,
    private val diskDataSource: () -> Observable<T>,
    protected val cacheOnDisk: (T) -> Unit = { }
) {

    protected var dataCache: T? = null

    fun getData(): Single<T> =
        Observable.concat(getDataFromMemory(), getDataFromDisk(), getDataFromNetwork())
            .firstElement()
            .subscribeOn(Schedulers.io())
            .toSingle()

    private fun getDataFromMemory(): Observable<T> =
        Observable.create<T> {
            val cache = dataCache
            if (cache != null) {
                it.onNext(cache)
            }
            it.onComplete()
        }

    private fun getDataFromNetwork(): Observable<T> =
        networkDataSource().onErrorComplete {
            it.printStackTrace()
            !(it is SecurityException || it is AuthProvider.NoAuthDataException)
        }.doOnNext { cacheInMemory(it); cacheOnDisk(it) }

    private fun getDataFromDisk(): Observable<T> =
        diskDataSource().onErrorComplete {
            it.printStackTrace()
            it !is SecurityException
        }.doOnNext { cacheInMemory(it) }

    protected fun cacheInMemory(t: T) {
        dataCache = t
    }

}