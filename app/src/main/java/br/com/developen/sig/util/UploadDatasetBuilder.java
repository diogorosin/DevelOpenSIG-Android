package br.com.developen.sig.util;

import br.com.developen.sig.bean.ModifiedAddressBean;
import br.com.developen.sig.bean.ModifiedAddressEdificationBean;
import br.com.developen.sig.bean.ModifiedAddressEdificationDwellerBean;
import br.com.developen.sig.bean.UploadDatasetBean;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.ModifiedAddressModel;

public class UploadDatasetBuilder {


    private UploadDatasetBean uploadDatasetBean = new UploadDatasetBean();


    public UploadDatasetBuilder withModifiedAddresses(){

        for (ModifiedAddressModel modifiedAddressModel:
                App.
                getInstance().
                getModifiedAddressRepository().
                getModifiedAddressesThatWasNotSyncedAsList()) {


            ModifiedAddressBean modifiedAddressBean = new ModifiedAddressBean();

            modifiedAddressBean.setIdentifier(modifiedAddressModel.getIdentifier());

            modifiedAddressBean.setAddress(modifiedAddressModel.getAddress());

            modifiedAddressBean.setDenomination(modifiedAddressModel.getDenomination());

            modifiedAddressBean.setNumber(modifiedAddressModel.getNumber());

            modifiedAddressBean.setReference(modifiedAddressModel.getReference());

            modifiedAddressBean.setDistrict(modifiedAddressModel.getDistrict());

            modifiedAddressBean.setCity(modifiedAddressModel.getCity().getIdentifier());

            modifiedAddressBean.setPostalCode(modifiedAddressModel.getPostalCode());

            modifiedAddressBean.setLatitude(modifiedAddressModel.getLatLng().getLatitude());

            modifiedAddressBean.setLongitude(modifiedAddressModel.getLatLng().getLongitude());

            modifiedAddressBean.setModifiedAt(modifiedAddressModel.getModifiedAt());

            modifiedAddressBean.setModifiedBy(modifiedAddressModel.getModifiedBy());


            for (ModifiedAddressEdificationModel modifiedAddressEdificationModel: App.
                    getInstance().
                    getModifiedAddressRepository().
                    getEdificationsOfModifiedAddressAsList(modifiedAddressBean.getIdentifier())) {

                ModifiedAddressEdificationBean modifiedAddressEdificationBean = new ModifiedAddressEdificationBean();

                modifiedAddressEdificationBean.setType(modifiedAddressEdificationModel.getType().getIdentifier());

                modifiedAddressEdificationBean.setReference(modifiedAddressEdificationModel.getReference());

                modifiedAddressEdificationBean.setFrom(modifiedAddressEdificationModel.getFrom());

                modifiedAddressEdificationBean.setTo(modifiedAddressEdificationModel.getTo());


                for (ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel :
                        App.
                        getInstance().
                        getModifiedAddressEdificationRepository().
                        getDwellersOfModifiedAddressEdificationAsList(modifiedAddressEdificationModel.
                                getModifiedAddress().
                                getIdentifier(),
                                modifiedAddressEdificationModel.
                                getEdification())) {

                    ModifiedAddressEdificationDwellerBean modifiedAddressEdificationDwellerBean = new ModifiedAddressEdificationDwellerBean();

                    modifiedAddressEdificationDwellerBean.setName(modifiedAddressEdificationDwellerModel.getName());

                    modifiedAddressEdificationDwellerBean.setMotherName(modifiedAddressEdificationDwellerModel.getMotherName());

                    modifiedAddressEdificationDwellerBean.setFatherName(modifiedAddressEdificationDwellerModel.getFatherName());

                    modifiedAddressEdificationDwellerBean.setCpf(modifiedAddressEdificationDwellerModel.getCpf());

                    modifiedAddressEdificationDwellerBean.setRgNumber(modifiedAddressEdificationDwellerModel.getRgNumber());

                    modifiedAddressEdificationDwellerBean.setRgAgency(modifiedAddressEdificationDwellerModel.getRgAgency().getIdentifier());

                    modifiedAddressEdificationDwellerBean.setRgState(modifiedAddressEdificationDwellerModel.getRgState().getIdentifier());

                    modifiedAddressEdificationDwellerBean.setBirthPlace(modifiedAddressEdificationDwellerModel.getBirthPlace().getIdentifier());

                    modifiedAddressEdificationDwellerBean.setBirthDate(modifiedAddressEdificationDwellerModel.getBirthDate());

                    modifiedAddressEdificationDwellerBean.setGender(modifiedAddressEdificationDwellerModel.getGender().getIdentifier());

                    modifiedAddressEdificationDwellerBean.setIndividual(modifiedAddressEdificationDwellerModel.getIndividual() != null ?
                            modifiedAddressEdificationDwellerModel.getIndividual().getIdentifier() : null);

                    modifiedAddressEdificationDwellerBean.setFrom(modifiedAddressEdificationDwellerModel.getFrom());

                    modifiedAddressEdificationDwellerBean.setTo(modifiedAddressEdificationDwellerModel.getTo());

                    modifiedAddressEdificationBean.
                            getDwellers().
                            put(modifiedAddressEdificationDwellerModel.getDweller(), modifiedAddressEdificationDwellerBean);

                }


                modifiedAddressBean.
                        getEdifications().
                        put(modifiedAddressEdificationModel.getEdification(), modifiedAddressEdificationBean);


            }


            uploadDatasetBean.getModifiedAddresses().add(modifiedAddressBean);


        }

        return this;

    }


    public UploadDatasetBean build(){

        return uploadDatasetBean;

    }


}