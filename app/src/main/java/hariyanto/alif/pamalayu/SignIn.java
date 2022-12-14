package hariyanto.alif.pamalayu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hariyanto.alif.pamalayu.Common.Common;
import hariyanto.alif.pamalayu.Model.User;

public class SignIn extends AppCompatActivity {

    EditText edPhone,edPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edPhone = (EditText) findViewById(R.id.edPhone);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting . . .");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Mengecek Jika user tidak ada di database
                        if (dataSnapshot.child(edPhone.getText().toString()).exists()) {

                            //Mendapat Informasi User
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edPhone.getText().toString());
                            if (user.getPassword().equals(edPassword.getText().toString()))
                            {
                                {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    Toast.makeText(SignIn.this, "Sign in Successfully !", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }else{
                                Toast.makeText(SignIn.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}