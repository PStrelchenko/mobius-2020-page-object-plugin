package com.mobius.idea_plugins.page_object.model

import com.android.SdkConstants


enum class AndroidTagDeclaration(
    val widgetFQCNs: List<String>
) {

    CHECK_BOX(
        widgetFQCNs = listOf(
            SdkConstants.FQCN_CHECK_BOX
        )
    ),
    BUTTON(
        widgetFQCNs = listOf(
            SdkConstants.FQCN_BUTTON
        )
    ),
    TEXT_VIEW(
        widgetFQCNs = listOf(
            SdkConstants.FQCN_TEXT_VIEW
        )
    ),
    VIEW_GROUP(
        widgetFQCNs = listOf(
            SdkConstants.CLASS_VIEWGROUP
        )
    ),
    VIEW(
        widgetFQCNs = listOf(
            SdkConstants.CLASS_VIEW
        )
    )

}