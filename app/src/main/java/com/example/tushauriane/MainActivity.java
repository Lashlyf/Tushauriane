package com.example.tushauriane;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;

        import android.os.Bundle;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;


        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;






public class MainActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 123;
    FirebaseAuth auth;
    DatabaseReference databaseReference;

    ReadWriteUserDetails userDetails;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView communities = findViewById(R.id.Communities);
        TextView Communities = findViewById(R.id.text6);
        ImageView chatBot = findViewById(R.id.chatBot);
        TextView Chatbot = findViewById(R.id.text7);
        ImageView mail = findViewById(R.id.imageView5);

        mail.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,ContactActivity.class);
            startActivity(intent);
        });

        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        if (!uid.isEmpty()){
            getUserData();

        }

        chatBot.setOnClickListener(View ->{
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        Chatbot.setOnClickListener(View ->{
            Intent intent = new Intent(MainActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        Communities.setOnClickListener(View ->{
            Intent intent = new Intent(MainActivity.this, Community
                    .class);
            startActivity(intent);
        });

        communities.setOnClickListener(View ->{
            Intent intent = new Intent(MainActivity.this, Community
                    .class);
            startActivity(intent);
        });


    }
    private void getUserData() {

        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails = snapshot.getValue(ReadWriteUserDetails.class);

                TextView userName = findViewById(R.id.name);
                userName.setText(userDetails.username);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }
        });
    }



}
