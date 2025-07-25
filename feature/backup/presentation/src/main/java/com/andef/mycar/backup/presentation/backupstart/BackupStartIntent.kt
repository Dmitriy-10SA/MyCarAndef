package com.andef.mycar.backup.presentation.backupstart

import com.andef.mycar.backup.domain.BackupData

sealed class BackupStartIntent {
    data class RestoreDate(
        val data: BackupData,
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : BackupStartIntent()
}