package com.github.rhancav.intellimockgo.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class GenerateMockAction extends AnAction {


  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    System.out.println("Generate mock clicked");
  }
}
