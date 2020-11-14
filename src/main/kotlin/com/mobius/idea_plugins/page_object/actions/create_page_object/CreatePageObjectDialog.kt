package com.mobius.idea_plugins.page_object.actions.create_page_object

import android.databinding.tool.ext.toCamelCase
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.xml.XmlFile
import com.intellij.refactoring.ui.PackageNameReferenceEditorCombo
import com.intellij.ui.layout.panel
import org.jetbrains.kotlin.idea.refactoring.ui.KotlinDestinationFolderComboBox
import javax.swing.JComponent


class CreatePageObjectDialog(
    private val xmlFile: XmlFile
) : DialogWrapper(xmlFile.project) {

    companion object {
        private const val RECENTS_KEY = "CreatePageObjectDialog.recents_key"
    }

    private val project: Project get() = xmlFile.project


    var className: String = xmlFile.name.removeSuffix(".xml").toCamelCase().capitalize()

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


    private fun getTargetPackage(): String {
        return classPackageChooser.text.trim { it <= ' ' }
    }

}