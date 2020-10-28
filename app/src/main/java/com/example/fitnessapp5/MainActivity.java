package com.example.fitnessapp5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText email, password, height, weight, weightGoals, calIntakeGoals ;
    Button login, register, signout, push, pull;
    CardView loginCardView;
    Switch convert ;
    private ImageView mealspic;
    private Uri imageUri;

    private FirebaseStorage storage ;
    private StorageReference storageReference;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUsers = database.getReference("users");

    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mealspic = findViewById(R.id.imgViewDailyMeals);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        register = findViewById(R.id.btnReg);
        login = findViewById(R.id.btnLogin);
        signout = findViewById(R.id.btnSignout);
        loginCardView = findViewById(R.id.cardview_login);
        height = findViewById(R.id.et_height);
        weight = findViewById(R.id.et_weight);

        weightGoals = findViewById(R.id.et_weightGoals);
        calIntakeGoals = findViewById(R.id.et_calIntakeGoals);

        push = findViewById(R.id.btnPush);
        pull = findViewById(R.id.btnPull);


        convert = findViewById(R.id.swtchConvert);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //image stuff
        mealspic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture ();
            }
        });

        //read from database
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewDetails = new Intent(MainActivity.this, ViewDetails.class);
                startActivity(viewDetails);
            }
        });


        //saves to database
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser fbuser = mAuth.getCurrentUser();

                if(convert.isChecked()) {

                    Users user = new Users(height.getText().toString()+"inch", weight.getText().toString()+ "lbs",
                            weightGoals.getText().toString()+"lbs", calIntakeGoals.getText().toString()+"kJ", fbuser.getEmail().toString());

                    myRefUsers.setValue(user);
                }
                else
                {
                    Users user = new Users(height.getText().toString()+"m", weight.getText().toString()+ "kg/s",
                            weightGoals.getText().toString()+"Kg/s", calIntakeGoals.getText().toString()+"kJ", fbuser.getEmail().toString());

                    myRefUsers.setValue(user);
                }

            }
        });

        //sign out
        if(mAuth.getCurrentUser()!=null){
            loginCardView.setVisibility(View.GONE);

        }
        else{

            loginCardView.setVisibility(View.VISIBLE);
        }


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnteredEmail = email.getText().toString().trim();
                String EnteredPswrd = password.getText().toString().trim();
                mAuth.createUserWithEmailAndPassword(EnteredEmail, EnteredPswrd)

                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getEmail() + " successfully registered", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnteredEmail = email.getText().toString();
                String EnteredPassword = password.getText().toString();
                mAuth.signInWithEmailAndPassword(EnteredEmail,EnteredPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    loginCardView.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();


                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(MainActivity.this, "You have signed out", Toast.LENGTH_SHORT).show();
                loginCardView.setVisibility(View.VISIBLE);
            }
        });


    }

    private void choosePicture() {

        Intent intent = new Intent ();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode == RESULT_OK && data.getData() != null ){
            imageUri = data.getData();
            mealspic.setImageURI(imageUri);
            uploadPic();
        }
    }

    private void uploadPic() {
        //set a progress bar. NOT DONE

        final String randomKey   = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                      //  Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(MainActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
        }

    }


}