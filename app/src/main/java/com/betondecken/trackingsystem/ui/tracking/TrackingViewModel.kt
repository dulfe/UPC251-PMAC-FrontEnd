package com.betondecken.trackingsystem.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrackingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is TRACKING Fragment"
    }
    val text: LiveData<String> = _text
}