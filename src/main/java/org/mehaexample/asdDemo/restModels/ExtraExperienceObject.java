package org.mehaexample.asdDemo.restModels;

import java.util.Date;

public class ExtraExperienceObject {
	
	private int extraExperienceId;
	private String neuId;
	private String companyName;
	private String startDate;
	private String endDate;
	private String title;
	private String description;
	
	public ExtraExperienceObject() {
		super();
	}

	public ExtraExperienceObject(int extraExperienceId, String neuId, String companyName, String startDate,
			String endDate, String title, String description) {
		super();
		this.extraExperienceId = extraExperienceId;
		this.neuId = neuId;
		this.companyName = companyName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.title = title;
		this.description = description;
	}

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
