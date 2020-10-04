package com.theevilroot.mybsuir.api

import io.reactivex.rxjava3.core.Observable

class MutableCompositeDataSource<T> (
    networkDataSource: () -> Observable<T>,
    diskDataSource: () -> Observable<T>,
    cacheOnDisk: (T) -> Unit = { },
    private val clearCache: () -> Unit
) : CompositeDataSource<T>(networkDataSource, diskDataSource, cacheOnDisk) {

    fun setMemoryData(t: T) {
        cacheInMemory(t)
        cacheOnDisk(t)
    }

    fun clear() {
        dataCache = null
        clearCache()
    }

}