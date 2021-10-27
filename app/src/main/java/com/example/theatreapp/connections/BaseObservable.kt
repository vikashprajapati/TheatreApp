package com.example.theatreapp.connections

import java.util.*
import java.util.concurrent.ConcurrentHashMap

open class BaseObservable<LISTENER> {
    private val listeners = Collections.newSetFromMap(ConcurrentHashMap<LISTENER, Boolean>(1));

    final fun registerListener(listener : LISTENER){
        listeners.add(listener)
    }

    final fun unRegisterListener(listener : LISTENER){
        listeners.remove(listener)
    }

    protected final fun getListeners() : Set<LISTENER>{
        return Collections.unmodifiableSet(listeners)
    }
}