package br.com.developen.sig.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import br.com.developen.sig.R;
import br.com.developen.sig.widget.ConfigurationPagerAdapter;


public class ConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuration);

        ConfigurationPagerAdapter sectionsPagerAdapter = new ConfigurationPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.activity_configuration_view_pager);

        viewPager.setAdapter(sectionsPagerAdapter);

    }

}