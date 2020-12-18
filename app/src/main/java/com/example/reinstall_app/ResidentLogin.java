package com.example.reinstall_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.local.UserIdStorageFactory;

public class ResidentLogin extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;


    EditText etResidentEmail,etResidentPassword, etEmailAccount;
    Button btnResidentLogin;
    TextView tvResidentReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident__login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);


        etResidentEmail=findViewById(R.id.etResidentEmail);
        etResidentPassword=findViewById(R.id.etResidentPassword);
        btnResidentLogin=findViewById(R.id.btnResidentLogin);
        tvResidentReset=findViewById(R.id.tvResidentReset);
        etEmailAccount=findViewById(R.id.etEmailAccount);

        tvLoad.setText("Busy authenticating user...please wait...");
        showProgress(true);

        Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
            @Override
            public void handleResponse(Boolean response) {

                if(response) {
                    tvLoad.setText("User authenticating...signing in...");

                    String userObject = UserIdStorageFactory.instance().getStorage().get();

                    Backendless.Data.of(BackendlessUser.class).findById(userObject, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            Intent intent = new Intent(ResidentLogin.this, com.example.reinstall_app.MainActivity.class);
                            startActivity(intent);
                            ResidentLogin.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(ResidentLogin.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    });
                }
                else
                {
                    showProgress(false);
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {

                Toast.makeText(ResidentLogin.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        tvResidentReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog=new AlertDialog.Builder(ResidentLogin.this);
                dialog.setMessage("Enter email related to acount for password reset."+"A reset link will be sent to the email address");

                View dialogView = getLayoutInflater().inflate(R.layout.dialog_view, null);
                dialog.setView(dialogView);

                etEmailAccount=dialogView.findViewById(R.id.etEmailAccount);

                dialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(etEmailAccount.getText().toString().isEmpty())
                        {
                            Toast.makeText(ResidentLogin.this, "Please enter an email adress!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            tvLoad.setText("Busy sending reset instructions to email address...please wait...");
                            showProgress(true);

                            Backendless.UserService.restorePassword(etEmailAccount.getText().toString().trim(), new AsyncCallback<Void>() {
                                @Override
                                public void handleResponse(Void response) {
                                    showProgress(false);
                                    Toast.makeText(ResidentLogin.this, "Reset instructions sent to email address!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {

                                    showProgress(false);
                                    Toast.makeText(ResidentLogin.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();

            }
        });

        btnResidentLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(etResidentEmail.getText().toString().isEmpty()||etResidentPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(ResidentLogin.this, "Enter all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String email=etResidentEmail.getText().toString().trim();
                    String password=etResidentPassword.getText().toString().trim();

                    tvLoad.setText("Logging in...");
                    showProgress(true);

                    Backendless.UserService.login(email, password, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {

                            Intent intent=new Intent(ResidentLogin.this, com.example.reinstall_app.MainActivity.class);

                           //ReinstallApplicationClass.user=response;
                            Toast.makeText(ResidentLogin.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            ResidentLogin.this.finish();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                            Toast.makeText(ResidentLogin.this, "Error: "+fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    }, true);
                }

            }
        });
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

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}