package com.mobius.idea_plugins.page_object.extensions

import android.databinding.tool.ext.toCamelCase
import com.android.SdkConstants
import com.android.tools.idea.psi.TagToClassMapper
import com.intellij.psi.XmlRecursiveElementVisitor
import com.intellij.psi.util.InheritanceUtil
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.mobius.idea_plugins.page_object.model.AndroidTagDeclaration
import com.mobius.idea_plugins.page_object.model.AndroidTagInfoModel
import org.jetbrains.kotlin.idea.util.module

private const val XML_FILE_EXTENSION = ".xml"


val XmlFile.pageObjectClassName: String get() = name.removeSuffix(XML_FILE_EXTENSION).toCamelCase().capitalize()

fun XmlFile.collectAndroidTagsInfoList(): List<AndroidTagInfoModel> {
    val result = mutableListOf<AndroidTagInfoModel>()

    this.accept(object : XmlRecursiveElementVisitor() {
        override fun visitXmlTag(tag: XmlTag?) {
            // DomManager doesn't work as expected, always returns null,
            // because getDomElement returns LayoutViewElement, that cannot be converted into View ;(
//            val tagPsiClass = (DomManager.getDomManager(project).getDomElement(tag) as? View)?.viewClass?.value
            val tagPsiClass = tag?.module?.let {
                TagToClassMapper.getInstance(it).getClassMap(SdkConstants.CLASS_VIEW)[tag.localName]
            }

            val idAttrValue = tag
                ?.getAttributeValue("${SdkConstants.ANDROID_NS_NAME_PREFIX}${SdkConstants.ATTR_ID}")
                ?.removePrefix(SdkConstants.NEW_ID_PREFIX)

            if (idAttrValue != null && tagPsiClass != null) {
                val declaration = AndroidTagDeclaration.values().first { declaration ->
                    declaration.widgetFQCNs.any { widgetFQCN -> InheritanceUtil.isInheritor(tagPsiClass, widgetFQCN) }
                }
                result += AndroidTagInfoModel(
                    id = idAttrValue,
                    androidTagDeclaration = declaration
                )
            }
            super.visitXmlTag(tag)
        }
    })

    return result
}