package com.mobius.idea_plugins.page_object.extensions

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiNameHelper


fun String.isValidIdentifier(project: Project): Boolean {
    val nameHelper = PsiNameHelper.getInstance(project)

    return nameHelper.isIdentifier(this)
}

fun String.isQualifiedName(project: Project): Boolean {
    val nameHelper = PsiNameHelper.getInstance(project)

    return nameHelper.isQualifiedName(this)
}