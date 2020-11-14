package com.mobius.idea_plugins.page_object.extensions

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages


fun Project.showErrorDialog(errorMessage: String) {
    Messages.showErrorDialog(this, errorMessage, "Error!")
}