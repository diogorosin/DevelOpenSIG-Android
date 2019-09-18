package br.com.developen.sig.bean;

import java.util.ArrayList;
import java.util.List;

public class UploadDatasetBean {

	private List<ModifiedAddressBean> modifiedAddresses;

	public List<ModifiedAddressBean> getModifiedAddresses() {

		if (modifiedAddresses == null)

			modifiedAddresses = new ArrayList<>();

		return modifiedAddresses;

	}

	public void setModifiedAddresses(List<ModifiedAddressBean> modifiedAddresses) {

		this.modifiedAddresses = modifiedAddresses;

	}

}