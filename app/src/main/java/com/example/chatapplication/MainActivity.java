package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private EditText editUsername , editPassword, editEmail;
    private Button signUpButton;
    private TextView txtLogInInfo;
    private boolean signUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editUsername = findViewById(R.id.editUsername);
        signUpButton = findViewById(R.id.signUpButton);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this , FriendsActivity.class));
            finish();
        }

        signUpButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(editEmail.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                    if(signUp && editUsername.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(signUp){
                    handleSignUp();
                }
                else{
                    handleSignIn();
                }
            }
        });

        txtLogInInfo = findViewById(R.id.txtLogInInfo);
        txtLogInInfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(signUp){
                    signUp = false;
                    signUpButton.setText("Sign-In");
                    txtLogInInfo.setText("Not an User? Sign-Up");
                    editUsername.setVisibility(View.GONE);
                }
                else{
                    signUp = true;
                    editUsername.setVisibility(View.VISIBLE);
                    signUpButton.setText("Sign-Up");
                    txtLogInInfo.setText("Already an User? Sign-In");
                }
            }
        });
    }

    private void handleSignUp(){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(editEmail.getText().toString() , editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("user/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(editUsername.getText().toString() , editEmail.getText().toString(),""));
                    startActivity(new Intent(MainActivity.this , FriendsActivity.class));
                    Toast.makeText(MainActivity.this,"Signed Up Successfully" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,task.getException().getLocalizedMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void handleSignIn(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(editEmail.getText().toString() , editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this , FriendsActivity.class));
                    Toast.makeText(MainActivity.this,"Signed In Successfully" , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid Email or Password" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}