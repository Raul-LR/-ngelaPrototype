package com.angela_prototype.rlr.angelaprototype.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.angela_prototype.rlr.angelaprototype.Model.SendUser;
import com.angela_prototype.rlr.angelaprototype.Pojos.User;
import com.angela_prototype.rlr.angelaprototype.Pojos.UserCredentials;
import com.angela_prototype.rlr.angelaprototype.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Raúl on 20/06/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private SendUser sendUser;

    @BindView(R.id.progressBar)
    protected View load_bar;

    @BindView(R.id.register_form)
    protected View register_form;

    @BindView(R.id.username)
    protected EditText username;

    @BindView(R.id.email)
    protected EditText email;

    @BindView(R.id.password)
    protected EditText password;

    @BindView(R.id.password_repeat)
    protected EditText password_repeat;

    @BindView(R.id.dni)
    protected EditText dni;

    @BindView(R.id.name)
    protected EditText name;

    @BindView(R.id.second_name)
    protected EditText second_name;

    @BindView(R.id.last_name)
    protected EditText last_name;

    @BindView(R.id.tarjetaSanitaria)
    protected EditText tarjetaSanitaria;

    @BindView(R.id.localidad)
    protected AutoCompleteTextView localidad;

    @BindView(R.id.municipio)
    protected AutoCompleteTextView municipio;

    @BindView(R.id.direccion)
    protected EditText direccion;

    @BindView(R.id.portal)
    protected EditText portal;

    @BindView(R.id.puerta)
    protected EditText puerta;

    @BindView(R.id.cp)
    protected EditText cp;

    @BindView(R.id.to_logInButton)
    protected TextView to_logInButton;

    @BindView(R.id.register_button)
    protected Button register_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this, this);
    }

    @OnClick(R.id.to_logInButton)
    public void OnClickToBack(){
        to_logInButton.setTextColor(to_logInButton.getContext().getResources().getColor(R.color.linkClickColor));
        Intent back = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(back);
    }

    @OnClick(R.id.register_button)
    public void OnClick(){
        attempRegister();
    }

    public void attempRegister(){
        email.setError(null);
        password.setError(null);
        password_repeat.setError(null);
        username.setError(null);
        dni.setError(null);
        tarjetaSanitaria.setError(null);
        name.setError(null);
        second_name.setError(null);
        last_name.setError(null);

        final String emailToSend = email.getText().toString();
        final String passwordToSend = password.getText().toString();
        final String passwordConfirmation = password_repeat.getText().toString();
        final String usernameToSend = username.getText().toString();
        final String dniToSend = dni.getText().toString();
        final String nameToSend = name.getText().toString();
        final String secondNameToSend = second_name.getText().toString();
        final String lastNameToSend = last_name.getText().toString();
        final String tarjetaSanitariaToSend = tarjetaSanitaria.getText().toString();
        final String localidadToSend = localidad.getText().toString();
        final String municipioToSend = municipio.getText().toString();
        final String direccionToSend = direccion.getText().toString();
        final String portalToSend = portal.getText().toString();
        final String puertaToSend = puerta.getText().toString();
        final String cpToSend = cp.getText().toString();
        int parsedPortalToSend = 0;
        int parsedCpToSend = 0;

        if(!portalToSend.equals("")){
            parsedPortalToSend = Integer.parseInt(portalToSend);
        }
        if(!cpToSend.equals("")){
            parsedCpToSend = Integer.parseInt(cpToSend);
        }

        UserCredentials userCredentialsToSend = new UserCredentials(0, 0, emailToSend, usernameToSend, passwordToSend);
        User userToSend = new User(userCredentialsToSend, 0, dniToSend, nameToSend, secondNameToSend, lastNameToSend, tarjetaSanitariaToSend, localidadToSend, municipioToSend, direccionToSend, parsedPortalToSend, puertaToSend, parsedCpToSend);

        boolean cancel = false;
        View focusView = null;

        //Check for a valid username
        if(TextUtils.isEmpty(usernameToSend)){
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordToSend)) {
            password.setError(getString(R.string.error_field_required));
            focusView = password;
            cancel = true;
        }else if (!isPasswordValid(passwordToSend)){
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        if(TextUtils.isEmpty(passwordConfirmation)){
            password_repeat.setError(getString(R.string.error_field_required));
            focusView = password_repeat;
            cancel = true;
        }else if(!isPasswordMatching(passwordToSend, passwordConfirmation)){
            password_repeat.setError("Las contraseñas no coinciden");
            focusView = password_repeat;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailToSend)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailToSend)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        //Check for a valid DNI.
        if (TextUtils.isEmpty(dniToSend)){
            dni.setError(getString(R.string.error_field_required));
            focusView = dni;
            cancel = true;
        }else if (!isDniValid(dniToSend)){
            dni.setError("El Dni introducido no es válido");
            focusView = dni;
            cancel = true;
        }

        //Check for a valid tarjetaSaniaria.
        if(!TextUtils.isEmpty(tarjetaSanitariaToSend) && !isTarjetaSanitariaValid(tarjetaSanitariaToSend)){
            tarjetaSanitaria.setError("La Tarjeta sanitaria introducida no es válida");
            focusView = tarjetaSanitaria;
            cancel = true;
        }

        //Check if there are a name and the last names
        if(TextUtils.isEmpty(nameToSend)){
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }

        if(TextUtils.isEmpty(secondNameToSend)){
            second_name.setError(getString(R.string.error_field_required));
            focusView = second_name;
            cancel = true;
        }

        if(TextUtils.isEmpty(lastNameToSend)){
            last_name.setError(getString(R.string.error_field_required));
            focusView = last_name;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else {
            showProgress(true);
            this.sendUser = new SendUser(userToSend);
            this.sendUser.execute((Void) null);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run(){
                    boolean success = sendUser.doInBackground();
                    if(success) {
                        Intent registered = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(registered);
                    }else{
                        showProgress(false);
                        Context context = getApplicationContext();
                        String text = "Ha ocurrido un error al registrar. Por favor intentelo de nuevo más tarde";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
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

    private boolean isPasswordMatching(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    private boolean isDniValid(String dni){
        return dni.length() == 9;
    }

    private boolean isTarjetaSanitariaValid(String tarjetaSanitaria){
        return tarjetaSanitaria.length() == 10;
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

            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
            register_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    register_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            load_bar.setVisibility(show ? View.VISIBLE : View.GONE);
            load_bar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    load_bar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            load_bar.setVisibility(show ? View.VISIBLE : View.GONE);
            register_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
}
