package com.vikash.syncr_core.utils

class Event<Any> (private var content : Any) {
    var hasBeenHandled = false

    fun getContentIfNotHandledOrReturnNull() : Any? =
        if(hasBeenHandled) null else {
            hasBeenHandled = true
            content
        }
}