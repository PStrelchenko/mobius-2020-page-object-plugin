package com.mobius.idea_plugins.page_object.model

import com.android.SdkConstants


enum class AndroidTagDeclaration(
    val widgets: List<String>
) {

    CHECK_BOX(
        widgets = listOf(
            SdkConstants.FQCN_CHECK_BOX
        )
    ),
    BUTTON(
        widgets = listOf(
            SdkConstants.FQCN_BUTTON
        )
    ),
    TEXT_VIEW(
        widgets = listOf(
            SdkConstants.FQCN_TEXT_VIEW
        )
    ),
    VIEW_GROUP(
        widgets = listOf(
            SdkConstants.CLASS_VIEWGROUP
        )
    ),
    VIEW(
        widgets = listOf(
            SdkConstants.CLASS_VIEW
        )
    )

}