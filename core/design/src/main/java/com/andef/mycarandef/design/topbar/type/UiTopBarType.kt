package com.andef.mycarandef.design.topbar.type

sealed class UiTopBarType {
    data object Center : UiTopBarType()
    data object NotCenter : UiTopBarType()
}