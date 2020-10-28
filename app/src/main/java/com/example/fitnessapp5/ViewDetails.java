package com.example.fitnessapp5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewDetails extends AppCompatActivity {

    ListView lstViewUserDetails ;
    List<String> userDetailsList;
    ArrayAdapter adapter;
    Users users;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefUsers = database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);


        lstViewUserDetails = findViewById(R.id.lstView_UserDetails);
        myRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new Users();
                userDetailsList = new ArrayList<String>();
                for(DataSnapshot usersfromdatabase : snapshot.getChildren())
                {
                    users = usersfromdatabase.getValue(Users.class);
                    userDetailsList.add(users.ToString());
                }

                adapter = new ArrayAdapter(ViewDetails.this,android.R.layout.simple_list_item_1,
                        (List) lstViewUserDetails);

                lstViewUserDetails.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }
}