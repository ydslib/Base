package com.crystallake.base.config

import android.util.SparseArray
import androidx.lifecycle.ViewModel

class DataBindingConfig(
    val layout: Int = 0,
    val vmVariableId: Int = 0,
    val stateViewModel: ViewModel? = null
) {
    val bindingParams: SparseArray<Any> = SparseArray()
    fun addBindingParam(variableId: Int, any: Any) = apply {
        bindingParams[variableId] ?: bindingParams.put(variableId, any)
    }
}