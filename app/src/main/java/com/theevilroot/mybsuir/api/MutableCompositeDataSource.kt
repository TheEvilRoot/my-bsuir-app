package com.theevilroot.mybsuir.api

import io.reactivex.rxjava3.core.Observable

class MutableCompositeDataSource<T> (
    networkDataSource: () -> Observable<T>
) : CompositeDataSource<T>(networkDataSource) {

    fun setMemoryData(t: T) {
        cacheInMemory(t)
    }

}