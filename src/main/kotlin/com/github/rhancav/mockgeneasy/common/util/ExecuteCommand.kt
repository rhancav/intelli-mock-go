package com.github.rhancav.mockgeneasy.common.util

import com.github.rhancav.mockgeneasy.common.PluginConstants.Commands
import com.github.rhancav.mockgeneasy.common.exception.UnsupportedOperatingSystemException
import com.intellij.openapi.util.SystemInfo
import java.io.BufferedReader
import java.io.IOException

fun String.executeCommand(): Pair<Boolean, String?> {
    return try {
        val shellCommand = getShellCommand(this)
        val processBuilder = ProcessBuilder(*shellCommand)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val output = process.inputStream.bufferedReader().use(BufferedReader::readText).trim()
        val isSuccess = process.waitFor() == 0
        Pair(isSuccess, output)
    } catch (e: IOException) {
        e.printStackTrace()
        Pair(false, "I/O error: ${e.message}")
    } catch (e: UnsupportedOperatingSystemException) {
        e.printStackTrace()
        Pair(false, "Unsupported OS: ${e.message}")
    } catch (e: Exception) {
        e.printStackTrace()
        Pair(false, "Error: ${e.message}")
    }
}

private fun getShellCommand(cmd: String): Array<String> {
    return if (SystemInfo.isWindows) {
        arrayOf(*Commands.WIN_SHELL, cmd)
    } else if (SystemInfo.isMac || SystemInfo.isLinux) {
        arrayOf(*Commands.UNIX_SHELL, cmd)
    } else throw UnsupportedOperatingSystemException(SystemInfo.getOsName())
}