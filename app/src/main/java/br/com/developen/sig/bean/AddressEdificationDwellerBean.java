package br.com.developen.sig.bean;

import java.util.Date;
import java.util.Objects;

public class AddressEdificationDwellerBean {


	private Integer address;

	private Integer edification;

	private Integer dweller;

	private Integer individual;

	private Date from;

	private Date to;


	public Integer getAddress() {

		return address;

	}


	public void setAddress(Integer address) {
		
		this.address = address;
		
	}


	public Integer getEdification() {
		
		return edification;
		
	}


	public void setEdification(Integer edification) {
		
		this.edification = edification;
		
	}


    public Integer getDweller() {

        return dweller;

    }


    public void setDweller(Integer dweller) {

        this.dweller = dweller;

    }


    public Integer getIndividual() {

		return individual;

	}


	public void setIndividual(Integer individual) {

		this.individual = individual;

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


    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEdificationDwellerBean that = (AddressEdificationDwellerBean) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(edification, that.edification) &&
                Objects.equals(dweller, that.dweller);

    }


    public int hashCode() {

        return Objects.hash(address, edification, dweller);

    }


}