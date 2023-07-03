package com.pennant.prodmtr.model.Dto;

import com.pennant.prodmtr.model.Entity.FunctionalUnit;

public class FunctionalUnitdto {
	private int modl_id;
	private int funt_id;
	private String funit_desc;
	private int prj_id;
	private String status;

	public int getPrj_id() {
		return prj_id;
	}

	public void setPrj_id(int prj_id) {
		this.prj_id = prj_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FunctionalUnitdto() {
		// TODO Auto-generated constructor stub
	}

	public int getModl_id() {
		return modl_id;
	}

	public void setModl_id(int modl_id) {
		this.modl_id = modl_id;
	}

	public int getFunt_id() {
		return funt_id;
	}

	public void setFunt_id(int funt_id) {
		this.funt_id = funt_id;
	}

	public String getFunit_desc() {
		return funit_desc;
	}

	public void setFunit_desc(String funit_desc) {
		this.funit_desc = funit_desc;
	}

	public static FunctionalUnitdto fromEntity(FunctionalUnit funit) {
		FunctionalUnitdto funiotdto = new FunctionalUnitdto();
		funiotdto.setModl_id(funit.getId().getFunitid());
		funiotdto.setFunt_id(funit.getId().getFunitid());
		funiotdto.setFunit_desc(funit.getDescription());
		funiotdto.setStatus(funit.getFunStatus());
		funiotdto.setPrj_id(funit.getProjectId().getProjectId());

		return funiotdto;

	}

}
