package com.vga.spinwheel.ui.screen.intro

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor() : ViewModel() {

    fun markIntroDone(onSaved: () -> Unit) {
        onSaved()
    }
}
