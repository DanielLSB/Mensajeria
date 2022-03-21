package com.example.bdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bdchat.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    getSupportActionBar().hide();

    mAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();

    progressDialog = new ProgressDialog(SignInActivity.this);
    progressDialog.setTitle("Iniciando");
    progressDialog.setMessage("Progreso de validadcion.");



        binding.btSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty())
            {
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                              progressDialog.dismiss();
                              if(task.isSuccessful()){
                                  Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                  startActivity(intent);

                              }else{
                                  Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                  
                                  
                              }
                            }
                        });

            }else
            {
                Toast.makeText(SignInActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();

            }

        }
    });

    if(mAuth.getCurrentUser()!=null)
    {
        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
        startActivity(intent);

    }
   binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
           startActivity(intent);

       }
   });
    }
}