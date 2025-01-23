package com.samapp.renttrack.di

import android.app.Application
import androidx.room.Room
import com.samapp.renttrack.data.local.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application(){
}