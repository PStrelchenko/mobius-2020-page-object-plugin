package com.mobius.idea_plugins.page_object.core

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile


/**
 * Base action for context defining: this action can be invoked only in XML files inside /layout dir.
 */
abstract class XmlLayoutCodeInsightAction : CodeInsightAction(), CodeInsightActionHandler {

    companion object {
        private const val LAYOUT_DIR_NAME = "layout"
    }


    final override fun startInWriteAction(): Boolean {
        return false
    }

    final override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean {
        return file is XmlFile && file.parent?.name == LAYOUT_DIR_NAME
    }

    override fun getHandler(): CodeInsightActionHandler {
        return this
    }


}