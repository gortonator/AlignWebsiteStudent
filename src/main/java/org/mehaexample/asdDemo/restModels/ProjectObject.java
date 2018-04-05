package org.mehaexample.asdDemo.restModels;

public class ProjectObject {
	private int projectId;
	private String neuId;
	private String projectName;
	private String startDate;
	private String endDate;
	private String description;

	public ProjectObject() {
		super();
	}

	public ProjectObject(int projectId, String neuId, String projectName, String startDate, String endDate,
			String description) {
		super();
		this.projectId = projectId;
		this.neuId = neuId;
		this.projectName = projectName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getNeuId() {
		return neuId;
	}

	public void setNeuId(String neuId) {
		this.neuId = neuId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
