package com.example.pdfreader3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pdf-reader-2-3c59c-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText Email = findViewById(R.id.inputEmail);
        final EditText Password = findViewById(R.id.inputPassword);
        final Button Loginbtn = findViewById(R.id.Loginbtn);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailTxt = Email.getText().toString();
                final String passwordTxt = Password.getText().toString();


                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser user = auth.getCurrentUser();
                            if(user != null){
                                String uid = user.getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
                                ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String name = snapshot.child("name").getValue().toString();
                                            String email = snapshot.child("email").getValue().toString();

                                            SharedPreferences sp = getSharedPreferences("sp",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("my_name",name);
                                            editor.putString("my_email",email);
                                            editor.apply();
                                            Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this,MenuActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}