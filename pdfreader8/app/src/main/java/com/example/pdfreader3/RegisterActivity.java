package com.example.pdfreader3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText fullName = findViewById(R.id.inputFullName);
        final EditText email = findViewById(R.id.inputEmail);
        final EditText Password = findViewById(R.id.inputPassword);
        final EditText conPassword = findViewById(R.id.conPassword);
        final Button registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get data from edittext to string variable
                final String fullNameTxt = fullName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = Password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();

                //check if user fill all the fields before sending to firebase
                if(fullNameTxt.isEmpty()||emailTxt.isEmpty()||passwordTxt.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please fill all the data", Toast.LENGTH_SHORT).show();
                }
               // check password are matching or not
                else if(!passwordTxt.equals(conPasswordTxt)){
                    Toast.makeText(RegisterActivity.this,"Password are not matching",Toast.LENGTH_SHORT).show();

                }
                else{

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                if(user != null){
                                    String uid = user.getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("name",fullNameTxt);
                                    map.put("email",emailTxt);
                                    ref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterActivity.this,"User Registered Successfully",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            }
        });
    }
}