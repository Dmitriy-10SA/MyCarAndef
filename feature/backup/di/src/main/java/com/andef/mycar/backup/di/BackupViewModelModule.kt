package com.andef.mycar.backup.di

import androidx.lifecycle.ViewModel
import com.andef.mycar.backup.presentation.backupmain.BackupMainViewModel
import com.andef.mycar.backup.presentation.backupstart.BackupStartViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface BackupViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BackupMainViewModel::class)
    fun bindBackupMainViewModel(impl: BackupMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BackupStartViewModel::class)
    fun bindBackupStartViewModel(impl: BackupStartViewModel): ViewModel
}