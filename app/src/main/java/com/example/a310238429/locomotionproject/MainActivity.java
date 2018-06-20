package com.example.a310238429.locomotionproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
private FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirestore();
    }
    public void onAddItemsClicked(View v) {
        // Get a reference to the restaurants collection
        final CollectionReference users = mFirestore.collection("Users");
        final TextView username = (TextView)findViewById(R.id.usernameEditText);
        final TextView email = (TextView)findViewById(R.id.EmailEditTxt);
        final TextView password = (TextView)findViewById(R.id.PasswordEditText);
        final boolean[] userExist = {false};

        mFirestore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                       User user = document.toObject(User.class);
                                       if(user.Name.equals(username.getText().toString())) {
                                           userExist[0] = true;
                                       }
                            }
                            if(!userExist[0]) {
                                users.add(new User(username.getText().toString(), email.getText().toString(), password.getText().toString()));
                            }
                        }
                    }
                });

/*        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/

    }
    public void AddUserToDatabase(View v) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");

// Creating new user node, which returns the unique key value
// new user node would be /users/$userid/
        String userId = mDatabase.push().getKey();

        TextView username = (TextView)findViewById(R.id.usernameEditText);
        TextView email = (TextView)findViewById(R.id.EmailEditTxt);
        TextView password = (TextView)findViewById(R.id.PasswordEditText);

// creating user object
        User user = new User(username.getText().toString(), email.getText().toString(),password.getText().toString());

// pushing user to 'users' node using the userId
        mDatabase.child(userId).setValue(user);
    }
    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }
}
