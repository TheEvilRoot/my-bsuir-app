package com.theevilroot.mybsuir.api.event

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class EventBus {
    private val systemEventBus = PublishSubject.create<Event>()

    fun observable(): Observable<Event> =
        systemEventBus

    fun publishable(): PublishSubject<Event> =
        systemEventBus
}