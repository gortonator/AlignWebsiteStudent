package org.mehaexample.asdDemo.model.alignprivate;

import java.util.Date;

public class ExtraExperiences {
	private int extraExperienceId;
	private String neuId;
	private String companyName;
	private Date startDate;
	private Date endDate;
	private String title;
	private String description;

	public ExtraExperiences(String neuId, String companyName, Date startDate, Date endDate,
			String title, String description) {
		this.neuId = neuId;
		this.companyName = companyName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.title = title;
		this.description = description;
	}

	public ExtraExperiences() { }

	public int getExtraExperienceId() {
		return extraExperienceId;
	}

	public void setExtraExperienceId(int extraExperienceId) {
		this.extraExperienceId = extraExperienceId;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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