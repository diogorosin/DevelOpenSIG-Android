package br.com.developen.sig.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.CredentialBean;
import br.com.developen.sig.bean.DatasetBean;
import br.com.developen.sig.bean.ExceptionBean;
import br.com.developen.sig.bean.GovernmentBean;
import br.com.developen.sig.bean.IntegerBean;
import br.com.developen.sig.bean.TokenBean;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.util.Sync;


/*

VOLLEY + GSON

https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley

 */

public class ConfigurationActivity extends AppCompatActivity {


    private static final int WELCOME_STEP = 0;

    private static final int LOGIN_STEP = 1;

    private static final int GOVERNMENT_STEP = 2;

    private static final int FINISH_STEP = 3;


    private int check = 0;

    private ViewPager viewPager;

    private LinearLayout dotsLayout;

    private SharedPreferences preferences;

    private Button previewButton, nextButton;

    private View progressView;

    private View accountFormView;

    private int[] layouts;


    private RequestQueue requestQueue;

    private Gson gson;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        setContentView(R.layout.activity_configuration);

        viewPager = findViewById(R.id.activity_configuration_view_pager);

        dotsLayout = findViewById(R.id.activity_configuration_layout_dots);

        previewButton = findViewById(R.id.activity_configuration_preview_button);

        nextButton = findViewById(R.id.activity_configuration_next_button);

        progressView = findViewById(R.id.activity_configuration_progress);

        accountFormView = findViewById(R.id.activity_configuration_body);

        layouts = new int[]{
                R.layout.activity_configuration_welcome_step,
                R.layout.activity_configuration_login_step,
                R.layout.activity_configuration_government_step,
                R.layout.activity_configuration_finish_step};

        addBottomDots(WELCOME_STEP);

        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter();

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        previewButton.setOnClickListener(v -> {

            int currentPage = getItem(0);

            switch (currentPage){

                case WELCOME_STEP:

                    break;

                case LOGIN_STEP:

                    viewPager.setCurrentItem(WELCOME_STEP, false);

                    break;

                case GOVERNMENT_STEP:

                    viewPager.setCurrentItem(LOGIN_STEP, false);

                    break;

            }

        });

        nextButton.setOnClickListener(v -> {

            int currentPage = getItem(0);

            switch (currentPage) {

                case WELCOME_STEP:

                    viewPager.setCurrentItem(LOGIN_STEP, false);

                    break;

                case LOGIN_STEP:

                    validateLoginStep();

                    break;

                case GOVERNMENT_STEP:

                    validateGovernmentStep();

                    break;

                case FINISH_STEP:

//                        Intent intent = new Intent(AccountActivity.this, MapActivity.class);

//                        startActivity(intent);

//                        finish();

                    break;

            }

        });

        preferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        gson = gsonBuilder.create();

    }


    // VIEW ////////////////////////////////////////////////////


    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        accountFormView.setVisibility(show ? View.GONE : View.VISIBLE);

        accountFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {

            public void onAnimationEnd(Animator animation) {

                accountFormView.setVisibility(show ? View.GONE : View.VISIBLE);

            }

        });

        progressView.setVisibility(show ? View.VISIBLE : View.GONE);

        progressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {

            public void onAnimationEnd(Animator animation) {

                progressView.setVisibility(show ? View.VISIBLE : View.GONE);

            }

        });

    }


    private void addBottomDots(int currentPage) {

        TextView[] dotsTextView = new TextView[layouts.length];

        dotsLayout.removeAllViews();

        for (int i = 0; i < dotsTextView.length; i++) {

            dotsTextView[i] = new TextView(this);

            dotsTextView[i].setText(Html.fromHtml("&#8226;"));

            dotsTextView[i].setTextSize(35);

            dotsTextView[i].setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorWhite));

            dotsLayout.addView(dotsTextView[i]);

        }

        int selectedDot;

        switch (currentPage){

            case LOGIN_STEP:

                selectedDot = 1;
                break;

            case GOVERNMENT_STEP:

                selectedDot = 2;
                break;

            case FINISH_STEP:

                selectedDot = 3;
                break;

            default:

                selectedDot = currentPage;
                break;

        }

        dotsTextView[selectedDot].setTextColor(
                ContextCompat.getColor(getBaseContext(),
                        R.color.colorAccent));

    }


    private int getItem(int i) {

        return viewPager.getCurrentItem() + i;

    }


    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int position) {

            addBottomDots(position);

            switch (position){

                case WELCOME_STEP:

                    break;

                case LOGIN_STEP:

                    final EditText loginPasswordEditText = findViewById(R.id.activity_configuration_user_login_edittext);

                    loginPasswordEditText.setOnEditorActionListener((textView, i, keyEvent) -> {

                        if (i == EditorInfo.IME_ACTION_GO) {

                            InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                            Objects.requireNonNull(imm).hideSoftInputFromWindow(textView.getWindowToken(), 0);

                            nextButton.callOnClick();

                            return true;

                        }

                        return false;

                    });

                    break;

                case GOVERNMENT_STEP:

                    onBindGovernmentList();

                    break;

                case FINISH_STEP:

                    break;

            }

            if (position == WELCOME_STEP || position == FINISH_STEP)

                previewButton.setVisibility(View.INVISIBLE);

            else

                previewButton.setVisibility(View.VISIBLE);

            if (position == layouts.length - 1) {

                nextButton.setText(getString(R.string.finish));

            } else {

                if (position == WELCOME_STEP)

                    nextButton.setText(getString(R.string.start));

                else

                    nextButton.setText(getString(R.string.next));

            }

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        public void onPageScrollStateChanged(int arg0) {}

    };


    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {}

        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert layoutInflater != null;

            View view = layoutInflater.inflate(layouts[position], container, false);

            container.addView(view);

            return view;

        }

        public int getCount() {

            return layouts.length;

        }

        public boolean isViewFromObject(View view, Object obj) {

            return view == obj;

        }

        public void destroyItem(ViewGroup container, int position, Object object) {

            View view = (View) object;

            container.removeView(view);

        }

    }


    // VALIDATORS ///////////////////////////////////////////////////


    private void validateLoginStep() {

        EditText loginEditText = findViewById(R.id.activity_configuration_user_login_edittext);

        EditText passwordEditText = findViewById(R.id.activity_configuration_user_password_edittext);

        loginEditText.setError(null);

        passwordEditText.setError(null);

        String login = loginEditText.getText().toString();

        String password = passwordEditText.getText().toString();

        boolean cancel = false;

        View focusView = null;

        if (TextUtils.isEmpty(password)) {

            passwordEditText.setError(getString(R.string.error_field_required));

            focusView = passwordEditText;

            cancel = true;

        } else {

            if(isValidPassword(password)){

                passwordEditText.setError(getString(R.string.error_invalid_password));

                focusView = passwordEditText;

                cancel = true;

            }

        }

        if (TextUtils.isEmpty(login)) {

            loginEditText.setError(getString(R.string.error_field_required));

            focusView = loginEditText;

            cancel = true;

        } else {

            if(isValidEmail(login)){

                loginEditText.setError(getString(R.string.error_invalid_email));

                focusView = loginEditText;

                cancel = true;

            }

        }

        if (cancel) {

            focusView.requestFocus();

        } else {

            showProgress(true);

            CredentialBean credentialBean = new CredentialBean();

            credentialBean.setLogin(login);

            credentialBean.setPassword(password);

            StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_BASE_URL + "account/authenticate",

                    response -> {

                        TokenBean tokenBean = gson.fromJson(response, TokenBean.class);

                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString(Constants.TOKEN_IDENTIFIER_PROPERTY, tokenBean.getIdentifier());

                        editor.putInt(Constants.TOKEN_LEVEL_PROPERTY, tokenBean.getLevel());

                        editor.putInt(Constants.GOVERNMENT_IDENTIFIER_PROPERTY, tokenBean.getGovernment().getIdentifier());

                        editor.putString(Constants.GOVERNMENT_DENOMINATION_PROPERTY, tokenBean.getGovernment().getDenomination());

                        editor.putString(Constants.GOVERNMENT_FANCYNAME_PROPERTY, tokenBean.getGovernment().getFancyName());

                        editor.putInt(Constants.USER_IDENTIFIER_PROPERTY, tokenBean.getUser().getIdentifier());

                        editor.putString(Constants.USER_NAME_PROPERTY, tokenBean.getUser().getName());

                        editor.putString(Constants.USER_LOGIN_PROPERTY, tokenBean.getUser().getLogin());

                        editor.apply();

                        viewPager.setCurrentItem(GOVERNMENT_STEP);

                    },

                    error -> {

                        if (error instanceof NoConnectionError){

                            Toast.makeText(getApplicationContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

                        } else {

                            try {

                                String responseBody = new String(error.networkResponse.data, "utf-8");

                                Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                                if (messaging != null && messaging.getMessages().length > 0)

                                    Toast.makeText(getApplicationContext(), messaging.getMessages()[0], Toast.LENGTH_LONG).show();

                            } catch (UnsupportedEncodingException e) {}

                        }

                    })

            {

                public String getBodyContentType() {

                    return Constants.JSON_CONTENT_TYPE;

                }

                public byte[] getBody() {

                    return gson.toJson(credentialBean).getBytes(StandardCharsets.UTF_8);

                }

            };

            requestQueue.add(request);

            requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> showProgress(false));

        }

    }


    private boolean isValidEmail(String email) {

        return !email.contains("@") || !email.contains(".");

    }


    private boolean isValidPassword(String passoword) {

        return passoword.trim().length() < 5;

    }


    private void validateGovernmentStep() {

        showProgress(true);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_BASE_URL + "dataset/initial",

                response -> {

                    DatasetBean datasetBean = gson.fromJson(response, DatasetBean.class);

                    new Sync(DB.getInstance(App.getContext())).dataset(datasetBean);

                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putBoolean(Constants.DEVICE_CONFIGURED_PROPERTY, true);

                    editor.apply();

                    viewPager.setCurrentItem(FINISH_STEP);

                },

                error -> {

                    if (error instanceof NoConnectionError){

                        Toast.makeText(getApplicationContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

                    } else {

                        try {

                            String responseBody = new String(error.networkResponse.data, "utf-8");

                            Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                            if (messaging != null && messaging.getMessages().length > 0)

                                Toast.makeText(getApplicationContext(), messaging.getMessages()[0], Toast.LENGTH_LONG).show();

                        } catch (UnsupportedEncodingException e) {}

                    }

                })

        {

            public String getBodyContentType() {

                return Constants.JSON_CONTENT_TYPE;

            }

            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<>();

                headers.put(Constants.AUTHORIZATION_HEADER, "Bearer " + preferences.getString(Constants.TOKEN_IDENTIFIER_PROPERTY, null));

                return headers;

            }

        };

        requestQueue.add(request);

        requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> showProgress(false));

    }


    // CONTROLLER ///////////////////////////////////////////////////


    // IMPLEMENTATIONS //////////////////////////////////////////////


    public void onBindGovernmentList(){

        check = 0;

        Spinner governmentSpinner = findViewById(R.id.activity_configuration_government_spinner);

        ArrayAdapter governmentAdapter = new ArrayAdapter<>(
                ConfigurationActivity.this,
                R.layout.activity_configuration_company_spinner,
                new ArrayList<>());

        governmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        governmentSpinner.setAdapter(governmentAdapter);

        governmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (++check > 1) {

                    showProgress(true);

                    GovernmentBean governmentBean = (GovernmentBean) parentView.getItemAtPosition(position);

                    StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_BASE_URL + "account/government",

                            response -> {

                                TokenBean tokenBean = gson.fromJson(response, TokenBean.class);

                                SharedPreferences.Editor editor = preferences.edit();

                                editor.putString(Constants.TOKEN_IDENTIFIER_PROPERTY, tokenBean.getIdentifier());

                                editor.putInt(Constants.TOKEN_LEVEL_PROPERTY, tokenBean.getLevel());

                                editor.putInt(Constants.GOVERNMENT_IDENTIFIER_PROPERTY, tokenBean.getGovernment().getIdentifier());

                                editor.putString(Constants.GOVERNMENT_DENOMINATION_PROPERTY, tokenBean.getGovernment().getDenomination());

                                editor.putString(Constants.GOVERNMENT_FANCYNAME_PROPERTY, tokenBean.getGovernment().getFancyName());

                                editor.putInt(Constants.USER_IDENTIFIER_PROPERTY, tokenBean.getUser().getIdentifier());

                                editor.putString(Constants.USER_NAME_PROPERTY, tokenBean.getUser().getName());

                                editor.putString(Constants.USER_LOGIN_PROPERTY, tokenBean.getUser().getLogin());

                                editor.apply();

                            },

                            error -> {

                                if (error instanceof NoConnectionError){

                                    Toast.makeText(getApplicationContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

                                } else {

                                    try {

                                        String responseBody = new String(error.networkResponse.data, "utf-8");

                                        Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                                        if (messaging != null && messaging.getMessages().length > 0)

                                            Toast.makeText(getApplicationContext(), messaging.getMessages()[0], Toast.LENGTH_LONG).show();

                                    } catch (UnsupportedEncodingException e) {}

                                }

                            })

                    {

                        public String getBodyContentType() {

                            return Constants.JSON_CONTENT_TYPE;

                        }

                        public byte[] getBody() {

                            return gson.toJson(new IntegerBean(governmentBean.getIdentifier())).getBytes(StandardCharsets.UTF_8);

                        }

                        public Map<String, String> getHeaders() {

                            Map<String, String> headers = new HashMap<>();

                            headers.put(Constants.AUTHORIZATION_HEADER, "Bearer " + preferences.getString(Constants.TOKEN_IDENTIFIER_PROPERTY, null));

                            return headers;

                        }

                    };

                    requestQueue.add(request);

                    requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> showProgress(false));

                }

            }

            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        showProgress(true);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_BASE_URL + "account/government",

                response -> {

                    try {

                        JSONArray jsonObject = new JSONArray(response);

                        if (jsonObject.length() > 0){

                            List<GovernmentBean> governments = Arrays.asList(gson.fromJson(jsonObject.toString(), GovernmentBean[].class));

                            governmentAdapter.clear();

                            for (GovernmentBean governmentBean : governments)

                                governmentAdapter.add(governmentBean);

                            int selected = preferences.getInt(Constants.GOVERNMENT_IDENTIFIER_PROPERTY,-1);

                            if (selected != -1){

                                GovernmentBean governmentBean = new GovernmentBean();

                                governmentBean.setIdentifier(selected);

                                int position = governmentAdapter.getPosition(governmentBean);

                                governmentSpinner.setSelection(position, false);

                            }

                        }

                    } catch (JSONException e) {}

                },

                error -> {

                    if (error instanceof NoConnectionError){

                        Toast.makeText(getApplicationContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

                    } else {

                        try {

                            String responseBody = new String(error.networkResponse.data, "utf-8");

                            Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                            if (messaging != null && messaging.getMessages().length > 0)

                                Toast.makeText(getApplicationContext(), messaging.getMessages()[0], Toast.LENGTH_LONG).show();

                        } catch (UnsupportedEncodingException e) {}

                    }

                })

        {

            public String getBodyContentType() {

                return Constants.JSON_CONTENT_TYPE;

            }

            public Map<String, String> getHeaders() {

                Map<String, String> headers = new HashMap<>();

                headers.put(Constants.AUTHORIZATION_HEADER, "Bearer " + preferences.getString(Constants.TOKEN_IDENTIFIER_PROPERTY, null));

                return headers;

            }

        };

        requestQueue.add(request);

        requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> showProgress(false));

    }


}