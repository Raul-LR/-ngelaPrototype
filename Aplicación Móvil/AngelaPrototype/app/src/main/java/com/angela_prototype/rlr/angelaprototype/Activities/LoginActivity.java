package com.angela_prototype.rlr.angelaprototype.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.angela_prototype.rlr.angelaprototype.Bluetooth.Bluetooth;
import com.angela_prototype.rlr.angelaprototype.Model.UserExtraction;
import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
    private UserExtraction userExtract = null;
    private User user = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mTextView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        final TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButton.setTextColor(registerButton.getContext().getResources().getColor(R.color.linkClickColor));
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mTextView = findViewById(R.id.textView);
        mProgressView = findViewById(R.id.login_progress);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            this.userExtract = new UserExtraction(email);
            this.userExtract.execute((Void) null);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run(){
                    System.out.println(userExtract.doInBackground().toString());
                    user = userExtract.doInBackground();
                    if(user.getCredentials().getPassword().equals(password)) {
                        Intent loged = new Intent(LoginActivity.this, MainActivity.class);
                        loged.putExtra("user_id", user.getId());
                        loged.putExtra("userCredentials_id", user.getCredentials().getId());
                        loged.putExtra("username", user.getCredentials().getUsername());
                        loged.putExtra("password", user.getCredentials().getPassword());
                        loged.putExtra("email", user.getCredentials().getEmail());
                        loged.putExtra("DNI", user.getDNI());
                        loged.putExtra("nombre", user.getNombre());
                        loged.putExtra("apellido1", user.getApellido1());
                        loged.putExtra("apellido2", user.getApellido2());
                        loged.putExtra("tarjetaSanitaria", user.getTarjetaSanitaria());
                        loged.putExtra("localidad", user.getLocalidad());
                        loged.putExtra("municipio", user.getMunicipio());
                        loged.putExtra("direccion", user.getDireccion());
                        loged.putExtra("portal", user.getPortal());
                        loged.putExtra("puerta", user.getPuerta());
                        loged.putExtra("cp", user.getCp());
                        startActivity(loged);
                    }else{
                        showProgress(false);
                        if(user.getCredentials().getEmail().equals(email)){
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                        }else{
                            mEmailView.setError("No se encuentran el correo y/o la contraseña introducidos");
                            mPasswordView.setError("No se encuentran el correo y/o la contraseña introducidos");
                        }
                    }
                }
            }, 2000);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && (email.contains(".com") || email.contains(".es") || email.contains(".net") || email.contains(".org"));
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mTextView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mTextView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
}

