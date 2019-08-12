package br.com.developen.sig.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
import br.com.developen.sig.database.ModifiedAddressDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerDAO;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerVO;
import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.database.OrganizationDAO;
import br.com.developen.sig.database.OrganizationVO;
import br.com.developen.sig.database.StateDAO;
import br.com.developen.sig.database.StateVO;
import br.com.developen.sig.database.SubjectDAO;
import br.com.developen.sig.database.SubjectModel;
import br.com.developen.sig.database.SubjectVO;
import br.com.developen.sig.database.TypeDAO;
import br.com.developen.sig.database.TypeVO;


@Database(entities = {
        AddressVO.class,
        AddressEdificationVO.class,
        AddressEdificationDwellerVO.class,
        AgencyVO.class,
        TypeVO.class,
        CityVO.class,
        CountryVO.class,
        IndividualVO.class,
        OrganizationVO.class,
        StateVO.class,
        SubjectVO.class,
        ModifiedAddressVO.class,
        ModifiedAddressEdificationVO.class,
        ModifiedAddressEdificationDwellerVO.class},
        version = 001, views = {SubjectModel.class}, exportSchema = false)
public abstract class DB extends RoomDatabase {

    private static DB INSTANCE;

    public static DB getInstance(Context context) {

        if (INSTANCE == null) {

            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), DB.class, "sig-database")
                    .allowMainThreadQueries()
                    .build();

        }

        return INSTANCE;

    }

    public abstract SubjectDAO subjectDAO();

    public abstract IndividualDAO individualDAO();

    public abstract OrganizationDAO organizationDAO();

    public abstract AgencyDAO agencyDAO();

    public abstract CountryDAO countryDAO();

    public abstract StateDAO stateDAO();

    public abstract TypeDAO typeDAO();

    public abstract CityDAO cityDAO();

    public abstract AddressDAO addressDAO();

    public abstract AddressEdificationDAO addressEdificationDAO();

    public abstract AddressEdificationDwellerDAO addressEdificationDwellerDAO();

    public abstract ModifiedAddressDAO modifiedAddressDAO();

    public abstract ModifiedAddressEdificationDAO modifiedAddressEdificationDAO();

    public abstract ModifiedAddressEdificationDwellerDAO modifiedAddressEdificationDwellerDAO();

}