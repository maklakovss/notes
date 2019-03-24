package com.mss.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mss.notes.data.NotesRepository
import com.mss.notes.data.provider.FireStoreProvider
import com.mss.notes.data.provider.RemoteDataProvider
import com.mss.notes.ui.main.MainViewModel
import com.mss.notes.ui.note.NoteViewModel
import com.mss.notes.ui.splash.SplashViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<RemoteDataProvider> { FireStoreProvider(get(), get()) }
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}