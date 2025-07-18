package com.andef.mycarandef.map.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MapMainViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(MapMainState())
    val state: StateFlow<MapMainState> = _state

    fun send(intent: MapMainIntent) {

    }
}