package com.mobius.idea_plugins.page_object.actions.create_page_object

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlFile
import com.mobius.idea_plugins.page_object.core.XmlLayoutCodeInsightAction


class CreatePageObjectAction : XmlLayoutCodeInsightAction() {

    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val dialog = CreatePageObjectDialog(psiFile as XmlFile).also { it.show() }
        if (dialog.isOK) {
            // TODO
        }
    }

}