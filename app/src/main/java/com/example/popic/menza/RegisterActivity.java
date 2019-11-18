package com.example.popic.menza;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.editText_emailRegister)
    EditText editText_emailRegister;
    @BindView(R.id.editText_passwordRegister)
    EditText editText_passwordRegister;
    @BindView(R.id.editText_usernameRegister)
    EditText editText_usernameRegister;
    @BindView(R.id.progressBarRegister)
    ProgressBar progressBar;
    @BindView(R.id.button_Register)
    Button button_register;

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();


    }

    @OnClick(R.id.button_Register)
    public void registerUser() {

        final String email = editText_emailRegister.getText().toString();
        String password = editText_passwordRegister.getText().toString();
        final String username = editText_usernameRegister.getText().toString();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            button_register.setVisibility(View.GONE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        button_register.setVisibility(View.VISIBLE);
                        Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                        user = auth.getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                        reference.keepSynced(true);
                        reference.child("email").setValue(email);
                        reference.child("username").setValue(username);
                        reference.child("userID").setValue(user.getUid());
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }


    @OnClick(R.id.button_backRegister)
    public void backToLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
