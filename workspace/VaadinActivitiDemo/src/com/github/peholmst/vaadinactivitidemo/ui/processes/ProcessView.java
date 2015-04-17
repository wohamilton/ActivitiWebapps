package com.github.peholmst.vaadinactivitidemo.ui.processes;

import java.util.List;

import org.activiti.engine.repository.ProcessDefinition;

import com.github.peholmst.mvp4vaadin.navigation.ControllableView;

public interface ProcessView extends ControllableView {

	String VIEW_ID = "process";

	void setProcessDefinitions(List<ProcessDefinition> definitions);

	void showProcessStartSuccess(ProcessDefinition process);

	void showProcessStartFailure(ProcessDefinition process);
}
