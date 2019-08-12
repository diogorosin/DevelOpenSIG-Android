package br.com.developen.sig.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.fragment.ModifiedAddressAddressFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationFragment;
import br.com.developen.sig.fragment.ModifiedAddressLocationFragment;
import br.com.developen.sig.task.CreateDwellerAsyncTask;
import br.com.developen.sig.task.CreateEdificationAsyncTask;
import br.com.developen.sig.task.UpdateAddressLocationAsynTask;
import br.com.developen.sig.util.Messaging;

public class ModifiedAddressActivity extends AppCompatActivity
        implements ModifiedAddressLocationFragment.LocationListener,
        ModifiedAddressEdificationFragment.EdificationFragmentListener,
        CreateEdificationAsyncTask.Listener,
        UpdateAddressLocationAsynTask.Listener {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";


    private SectionsPagerAdapter sectionsPagerAdapter;

    private FloatingActionButton floatingActionButton;

    private ViewPager viewPager;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_toolbar);

        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.activity_modified_address_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position==2?View.VISIBLE:View.GONE);

            }

            public void onPageScrollStateChanged(int state) {}

        });

        final TabLayout tabLayout = findViewById(R.id.activity_modified_address_tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        floatingActionButton = findViewById(R.id.activity_modified_address_fab);

        floatingActionButton.setOnClickListener(v ->

                new CreateEdificationAsyncTask(ModifiedAddressActivity.this).
                        execute(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0))

        );

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_modified_address, menu);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_modified_address_action_settings)

            return true;

        return super.onOptionsItemSelected(item);

    }


    public void onPointerCaptureChanged(boolean hasCapture) {}


    public void onLocationChanged(LatLng latLng) {

        new UpdateAddressLocationAsynTask<>(this).execute(
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0),
                latLng.latitude,
                latLng.longitude);

    }


    public void onUpdateAddressLocationSuccess() {}


    public void onUpdateAddressLocationFailure(Messaging messaging) {}


    public void onEdificationClicked(ModifiedAddressEdificationModel modifiedAddressEdificationModel) {

        editEdification(modifiedAddressEdificationModel.
                getModifiedAddress().
                getIdentifier(),
                modifiedAddressEdificationModel.
                        getEdification());

    }


    public void onEdificationLongClick(ModifiedAddressEdificationModel modifiedAddressEdificationModel) {}


    public void onCreateEdificationSuccess(Integer identifier) {

        editEdification(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0), identifier);

    }


    public void onCreateEdificationFailure(Messaging messaging) {}


    public void editEdification(Integer modifiedAddress, Integer edification){

        Intent edificationIntent = new Intent(ModifiedAddressActivity.this, ModifiedAddressEdificationActivity.class);

        edificationIntent.putExtra(ModifiedAddressEdificationDwellerActivity.MODIFIED_ADDRESS_IDENTIFIER, modifiedAddress);

        edificationIntent.putExtra(ModifiedAddressEdificationDwellerActivity.EDIFICATION_IDENTIFIER, edification);

        startActivity(edificationIntent);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);

        }

        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position){

                case 0:

                    f = ModifiedAddressLocationFragment.newInstance(ModifiedAddressActivity.this,
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

                case 1:

                    f = ModifiedAddressAddressFragment.newInstance(getIntent().
                            getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

                case 2:

                    f = ModifiedAddressEdificationFragment.newInstance(getIntent().
                            getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

            }

            return f;

        }

        public int getCount() {

            return 3;

        }

    }


}