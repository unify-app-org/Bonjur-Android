package com.bonjur.app.navigation

import androidx.lifecycle.ViewModel
import com.bonjur.navigation.Navigator
import com.bonjur.storage.defaultPreference.DefaultStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    val navigator: Navigator,
    val defaultStorage: DefaultStorage
) : ViewModel()