package com.yihs.dailycashflow.di

import com.yihs.dailycashflow.repository.Repository
import com.yihs.dailycashflow.ui.auth.AuthViewModel
import com.yihs.dailycashflow.ui.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single { Repository(get(), get() ) }

    viewModelOf(::AuthViewModel)
    viewModelOf(::ProfileViewModel)

}
