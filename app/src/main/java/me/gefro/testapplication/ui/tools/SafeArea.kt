package me.gefro.testapplication.ui.tools

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SafeArea {

    private val _top = MutableStateFlow(0f)
    val top: StateFlow<Float> = _top

    fun setTop(dimen: Float){
        _top.value = dimen
    }

    private val _bottom = MutableStateFlow(0f)
    val bottom: StateFlow<Float> = _bottom

    fun setBottom(dimen: Float){
        _bottom.value = dimen
    }

}