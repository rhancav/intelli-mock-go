package com.github.rhancav.mockgeneasy.ui

import com.github.rhancav.mockgeneasy.cache.DirectoryAutoCompletionCache
import com.github.rhancav.mockgeneasy.common.PluginConstants
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField


class GenerateMockDialog(private val project: Project, private val file: VirtualFile) :
    DialogWrapper(project) {

    private lateinit var contentPane: JPanel
    private lateinit var directoryField: TextFieldWithAutoCompletionAndFileBrowser
    private lateinit var packageNameField: JTextField

    init {
        init()
        title = PluginConstants.UITexts.ACTION_TITLE
        setPreferredSizeBasedOnPath()
    }

    override fun createCenterPanel(): JComponent {
        contentPane = JPanel(GridBagLayout())
        val gbc = GridBagConstraints()

        directoryField = TextFieldWithAutoCompletionAndFileBrowser(
            project,
            DirectoryAutoCompletionCache,
        )

        directoryField.childComponent.text = file.parent.presentableUrl

        packageNameField = JTextField(PluginConstants.UITexts.DEFAULT_PACKAGE)

        gbc.gridx = 0
        gbc.gridy = 0
        gbc.insets = JBUI.insets(5)
        gbc.anchor = GridBagConstraints.WEST
        contentPane.add(JLabel(PluginConstants.UITexts.DESTINATION))

        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridwidth = 2
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0
        contentPane.add(directoryField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.gridwidth = 1
        gbc.weightx = 0.0
        contentPane.add(JLabel(PluginConstants.UITexts.PACKAGE_NAME), gbc)

        gbc.gridx = 1
        gbc.gridy = 1
        gbc.gridwidth = 2
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.weightx = 1.0
        contentPane.add(packageNameField, gbc)

        return contentPane
    }

    private fun setPreferredSizeBasedOnPath() {
        val pathLength = file.parent.presentableUrl.length
        val width = pathLength * 10
        val height = 150
        setSize(width, height)
    }

    fun getSelectedDirectory(): String {
        return directoryField.childComponent.text
    }

    fun getPackageName(): String {
        return packageNameField.text
    }
}
