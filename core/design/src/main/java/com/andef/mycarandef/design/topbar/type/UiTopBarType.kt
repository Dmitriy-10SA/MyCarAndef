package com.andef.mycarandef.design.topbar.type

sealed class UiTopBarType {
    data object Center : UiTopBarType()
    data object NotCenter : UiTopBarType()
    data class WithTabs(
        val tabs: List<UiTopBarTab>,
        val selectedTabIndex: Int,
        val onTabClick: (UiTopBarTab) -> Unit
    ) : UiTopBarType()
}

open class UiTopBarTab(val id: Int, val title: String)