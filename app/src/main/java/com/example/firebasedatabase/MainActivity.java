package com.example.firebasedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements ValueEventListener {
    private TextView MgsHeading;
    private Button Submit;
    private EditText MgsSubmit;
    private RadioButton Red, Blue;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mHeadingReference = mRootReference.child("Heading");
    private DatabaseReference mColorReference = mRootReference.child("Color");

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MgsHeading = findViewById(R.id.MgsHeading);
        Submit = findViewById(R.id.Submit);
        MgsSubmit = findViewById(R.id.MgsSubmit);
        Red = findViewById(R.id.red);
        Blue = findViewById(R.id.blue);

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void submitHeading(View view) {
        String heading = MgsSubmit.getText().toString().trim();
        mHeadingReference.setValue(heading);
        MgsSubmit.setText("");

    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.red:
                mColorReference.setValue("red");
                break;
            case R.id.blue:
                mColorReference.setValue("blue");
                break;
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if (dataSnapshot.getValue(String.class) != null) {

            String key = dataSnapshot.getKey();
            assert key != null;
            if (key.equals("Heading")) {
                String heading = dataSnapshot.getValue(String.class);
                MgsHeading.setText(heading);
            } else if (key.equals("Color")) {
                String color = dataSnapshot.getValue(String.class);
                assert color != null;
                if (color.equals("red")) {
                    MgsHeading.setTextColor(ContextCompat.getColor(this, R.color.red));
                    Red.setChecked(true);
                } else if (color.equals("blue")) {
                    MgsHeading.setTextColor(ContextCompat.getColor(this, R.color.blue));
                    Blue.setChecked(true);
                }
            }
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.i("Shagul", databaseError.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHeadingReference.addValueEventListener(this);
        mColorReference.addValueEventListener(this);
    }
}
