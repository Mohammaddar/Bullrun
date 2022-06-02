package com.example.bullrun.data.repos

import android.util.Log
import android.widget.Toast
import com.example.bullrun.App

inline fun <T> invokeOrCatch(action: () -> T): T? {
    return try {
        action()
    } catch (e: Exception) {
        Log.e("RepositoryError","$e")
        null
    }
}