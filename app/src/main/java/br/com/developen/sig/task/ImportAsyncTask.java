package br.com.developen.sig.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import br.com.developen.sig.bean.AddressBean;
import br.com.developen.sig.bean.AddressEdificationBean;
import br.com.developen.sig.bean.AddressEdificationDwellerBean;
import br.com.developen.sig.bean.AgencyBean;
import br.com.developen.sig.bean.CityBean;
import br.com.developen.sig.bean.CountryBean;
import br.com.developen.sig.bean.DatasetBean;
import br.com.developen.sig.bean.IndividualBean;
import br.com.developen.sig.bean.OrganizationBean;
import br.com.developen.sig.bean.StateBean;
import br.com.developen.sig.bean.TypeBean;
import br.com.developen.sig.database.AddressDAO;
import br.com.developen.sig.database.AddressEdificationDAO;
import br.com.developen.sig.database.AddressEdificationDwellerDAO;
import br.com.developen.sig.database.AddressEdificationDwellerVO;
import br.com.developen.sig.database.AddressEdificationVO;
import br.com.developen.sig.database.AddressVO;
import br.com.developen.sig.database.AgencyDAO;
import br.com.developen.sig.database.AgencyVO;
import br.com.developen.sig.database.CityDAO;
import br.com.developen.sig.database.CityVO;
import br.com.developen.sig.database.CountryDAO;
import br.com.developen.sig.database.CountryVO;
import br.com.developen.sig.database.IndividualDAO;
import br.com.developen.sig.database.IndividualVO;
import br.com.developen.sig.database.OrganizationDAO;
import br.com.developen.sig.database.OrganizationVO;
import br.com.developen.sig.database.StateDAO;
import br.com.developen.sig.database.StateVO;
import br.com.developen.sig.database.SubjectDAO;
import br.com.developen.sig.database.SubjectVO;
import br.com.developen.sig.database.TypeDAO;
import br.com.developen.sig.database.TypeVO;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class ImportAsyncTask<L extends ImportAsyncTask.Listener > extends AsyncTask<DatasetBean, Integer, Object> {


    private WeakReference<L> listener;


    public ImportAsyncTask(L activity) {

        this.listener = new WeakReference<>(activity);

    }


    protected void onPreExecute() {

        L listener = this.listener.get();

        if (listener != null)

            listener.onImportPreExecute();

    }


    protected void onProgressUpdate(Integer... progress){

        L listener = this.listener.get();

        if (listener != null)

            listener.onImportProgressUpdate(progress[0]);

    }


    public void onCancelled(){

        L listener = this.listener.get();

        if (listener != null)

            listener.onImportCancelled();

    }


    protected Object doInBackground(DatasetBean... params) {

        DatasetBean datasetBean = params[0];

        DB database = DB.getInstance(App.getInstance());

        database.runInTransaction(() -> {

            publishProgress(1);

            if (datasetBean.getTypes() != null &&
                    !datasetBean.getTypes().isEmpty()){

                TypeDAO typeDAO = database.typeDAO();

                for (TypeBean typeBean: datasetBean.getTypes()) {

                    TypeVO typeVO = new TypeVO();

                    typeVO.setIdentifier(typeBean.getIdentifier());

                    typeVO.setDenomination(typeBean.getDenomination());

                    if (typeDAO.exists(typeVO.getIdentifier()))

                        typeDAO.update(typeVO);

                    else

                        typeDAO.create(typeVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getAgencies() != null &&
                    !datasetBean.getAgencies().isEmpty()){

                AgencyDAO agencyDAO = database.agencyDAO();

                for (AgencyBean agencyBean: datasetBean.getAgencies()) {

                    AgencyVO agencyVO = new AgencyVO();

                    agencyVO.setIdentifier(agencyBean.getIdentifier());

                    agencyVO.setDenomination(agencyBean.getDenomination());

                    agencyVO.setAcronym(agencyBean.getAcronym());

                    if (agencyDAO.exists(agencyVO.getIdentifier()))

                        agencyDAO.update(agencyVO);

                    else

                        agencyDAO.create(agencyVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getCountries() != null &&
                    !datasetBean.getCountries().isEmpty()){

                CountryDAO countryDAO = database.countryDAO();

                for (CountryBean countryBean: datasetBean.getCountries()) {

                    CountryVO countryVO = new CountryVO();

                    countryVO.setIdentifier(countryBean.getIdentifier());

                    countryVO.setDenomination(countryBean.getDenomination());

                    countryVO.setAcronym(countryBean.getAcronym());

                    if (countryDAO.exists(countryVO.getIdentifier()))

                        countryDAO.update(countryVO);

                    else

                        countryDAO.create(countryVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getStates() != null &&
                    !datasetBean.getStates().isEmpty()){

                StateDAO stateDAO = database.stateDAO();

                for (StateBean stateBean: datasetBean.getStates()) {

                    StateVO stateVO = new StateVO();

                    stateVO.setIdentifier(stateBean.getIdentifier());

                    stateVO.setDenomination(stateBean.getDenomination());

                    stateVO.setAcronym(stateBean.getAcronym());

                    stateVO.setCountry(stateBean.getCountry());

                    if (stateDAO.exists(stateVO.getIdentifier()))

                        stateDAO.update(stateVO);

                    else

                        stateDAO.create(stateVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getCities() != null &&
                    !datasetBean.getCities().isEmpty()){

                CityDAO cityDAO = database.cityDAO();

                for (CityBean cityBean: datasetBean.getCities()) {

                    CityVO cityVO = new CityVO();

                    cityVO.setIdentifier(cityBean.getIdentifier());

                    cityVO.setDenomination(cityBean.getDenomination());

                    cityVO.setState(cityBean.getState());

                    if (cityDAO.exists(cityVO.getIdentifier()))

                        cityDAO.update(cityVO);

                    else

                        cityDAO.create(cityVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getIndividuals() != null &&
                    !datasetBean.getIndividuals().isEmpty()){

                SubjectDAO subjectDAO = database.subjectDAO();

                IndividualDAO individualDAO = database.individualDAO();

                for (IndividualBean individualBean: datasetBean.getIndividuals()) {

                    SubjectVO subjectVO = new SubjectVO();

                    subjectVO.setIdentifier(individualBean.getIdentifier());

                    if (subjectDAO.exists(subjectVO.getIdentifier()))

                        subjectDAO.update(subjectVO);

                    else

                        subjectDAO.create(subjectVO);

                    IndividualVO individualVO = new IndividualVO();

                    individualVO.setIdentifier(individualBean.getIdentifier());

                    individualVO.setActive(individualBean.getActive());

                    individualVO.setName(individualBean.getName());

                    individualVO.setMotherName(individualBean.getMotherName());

                    individualVO.setFatherName(individualBean.getFatherName());

                    individualVO.setCpf(individualBean.getCpf());

                    individualVO.setRgNumber(individualBean.getRgNumber());

                    individualVO.setRgAgency(individualBean.getRgAgency());

                    individualVO.setRgState(individualBean.getRgState());

                    individualVO.setBirthDate(individualBean.getBirthDate());

                    individualVO.setBirthPlace(individualBean.getBirthPlace());

                    individualVO.setGender(individualBean.getGender());

                    if (individualDAO.exists(individualVO.getIdentifier()))

                        individualDAO.update(individualVO);

                    else

                        individualDAO.create(individualVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getOrganizations() != null &&
                    !datasetBean.getOrganizations().isEmpty()){

                SubjectDAO subjectDAO = database.subjectDAO();

                OrganizationDAO organizationDAO = database.organizationDAO();

                for (OrganizationBean organizationBean: datasetBean.getOrganizations()) {

                    SubjectVO subjectVO = new SubjectVO();

                    subjectVO.setIdentifier(organizationBean.getIdentifier());

                    if (subjectDAO.exists(subjectVO.getIdentifier()))

                        subjectDAO.update(subjectVO);

                    else

                        subjectDAO.create(subjectVO);

                    OrganizationVO organizationVO = new OrganizationVO();

                    organizationVO.setIdentifier(organizationBean.getIdentifier());

                    organizationVO.setActive(organizationBean.getActive());

                    organizationVO.setDenomination(organizationBean.getDenomination());

                    organizationVO.setFancyName(organizationBean.getFancyName());

                    if (organizationDAO.exists(organizationVO.getIdentifier()))

                        organizationDAO.update(organizationVO);

                    else

                        organizationDAO.create(organizationVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getAddresses() != null &&
                    !datasetBean.getAddresses().isEmpty()){

                AddressDAO addressDAO = database.addressDAO();

                for (AddressBean addressBean: datasetBean.getAddresses()) {

                    AddressVO addressVO = new AddressVO();

                    addressVO.setIdentifier(addressBean.getIdentifier());

                    addressVO.setDenomination(addressBean.getDenomination());

                    addressVO.setNumber(addressBean.getNumber());

                    addressVO.setReference(addressBean.getReference());

                    addressVO.setDistrict(addressBean.getDistrict());

                    addressVO.setPostalCode(addressBean.getPostalCode());

                    addressVO.setCity(addressBean.getCity());

                    addressVO.setLatitude(addressBean.getLatitude());

                    addressVO.setLongitude(addressBean.getLongitude());

                    addressVO.setVerifiedAt(addressBean.getVerifiedAt());

                    addressVO.setVerifiedBy(addressBean.getVerifiedBy());

                    if (addressDAO.exists(addressVO.getIdentifier()))

                        addressDAO.update(addressVO);

                    else

                        addressDAO.create(addressVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getAddressesEdifications() != null &&
                    !datasetBean.getAddressesEdifications().isEmpty()){

                AddressEdificationDAO addressEdificationDAO = database.addressEdificationDAO();

                for (AddressEdificationBean addressEdificationBean: datasetBean.getAddressesEdifications()) {

                    AddressEdificationVO addressEdificationVO = new AddressEdificationVO();

                    addressEdificationVO.setAddress(addressEdificationBean.getAddress());

                    addressEdificationVO.setEdification(addressEdificationBean.getEdification());

                    addressEdificationVO.setType(addressEdificationBean.getType());

                    addressEdificationVO.setReference(addressEdificationBean.getReference());

                    addressEdificationVO.setFrom(addressEdificationBean.getFrom());

                    addressEdificationVO.setTo(addressEdificationBean.getTo());

                    if (addressEdificationDAO.exists(
                            addressEdificationVO.getAddress(),
                            addressEdificationVO.getEdification()))

                        addressEdificationDAO.update(addressEdificationVO);

                    else

                        addressEdificationDAO.create(addressEdificationVO);

                }

            }

            publishProgress(1);

            if (datasetBean.getAddressesEdificationsDwellers() != null &&
                    !datasetBean.getAddressesEdificationsDwellers().isEmpty()){

                AddressEdificationDwellerDAO addressEdificationDwellerDAO = database.addressEdificationDwellerDAO();

                for (AddressEdificationDwellerBean addressEdificationDwellerBean : datasetBean.getAddressesEdificationsDwellers()) {

                    AddressEdificationDwellerVO addressEdificationDwellerVO = new AddressEdificationDwellerVO();

                    addressEdificationDwellerVO.setAddress(addressEdificationDwellerBean.getAddress());

                    addressEdificationDwellerVO.setEdification(addressEdificationDwellerBean.getEdification());

                    addressEdificationDwellerVO.setDweller(addressEdificationDwellerBean.getDweller());

                    addressEdificationDwellerVO.setIndividual(addressEdificationDwellerBean.getIndividual());

                    addressEdificationDwellerVO.setFrom(addressEdificationDwellerBean.getFrom());

                    addressEdificationDwellerVO.setTo(addressEdificationDwellerBean.getTo());

                    if (addressEdificationDwellerDAO.exists(
                            addressEdificationDwellerVO.getAddress(),
                            addressEdificationDwellerVO.getEdification(),
                            addressEdificationDwellerVO.getDweller()))

                        addressEdificationDwellerDAO.update(addressEdificationDwellerVO);

                    else

                        addressEdificationDwellerDAO.create(addressEdificationDwellerVO);

                }

            }

        });

        return true;

    }


    protected void onPostExecute(Object callResult) {

        L listener = this.listener.get();

        if (listener != null) {

            if (callResult instanceof Boolean){

                listener.onImportSuccess();

            } else {

                if (callResult instanceof Messaging){

                    listener.onImportFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onImportPreExecute();

        void onImportProgressUpdate(Integer status);

        void onImportSuccess();

        void onImportFailure(Messaging messaging);

        void onImportCancelled();

    }


}