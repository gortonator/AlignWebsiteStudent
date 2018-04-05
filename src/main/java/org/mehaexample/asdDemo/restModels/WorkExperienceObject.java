package org.mehaexample.asdDemo.restModels;

public class WorkExperienceObject {
	private int workExperienceId;
	private String neuId;
	private String companyName;
	private String startDate;
	private String endDate;
	private boolean currentJob;
	private boolean coop;
	private String title;
	private String description;
	
	public WorkExperienceObject() {
		super();
	}

	public WorkExperienceObject(int workExperienceId, String neuId, String companyName, String startDate,
			String endDate, boolean currentJob, boolean coop, String title, String description) {
		super();
		this.workExperienceId = workExperienceId;
		this.neuId = neuId;
		this.companyName = companyName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.currentJob = currentJob;
		this.coop = coop;
		this.title = title;
		this.description = description;
	}

	public int getWorkExperienceId() {
		return workExperienceId;
	}

	public void setWorkExperienceId(int workExperienceId) {
		this.workExperienceId = workExperienceId;
	}

	public String getNeuId() {
		return neuId;
	}

	public void setNeuId(String neuId) {
		this.neuId = neuId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public boolean isCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(boolean currentJob) {
		this.currentJob = currentJob;
	}

	public boolean isCoop() {
		return coop;
	}

	public void setCoop(boolean coop) {
		this.coop = coop;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
