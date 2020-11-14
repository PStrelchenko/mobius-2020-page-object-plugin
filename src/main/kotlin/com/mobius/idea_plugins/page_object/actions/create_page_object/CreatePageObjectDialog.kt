package com.mobius.idea_plugins.page_object.actions.create_page_object

import com.intellij.ide.util.DirectoryChooser
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo
import com.intellij.ui.layout.panel
import com.mobius.idea_plugins.page_object.extensions.isQualifiedName
import com.mobius.idea_plugins.page_object.extensions.isValidIdentifier
import com.mobius.idea_plugins.page_object.extensions.pageObjectClassName
import com.mobius.idea_plugins.page_object.extensions.showErrorDialog
import org.jetbrains.kotlin.idea.refactoring.ui.KotlinDestinationFolderComboBox
import javax.swing.JComponent


class CreatePageObjectDialog(
    private val xmlFile: XmlFile
) : DialogWrapper(xmlFile.project) {

    companion object {
        private const val RECENTS_KEY = "CreatePageObjectDialog.recents_key"
    }

    private val project: Project get() = xmlFile.project


    var className: String = xmlFile.pageObjectClassName

    private lateinit var classPackageChooser: PackageNameReferenceEditorCombo
    private lateinit var destinationFolderCB: KotlinDestinationFolderComboBox


    init {
        init()
        title = "Create Page Object"
    }

    override fun createCenterPanel(): JComponent? {
        return panel {
            titledRow("Enter class name:") {
                row {
                    textField(this@CreatePageObjectDialog::className)
                }
            }
            titledRow("Choose your target package:") {
                row {
                    val initialPackage = "com.example.myapplication"
                    classPackageChooser = PackageNameReferenceEditorCombo(
                        initialPackage,
                        project,
                        RECENTS_KEY,
                        "Choose your package"
                    )
                    classPackageChooser()
                }
                row {
                    destinationFolderCB = object : KotlinDestinationFolderComboBox() {
                        override fun getTargetPackage(): String {
                            return this@CreatePageObjectDialog.getTargetPackage()
                        }
                    }
                    destinationFolderCB.setData(
                        project,
                        xmlFile.containingDirectory,
                        classPackageChooser.childComponent
                    )
                    destinationFolderCB()
                }
            }
        }
    }

    override fun doOKAction() {
        if (isFormValid()) {
            super.doOKAction()
        }
    }

    fun getResult(): CreatePageObjectDialogResult {
        return CreatePageObjectDialogResult(
            xmlFile = xmlFile,
            className = getCurrentClassName(),
            packageName = getTargetPackage(),
            targetPsiDirectory = getTargetPsiDirectory()!!
        )
    }

    private fun isFormValid(): Boolean {
        return isClassNameValid() && isPackageNameValid() && isTargetPackageValid()
    }

    private fun isClassNameValid(): Boolean {
        val currentClassName = getCurrentClassName()

        return when {
            currentClassName.isBlank() -> {
                project.showErrorDialog("Class name is blank!")
                false
            }

            currentClassName.isValidIdentifier(project).not() -> {
                project.showErrorDialog("Class name is not valid identifier!")
                false
            }

            else -> {
                true
            }
        }
    }

    private fun isPackageNameValid(): Boolean {
        val currentPackageName = getTargetPackage()

        return when {
            currentPackageName.isBlank() -> {
                project.showErrorDialog("Package name is blank!")
                false
            }

            currentPackageName.isQualifiedName(project).not() -> {
                project.showErrorDialog("Package name is not valid!")
                false
            }

            else -> {
                true
            }
        }
    }

    private fun isTargetPackageValid(): Boolean {
        return when (getTargetPsiDirectory()) {
            null -> {
                project.showErrorDialog("Target package is blank!")
                false
            }

            else -> {
                true
            }
        }
    }

    private fun getTargetPackage(): String {
        return classPackageChooser.text.trim { it <= ' ' }
    }

    private fun getCurrentClassName(): String {
        return className
    }

    private fun getTargetPsiDirectory(): PsiDirectory? {
        val selectedItem = destinationFolderCB.comboBox.selectedItem as DirectoryChooser.ItemWrapper
        return selectedItem.directory
    }

}