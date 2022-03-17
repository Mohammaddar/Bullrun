package com.example.bullrun.ui.fragments.searchList

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchListViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchListViewModel::class.java)) {
            return SearchListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

