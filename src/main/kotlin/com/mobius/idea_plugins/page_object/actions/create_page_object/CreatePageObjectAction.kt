package com.mobius.idea_plugins.page_object.actions.create_page_object

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.xml.XmlFile
import com.mobius.idea_plugins.page_object.core.XmlLayoutCodeInsightAction
import com.mobius.idea_plugins.page_object.extensions.collectAndroidTagsInfoList
import com.mobius.idea_plugins.page_object.model.AndroidTagDeclaration.*
import com.mobius.idea_plugins.page_object.model.AndroidTagInfoModel
import org.jetbrains.kotlin.idea.core.ShortenReferences
import org.jetbrains.kotlin.idea.formatter.commitAndUnblockDocument
import org.jetbrains.kotlin.idea.util.application.executeWriteCommand
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory


class CreatePageObjectAction : XmlLayoutCodeInsightAction() {

    companion object {
        private const val COMMAND_NAME = "CreatePageObjectActionCommand"
    }


    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {
        val dialog = CreatePageObjectDialog(psiFile as XmlFile).also { it.show() }
        if (dialog.isOK) {
            handleDialogResult(dialog.getResult())
        }
    }


    private fun handleDialogResult(result: CreatePageObjectDialogResult) {
        val project = result.xmlFile.project
        val pageObjectClassText = getPageObjectClassText(result)

        val ktPsiFactory = KtPsiFactory(project)
        val pageObjectKtFile = ktPsiFactory.createFile(result.getPageObjectClassFileName(), pageObjectClassText)

        project.executeWriteCommand(COMMAND_NAME) {
            val addedFile = result.targetPsiDirectory.add(pageObjectKtFile) as KtFile

            addedFile.commitAndUnblockDocument()
            ShortenReferences.DEFAULT.process(addedFile)
            CodeStyleManager.getInstance(project).reformat(addedFile)
        }
    }


    private fun getPageObjectClassText(result: CreatePageObjectDialogResult): String {
        val androidTagsInfoList = result.xmlFile.collectAndroidTagsInfoList()
        val properties = androidTagsInfoList.map { it.toPropertyDeclaration() }

        return """
        package ${result.packageName}
            
        class ${result.className} : com.agoda.kakao.screen.Screen<${result.className}>() {
        
            ${properties.joinToString(separator = "\n")}

        }
        """
    }

    private fun CreatePageObjectDialogResult.getPageObjectClassFileName(): String {
        return "${className}.kt"
    }

    private fun AndroidTagInfoModel.toPropertyDeclaration(): String {
        val kakaoWidgetFQCN = toKakaoWidgetFQCN()
        val propertyName = id.decapitalize()

        return "private val $propertyName = $kakaoWidgetFQCN { withId(R.id.${id}) }"
    }

    private fun AndroidTagInfoModel.toKakaoWidgetFQCN(): String {
        return when (androidTagDeclaration) {
            CHECK_BOX -> "com.agoda.kakao.check.KCheckBox"
            BUTTON -> "com.agoda.kakao.text.KButton"
            TEXT_VIEW -> "com.agoda.kakao.text.KTextView"
            VIEW_GROUP, VIEW -> "com.agoda.kakao.common.views.KView"
        }
    }

}