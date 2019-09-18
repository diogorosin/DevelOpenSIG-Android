package br.com.developen.sig.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;


public class SplashActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //DEFINE O LAYOUT
        setContentView(R.layout.activity_splash);

        //INICIALIZA VARIAVEIS DO SISTEMA
        App.getInstance().initialize();

        //INICIALIZA BARRA DE PROGRESSO
        ProgressBar spinner = findViewById(R.id.activity_splash_progressbar);

        spinner.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.DST);

        //VERIFICA CONFIGURACOES
        Handler handle = new Handler();

        handle.postDelayed(() -> {

            Intent intent;

            SharedPreferences preferences = getSharedPreferences(
                    Constants.SHARED_PREFERENCES_NAME, 0);

            if (preferences.getBoolean(Constants.DEVICE_CONFIGURED_PROPERTY, false)) {

                if (preferences.getInt(Constants.USER_IDENTIFIER_PROPERTY, 0) == 0) {

                    intent = new Intent(SplashActivity.this, LoginActivity.class);

                } else {

                    intent = new Intent(SplashActivity.this, MapActivity.class);

                }

            } else {

                intent = new Intent(SplashActivity.this, ConfigurationActivity.class);

            }

            if (getIntent().getData() != null){

                List<String> params = getIntent().getData().getPathSegments();

                try {

                    intent.putExtra(Constants.DEFAULT_ADDRESS, Integer.valueOf(params.get(1)));

                } catch (NumberFormatException ignored) {}

            }

            startActivity(intent);

            finish();

        }, 500);

    }

}