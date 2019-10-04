package br.com.developen.sig.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.CredentialBean;
import br.com.developen.sig.bean.ExceptionBean;
import br.com.developen.sig.bean.TokenBean;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.Messaging;

public class LoginActivity extends AppCompatActivity {


    private ProgressDialog progressDialog;

    private EditText loginEditText;

    private EditText passwordEditText;

    private SharedPreferences preferences;

    private RequestQueue requestQueue;

    private Gson gson;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.activity_login_login_edittext);

        passwordEditText = findViewById(R.id.activity_login_password_edittext);

        passwordEditText.setOnEditorActionListener((textView, id, keyEvent) -> {

            boolean handled = false;

            if (id == EditorInfo.IME_ACTION_GO) {

                InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                assert imm != null;

                imm.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

                validateFieldsAndAttemptLogin();

                handled = true;

            }

            return handled;

        });

        Button signInButton = findViewById(R.id.activity_login_sign_in_button);

        signInButton.setOnClickListener(view -> validateFieldsAndAttemptLogin());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        gson = gsonBuilder.create();

    }


    private void validateFieldsAndAttemptLogin() {

        loginEditText.setError(null);

        passwordEditText.setError(null);

        String login = loginEditText.getText().toString();

        String password = passwordEditText.getText().toString();

        boolean cancel = false;

        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {

            passwordEditText.setError(getString(R.string.error_invalid_password));

            focusView = passwordEditText;

            cancel = true;

        }

        if (TextUtils.isEmpty(login)) {

            loginEditText.setError(getString(R.string.error_field_required));

            focusView = loginEditText;

            cancel = true;

        } else if (!isEmailValid(login)) {

            loginEditText.setError(getString(R.string.error_invalid_email));

            focusView = loginEditText;

            cancel = true;

        }

        if (cancel) {

            focusView.requestFocus();

        } else {

            if (!getProgressDialog().isShowing())

                getProgressDialog().show();

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

                        Intent intent = new Intent(LoginActivity.this, MapActivity.class);

                        startActivity(intent);

                        finish();

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

                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    String uft8String = new String(response.data, StandardCharsets.UTF_8);

                    return Response.success(uft8String, HttpHeaderParser.parseCacheHeaders(response));

                }

                public byte[] getBody() {

                    return gson.toJson(credentialBean).getBytes(StandardCharsets.UTF_8);

                }

            };

            request.setRetryPolicy(new DefaultRetryPolicy(
                    1000 * 10,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(request);

            requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> {

                if (getProgressDialog().isShowing())

                    getProgressDialog().hide();

            });

        }

    }


    private boolean isEmailValid(String email) {

        return email.contains("@");

    }


    private boolean isPasswordValid(String password) {

        return password.length() > 4;

    }


    public ProgressDialog getProgressDialog() {

        if (progressDialog==null){

            progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme);

            progressDialog.setIndeterminate(true);

            progressDialog.setMessage("Validando credenciais...");

        }

        return progressDialog;

    }


}