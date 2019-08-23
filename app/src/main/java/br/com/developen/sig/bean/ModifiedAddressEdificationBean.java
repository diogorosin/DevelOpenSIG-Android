package br.com.developen.sig.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModifiedAddressEdificationBean {

	private Integer type;

	private String reference;

    private Date from;

    private Date to;

	private Map<Integer, ModifiedAddressEdificationDwellerBean> dwellers;

	public Integer getType() {

		return type;

	}

	public void setType(Integer type) {

		this.type = type;

	}

	public String getReference() {

		return reference;

	}

	public void setReference(String reference) {

		this.reference = reference;

	}
	
	public Date getFrom() {
		
		return from;
		
	}

	public void setFrom(Date from) {
		
		this.from = from;
		
	}

	public Date getTo() {

		return to;

	}

	public void setTo(Date to) {

		this.to = to;

	}

	public Map<Integer, ModifiedAddressEdificationDwellerBean> getDwellers() {

		if (dwellers==null) 

			dwellers = new HashMap<Integer, ModifiedAddressEdificationDwellerBean>();

		return dwellers;

	}

	public void setDwellers(Map<Integer, ModifiedAddressEdificationDwellerBean> dwellers) {

		this.dwellers = dwellers;

	}

}