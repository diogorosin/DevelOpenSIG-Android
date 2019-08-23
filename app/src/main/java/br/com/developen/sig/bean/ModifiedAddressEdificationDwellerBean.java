package br.com.developen.sig.bean;

import java.util.Date;


public class ModifiedAddressEdificationDwellerBean {

	private Integer subject;

    private String type;

    private String nameOrDenomination;

    private String fancyName;

    private String motherName;

    private String fatherName;

    private Long cpf;

    private Long rgNumber;

    private Integer rgAgency;

    private Integer rgState;

    private Integer birthPlace;

    private Date birthDate;

    private String gender;

	private Date from;

	private Date to;

	public Integer getSubject() {

		return subject;

	}

	public void setSubject(Integer subject) {

		this.subject = subject;

	}

	public String getNameOrDenomination() {

		return nameOrDenomination;

	}

	public void setNameOrDenomination(String nameOrDenomination) {

		this.nameOrDenomination = nameOrDenomination;

	}

	public String getType() {

		return type;

	}

	public void setType(String type) {

		this.type = type;

	}

	public String getFancyName() {

		return fancyName;

	}

	public void setFancyName(String fancyName) {

		this.fancyName = fancyName;

	}

	public String getMotherName() {

		return motherName;

	}

	public void setMotherName(String motherName) {

		this.motherName = motherName;

	}

	public String getFatherName() {
		
		return fatherName;

	}

	public void setFatherName(String fatherName) {
		
		this.fatherName = fatherName;
		
	}

	public Long getCpf() {
		
		return cpf;
		
	}

	public void setCpf(Long cpf) {
		
		this.cpf = cpf;
		
	}

	public Long getRgNumber() {
		
		return rgNumber;
		
	}

	public void setRgNumber(Long rgNumber) {
		
		this.rgNumber = rgNumber;
		
	}

	public Integer getRgAgency() {
		
		return rgAgency;
		
	}

	public void setRgAgency(Integer rgAgency) {
		
		this.rgAgency = rgAgency;
		
	}

	public Integer getRgState() {
		
		return rgState;
		
	}

	public void setRgState(Integer rgState) {
		
		this.rgState = rgState;
		
	}

	public Integer getBirthPlace() {
		
		return birthPlace;
		
	}

	public void setBirthPlace(Integer birthPlace) {
		
		this.birthPlace = birthPlace;
		
	}

	public Date getBirthDate() {
		
		return birthDate;
		
	}

	public void setBirthDate(Date birthDate) {

		this.birthDate = birthDate;

	}

	public String getGender() {
		
		return gender;

	}

	public void setGender(String gender) {

		this.gender = gender;

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

}