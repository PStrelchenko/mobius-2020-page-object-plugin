package com.mobius.idea_plugins.page_object.actions.create_page_object

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages


class CreatePageObjectAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        println("Hello, world!")
        Messages.showMessageDialog("My message", "My title", Messages.getInformationIcon())
    }

}