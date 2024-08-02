package com.github.rhancav.intellimockgo.ui

import com.github.rhancav.intellimockgo.common.PluginConstants
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.openapi.ui.TextComponentAccessor
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.TextFieldWithAutoCompletionWithCache
import com.intellij.util.textCompletion.TextCompletionCache

class TextFieldWithAutoCompletionAndFileBrowser(
    project: Project,
    cache: TextCompletionCache<String>
) :
    ComponentWithBrowseButton<TextFieldWithAutoCompletion<String>>(
        TextFieldWithAutoCompletionWithCache.create(
            cache,
            true,
            project,
            null,
            true,
            null,
            true,
            true,
            false
        ),
        null
    ) {
    init {
        addBrowseFolderListener(
            PluginConstants.UITexts.DESTINATION,
            "",
            project,
            FileChooserDescriptorFactory.createSingleLocalFileDescriptor(),
            CustomTextComponentAccessor
        )
    }

    private object CustomTextComponentAccessor :
        TextComponentAccessor<TextFieldWithAutoCompletion<String>> {
        override fun getText(component: TextFieldWithAutoCompletion<String>?): String {
            return component!!.text
        }

        override fun setText(component: TextFieldWithAutoCompletion<String>?, text: String) {
            component?.text = text
        }

    }
}