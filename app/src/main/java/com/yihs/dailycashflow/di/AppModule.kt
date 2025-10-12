package com.yihs.dailycashflow.di

import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.ui.auth.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single { Repository(get()) }

    viewModelOf(::AuthViewModel)
}
