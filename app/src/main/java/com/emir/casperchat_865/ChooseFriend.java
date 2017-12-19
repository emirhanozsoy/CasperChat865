package com.emir.casperchat_865;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChooseFriend extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth mAuth;
    ListView list_view_friend;
    private static final int uniqeID = 2134;
    CircleImageView profilepic;
    private ListAdapter adap;
    private List<adapter> mListadapter;
    private StorageReference mStorageRef;
    DatabaseReference mRef, userRef, mailRef, imageRef, userNameRef, messageRef, refTo, refMsg;
    private final static int GALERI_INDEX = 2;
    private final static int GALERI_INDEX2 = 3;
    String oppositeusername;
    String usrname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        messageRef = FirebaseDatabase.getInstance().getReference("messaging");
        mRef = FirebaseDatabase.getInstance().getReference("allUsers");
        mListadapter = new ArrayList<>();
        list_view_friend = (ListView) findViewById(R.id.list_view_friend);

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("allUsers").child(mAuth.getCurrentUser().getUid());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dsArkadaslar = dataSnapshot.child("friends");
                for (DataSnapshot friend : dsArkadaslar.getChildren()) {
                    mListadapter.clear();
                    String friendEmail = friend.getValue(String.class);
                    mRef.orderByChild("mail").equalTo(friendEmail)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dsFriendDetail : dataSnapshot.getChildren()) {
                                        Map<String, String> mapFriendDetail = (Map<String, String>) dsFriendDetail.getValue();
                                        String friendImgLink = mapFriendDetail.get("userImage");
                                        mListadapter.add(new adapter(mapFriendDetail.get("mail"), friendImgLink));
                                    }
                                    list_view_friend.invalidateViews();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRef.orderByChild("mail").equalTo(mAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();
                            usrname=mapUsername.get("userName");
                            final String childurl = "to_" + usrname;
                            list_view_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    final TextView tvFriendMail = (TextView) view.findViewById(R.id.textView2FrienMail);
                                    final String parsed = tvFriendMail.getText().toString();
                                    final Date currentTime = Calendar.getInstance().getTime();
                                    final String photolink;
                                    Bundle extras = getIntent().getExtras();
                                    photolink = extras.getString("imagephoto");

                                    mRef.orderByChild("mail").equalTo(parsed)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();

                                                oppositeusername=mapUsername.get("userName");
                                                Toast.makeText(getApplicationContext(), oppositeusername, Toast.LENGTH_SHORT).show();
                                                messageRef.child("messages").child("to_" + oppositeusername).child("from").push().setValue(usrname + "_" + photolink + "__" + currentTime.toString().substring(11, 16));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    //messageRef.child("messages").child("to_" + parsed).child("from").push().setValue(usrname + "_" + photolink + "__" + currentTime.toString().substring(11, 16));
                                    Intent intent = new Intent(ChooseFriend.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            adap = new listAdapter(getApplicationContext(), mListadapter);
                            list_view_friend.setAdapter(adap);
                            list_view_friend.invalidateViews();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
