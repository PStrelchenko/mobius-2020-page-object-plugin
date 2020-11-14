package com.mobius.idea_plugins.page_object.extensions

import android.databinding.tool.ext.toCamelCase
import com.android.SdkConstants
import com.android.tools.idea.psi.TagToClassMapper
import com.intellij.psi.util.InheritanceUtil
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.mobius.idea_plugins.page_object.model.AndroidTagDeclaration
import com.mobius.idea_plugins.page_object.model.AndroidTagInfoModel
import org.jetbrains.kotlin.idea.util.module


val XmlFile.pageObjectClassName: String get() = name.removeSuffix(".xml").toCamelCase().capitalize()

fun XmlFile.collectAndroidTagsInfoList(): List<AndroidTagInfoModel> {
    return document?.collectXmlTags()?.mapNotNull { it.toAndroidTagInfoModel() } ?: emptyList()
}


private fun XmlDocument.collectXmlTags(): List<XmlTag> {
    val result = mutableListOf<XmlTag>()

    rootTag?.collectTagsRecursively(result)

    return result
}

private fun XmlTag.collectTagsRecursively(result: MutableList<XmlTag>) {
    result += this
    this.children.filterIsInstance<XmlTag>().forEach { it.collectTagsRecursively(result) }
}

private fun XmlTag.toAndroidTagInfoModel(): AndroidTagInfoModel? {
    val idAttr = this.attributes.firstOrNull { it.localName == "id" }
    val idAttrValue = idAttr?.value?.removePrefix(SdkConstants.NEW_ID_PREFIX) ?: return null

    val tagPsiClass = TagToClassMapper.getInstance(this.containingFile?.module!!)
        .getClassMap(SdkConstants.CLASS_VIEW)[localName] ?: return null

    val declaration = AndroidTagDeclaration.values().first {
        it.widgets.any { widget -> InheritanceUtil.isInheritor(tagPsiClass, widget) }
    }

    return AndroidTagInfoModel(
        id = idAttrValue,
        androidTagDeclaration = declaration
    )
}