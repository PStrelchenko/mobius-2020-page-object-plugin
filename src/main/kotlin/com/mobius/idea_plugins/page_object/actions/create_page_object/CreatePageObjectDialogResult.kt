package com.mobius.idea_plugins.page_object.actions.create_page_object

import com.intellij.psi.PsiDirectory
import com.intellij.psi.xml.XmlFile


data class CreatePageObjectDialogResult(
    val xmlFile: XmlFile,
    val className: String,
    val packageName: String,
    val targetPsiDirectory: PsiDirectory
)