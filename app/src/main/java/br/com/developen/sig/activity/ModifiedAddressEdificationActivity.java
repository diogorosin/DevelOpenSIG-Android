package br.com.developen.sig.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationTypeFragment;
import br.com.developen.sig.task.CreateDwellerAsyncTask;
import br.com.developen.sig.util.Messaging;

public class ModifiedAddressEdificationActivity extends AppCompatActivity
        implements ModifiedAddressEdificationDwellerFragment.ModifiedAddressEdificationDwellerFragmentListener,
CreateDwellerAsyncTask.Listener{


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private SectionsPagerAdapter sectionsPagerAdapter;

    private FloatingActionButton floatingActionButton;

    private ViewPager viewPager;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address_edification);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_edification_toolbar);

        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.activity_modified_address_edification_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position==1 ? View.VISIBLE : View.GONE);

            }

            public void onPageScrollStateChanged(int state) {}

        });

        final TabLayout tabLayout = findViewById(R.id.activity_modified_address_edification_tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        floatingActionButton = findViewById(R.id.activity_modified_address_edification_fab);

        floatingActionButton.setOnClickListener(v -> {

            new CreateDwellerAsyncTask(ModifiedAddressEdificationActivity.this).execute(
                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_modified_address_edification, menu);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_modified_address_edification_action_settings)

            return true;

        return super.onOptionsItemSelected(item);

    }


    public void onPointerCaptureChanged(boolean hasCapture) {}


    public void onDwellerClicked(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel) {

        editDweller(modifiedAddressEdificationDwellerModel.
                getModifiedAddressEdification().
                getModifiedAddress().
                getIdentifier(), modifiedAddressEdificationDwellerModel.
                        getModifiedAddressEdification().
                        getEdification(), modifiedAddressEdificationDwellerModel.
                        getDweller());

    }


    public void onDwellerLongClick(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel) {}


    public void onCreateDwellerSuccess(Integer dweller) {

        editDweller(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER,0), dweller);

    }


    public void editDweller(Integer modifiedAddress, Integer edification, Integer dweller){

        Intent dwellerIntent = new Intent(ModifiedAddressEdificationActivity.this, ModifiedAddressEdificationDwellerActivity.class);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.MODIFIED_ADDRESS_IDENTIFIER, modifiedAddress);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.EDIFICATION_IDENTIFIER, edification);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.DWELLER_IDENTIFIER, dweller);

        startActivity(dwellerIntent);

    }


    public void onCreateDwellerFailure(Messaging messaging) {

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);

        }

        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position){

                case 0:

                    f = ModifiedAddressEdificationTypeFragment.newInstance(
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                            getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

                    break;

                case 1:

                    f = ModifiedAddressEdificationDwellerFragment.newInstance(
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                            getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

                    break;

            }

            return f;

        }

        public int getCount() {

            return 2;

        }

    }


}