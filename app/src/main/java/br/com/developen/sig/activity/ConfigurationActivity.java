package br.com.developen.sig.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.CredentialBean;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.Messaging;




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

            dotsTextView[i].setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));

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

                    //onBindGovernmentList();

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

            CredentialBean credentialBean = new CredentialBean();

            credentialBean.setLogin(login);

            credentialBean.setPassword(password);

            StringRequest request = new StringRequest(Request.Method.POST, "http://200.98.202.29:8080/sig/rest/account/authenticate",
                    response -> {

                        Log.d("DIOGO", response);


                    }, error -> {

                Log.d("DIOGO", error.getMessage());

            }){

                public String getBodyContentType() {

                    return "application/json; charset=utf-8";

                }

                public byte[] getBody() {

                    return gson.toJson(credentialBean).getBytes(StandardCharsets.UTF_8);

                }

            };

            requestQueue.add(request);

            //new AuthenticateAsyncTask<>(this).execute(credentialBean);

        }

    }


    private boolean isValidEmail(String email) {

        return !email.contains("@") || !email.contains(".");

    }


    private boolean isValidPassword(String passoword) {

        return passoword.trim().length() < 5;

    }


    private void validateGovernmentStep() {


        //new GetInitialDatasetAsyncTask<>(this).execute();


    }


    // CONTROLLER ///////////////////////////////////////////////////


    // IMPLEMENTATIONS //////////////////////////////////////////////

    /*

    public void onAuthenticatePreExecute() {

        showProgress(true);

    }


    public void onAuthenticateSuccess(TokenBean tokenBean) {

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

        viewPager.setCurrentItem(GOVERNMENT_STEP, false);

    }


    public void onAuthenticateFailure(Messaging messaging) {

        showProgress(false);

        showAlertDialog(messaging, R.string.dlg_title_login_failure);

    }


    public void onBindGovernmentList(){

        check = 0;

        Spinner governmentSpinner = findViewById(R.id.activity_configuration_government_spinner);

        ArrayAdapter governmentAdapter = new ArrayAdapter<>(
                ConfigurationActivity.this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>());

        governmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        governmentSpinner.setAdapter(governmentAdapter);

        governmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (++check > 1) {

                    GovernmentBean governmentBean = (GovernmentBean) parentView.getItemAtPosition(position);

                    //new ChangeGovernmentAsyncTask<>(ConfigurationActivity.this).execute(governmentBean.getIdentifier());

                }

            }

            public void onNothingSelected(AdapterView<?> parentView) {}

        });

        new BindGovernmentListAsyncTask<>(
                this,
                governmentSpinner,
                governmentAdapter)
                .execute(0);

    }


    public void onBindGovernmentListPreExecute() {}


    public void onBindGovernmentListSuccess() {

        showProgress(false);

    }


    public void onBindGovernmentListFailure(Messaging messaging) {

        showProgress(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(accountFormView.getContext());

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setTitle(R.string.dlg_title_request_failure);

        builder.setPositiveButton(R.string.button_try_again,

                (dialog, id) -> onBindGovernmentList());

        AlertDialog alert = builder.create();

        alert.show();

    }


    public void onChangeGovernmentPreExecute() {

        showProgress(true);

    }


    public void onChangeGovernmentSuccess(TokenBean tokenBean) {

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

        showProgress(false);

    }


    public void onChangeGovernmentFailure(Messaging messaging) {

        showProgress(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(accountFormView.getContext());

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setTitle(R.string.dlg_title_request_failure);

        builder.setPositiveButton(R.string.button_ok,

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }

                });

        AlertDialog alert = builder.create();

        alert.show();

    }


    public void onGetInitialDatasetPreExecute() {

        showProgress(true);

    }


    public void onGetInitialDatasetSuccess() {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(Constants.DEVICE_CONFIGURED_PROPERTY, true);

        editor.apply();

        viewPager.setCurrentItem(FINISH_STEP, false);

        showProgress(false);

    }


    public void onGetInitialDatasetFailure(Messaging messaging) {

        showProgress(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(accountFormView.getContext());

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setTitle(R.string.dlg_title_request_failure);

        builder.setPositiveButton(R.string.button_try_again,

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }

                });

        AlertDialog alert = builder.create();

        alert.show();

    } */


    // FUNCTIONS ////////////////////////////////////////////////////


    private void showAlertDialog(Messaging messaging, int title){

        AlertDialog.Builder builder = new AlertDialog.Builder(accountFormView.getContext());

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setTitle(title);

        builder.setPositiveButton(android.R.string.ok,

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }

                });

        AlertDialog alert = builder.create();

        alert.show();

    }










}