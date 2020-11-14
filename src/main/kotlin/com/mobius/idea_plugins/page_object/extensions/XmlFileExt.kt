package com.mobius.idea_plugins.page_object.extensions

import android.databinding.tool.ext.toCamelCase
import com.intellij.psi.xml.XmlFile


val XmlFile.pageObjectClassName: String get() = name.removeSuffix(".xml").toCamelCase().capitalize()