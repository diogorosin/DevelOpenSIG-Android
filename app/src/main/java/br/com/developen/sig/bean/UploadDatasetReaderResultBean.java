package br.com.developen.sig.bean;

import java.util.HashMap;
import java.util.Map;

public class UploadDatasetReaderResultBean {

	private Map<Integer, Boolean> modifiedAddressesThatWasImported;

	public Map<Integer, Boolean> getModifiedAddressesThatWasImported() {

		if (modifiedAddressesThatWasImported == null)

			modifiedAddressesThatWasImported = new HashMap<>();

		return modifiedAddressesThatWasImported;

	}

}
