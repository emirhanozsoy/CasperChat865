package com.emir.casperchat_865;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private Button btnChangeEmail, btnChangePassword, btnSendResetEmail, btnRemoveUser,
            changeEmail, changePassword, sendEmail, remove, signOut;

    private EditText oldEmail, newEmail, password, newPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String userNamee;
    String friendMail,userName;
    LinearLayout ly_linear,ly_linear_friends,ly_linear_chat,ly_photo;
    private FirebaseAuth mAuth;
    ListView listviewFriend,listViewchat;
    private static final int uniqeID=2134;
    CircleImageView profilepic;
    private ListAdapter adap;
    private List<adapter> mListadapter;
    private StorageReference mStorageRef;
    DatabaseReference mRef,userRef, mailRef, imageRef,userNameRef,messageRef,refTo,refMsg;
    private final static int GALERI_INDEX=2;
    private final static int GALERI_INDEX2=3;
    ArrayList<String> Chatlist;
    private customListAdapter adap2;
    ArrayAdapter<String > arrayAdapter;
    public static String friendImgLink;
    String userName2;
    String text;
    String buffer;
    EditText chattextoo;
    String textt;
    String usrname;
    String person;
    NotificationCompat.Builder notification;
    List<String> texts = new ArrayList<String>();
    String[] strings = new String[texts.size()];
    String oppositeusername;
    private List<customChatadapter> mListadapter2;
    private ProgressBar progressBarmessage;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.my_menu,menu);
        menuInflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.changeUserName){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.change_username,null);
            mBuilder.setView(dialogView);
            final AlertDialog dialogAddNewFriend= mBuilder.create();
            dialogAddNewFriend.show();
            final EditText et_username = (EditText)dialogView.findViewById(R.id.userName);
            Button btn_changeusrname =(Button)dialogView.findViewById(R.id.changeUserName);
            btn_changeusrname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!et_username.getText().toString().isEmpty()){

                        userRef = mRef.child(mAuth.getCurrentUser().getUid().toString());
                        userNameRef=userRef.child("userName");
                        mailRef =userRef.child("mail");
                        userName= et_username.getText().toString();
                        et_username.setText("");
                        dialogAddNewFriend.hide();
                        mailRef.setValue(mAuth.getCurrentUser().getEmail());
                        userNameRef.setValue(userName);
                        
                        //Query delete = mRef.child(mAuth.getCurrentUser().getUid().toString()).child("userName").orderByValue().equalTo(et_username.getText().toString().trim());
                        /* mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    if(et_username.getText().toString().equals(appleSnapshot.child("userName").getValue())){
                                        Toast.makeText(getApplicationContext(),"Aynı",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        userRef = mRef.child(mAuth.getCurrentUser().getUid().toString());
                                        userNameRef=userRef.child("userName");
                                        mailRef =userRef.child("mail");
                                        userName= et_username.getText().toString();
                                        et_username.setText("");
                                        dialogAddNewFriend.hide();
                                        mailRef.setValue(mAuth.getCurrentUser().getEmail());
                                        userNameRef.setValue(userName);
                                    }

                                    /*appleSnapshot.getRef();
                                    userName= et_username.getText().toString();
                                    et_username.setText("");
                                    dialogAddNewFriend.hide();
                                    mailRef.setValue(mAuth.getCurrentUser().getEmail());
                                    userNameRef.setValue(userName);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/


                    }
                }
            });

        }
        else if(item.getItemId()==R.id.addImage){
            Intent galeriINTENT=new Intent(Intent.ACTION_PICK);
            galeriINTENT.setType("image/*");
            startActivityForResult(galeriINTENT,GALERI_INDEX);

            userRef = mRef.child(mAuth.getCurrentUser().getUid().toString());
            imageRef=userRef.child("userImage");


        }
        else if(item.getItemId()==R.id.addFriend){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.add_friend_dialog,null);
            mBuilder.setView(dialogView);
            final AlertDialog dialogAddNewFriend= mBuilder.create();
            dialogAddNewFriend.show();

            final EditText et_frienMail = (EditText)dialogView.findViewById(R.id.friendMail);
            Button btn_addfriend =(Button)dialogView.findViewById(R.id.addFriendButton);
            btn_addfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!et_frienMail.getText().toString().isEmpty()){
                        friendMail= et_frienMail.getText().toString();
                        et_frienMail.setText("");
                        dialogAddNewFriend.hide();

                        mRef.orderByChild("mail").equalTo(friendMail)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                            Map<String, String> mapAddFriend = (Map<String, String>) postSnapshot.getValue();

                                            DatabaseReference frienRef = mRef.child(mAuth.getCurrentUser().getUid()).child("friends");
                                            frienRef.push().setValue(mapAddFriend.get("mail"));
                                            Toast.makeText(getApplicationContext(),"Friend has been added!",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
        else if(item.getItemId()==R.id.logout){
            signOut();

        }
        else if(item.getItemId()==R.id.action_camera){

            Intent galeriINTENT2=new Intent(Intent.ACTION_PICK);
            galeriINTENT2.setType("image/*");
            startActivityForResult(galeriINTENT2,GALERI_INDEX2);
            /*   AlertDialog.Builder mBuilder10 = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.choose_photo,null);
            mBuilder10.setView(dialogView);
            final AlertDialog dialogAddNewFriend= mBuilder10.create();
            dialogAddNewFriend.show();
            Button btn_choose_photo =(Button)dialogView.findViewById(R.id.choose_photo_from_gallery);
            btn_choose_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent galeriINTENT2=new Intent(Intent.ACTION_PICK);
                    galeriINTENT2.setType("image/*");
                    startActivityForResult(galeriINTENT2,GALERI_INDEX2);
                }
            });
            Button btn_take_photo =(Button)dialogView.findViewById(R.id.take_photo);
            btn_choose_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });*/

        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ly_linear_chat.setVisibility(View.VISIBLE);
                    ly_linear.setVisibility(View.INVISIBLE);
                    ly_linear_friends.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_friends:
                    ly_linear_chat.setVisibility(View.INVISIBLE);
                    ly_linear.setVisibility(View.INVISIBLE);
                    ly_linear_friends.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    ly_linear_chat.setVisibility(View.INVISIBLE);
                    ly_linear.setVisibility(View.VISIBLE);
                    ly_linear_friends.setVisibility(View.INVISIBLE);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ly_linear=(LinearLayout) findViewById(R.id.linear_layout);
        ly_linear_friends=(LinearLayout)findViewById(R.id.friends_layout);
        ly_linear_chat=(LinearLayout)findViewById(R.id.chats_layout);
        listviewFriend=(ListView)findViewById(R.id.listview_friendss);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        messageRef=FirebaseDatabase.getInstance().getReference("messaging");
        mRef= FirebaseDatabase.getInstance().getReference("allUsers");
        mListadapter =new ArrayList<>();




        //final String childurl="to_"+mAuth.getCurrentUser().getEmail().toString().substring(0,mAuth.getCurrentUser().getEmail().toString().indexOf("@"));
        //refTo=messageRef.child("to_"+mAuth.getCurrentUser().getEmail().toString().substring(0,mAuth.getCurrentUser().getEmail().toString().indexOf("@")));
        //refMsg=refTo.child("message");
        Chatlist = new ArrayList<>();
        mListadapter2 = new ArrayList<>();
        adap2= new customListAdapter(getApplicationContext(), mListadapter2);
        listViewchat=(ListView) findViewById(R.id.listview_chat);
        listViewchat.setAdapter(arrayAdapter);
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);


        final CircleImageView profilepic=(CircleImageView)findViewById(R.id.profile_pic);
        final TextView profilusername=(TextView)findViewById(R.id.profile_username);

        final TextView usernameshit=(TextView) findViewById(R.id.deneme);
        mRef.orderByChild("mail").equalTo(mAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();


                            usrname=mapUsername.get("userName");
                            person=mapUsername.get("userImage");
                            Uri myUri=Uri.parse(person);

                            Picasso.with(MainActivity.this)
                                    .load(myUri)
                                    .into(profilepic);

                            profilusername.setText(usrname.toUpperCase());
                            //Toast.makeText(getApplicationContext(), usrname, Toast.LENGTH_SHORT).show();
                            usernameshit.setText(usrname);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });


        mRef.orderByChild("mail").equalTo(mAuth.getCurrentUser().getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                            Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();
                            final String childurl="to_"+usrname;
        //refTo=messageRef.child("to_"+mAuth.getCurrentUser().getEmail().toString().substring(0,mAuth.getCurrentUser().getEmail().toString().indexOf("@")));
        //Toast.makeText(getApplicationContext(), childurl, Toast.LENGTH_SHORT).show();
        DatabaseReference ref2 =FirebaseDatabase.getInstance().getReference("messaging").child("messages").child(childurl);
       ref2.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Chatlist.clear();
               mListadapter2.clear();

               notification.setSmallIcon(R.drawable.ghosticon);
               notification.setTicker("ticker");
               notification.setWhen(System.currentTimeMillis());
               notification.setContentTitle("Casp");
               notification.setContentText("New Message");
               //Toast.makeText(getApplicationContext(),"********",Toast.LENGTH_SHORT);
               Intent noti=new Intent(MainActivity.this,MainActivity.class);
               PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,noti,PendingIntent.FLAG_UPDATE_CURRENT);

               notification.setContentIntent(pendingIntent);

               NotificationManager nm =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

               DataSnapshot ds_Chat=dataSnapshot.child("from");
               for (DataSnapshot dsChat2: ds_Chat.getChildren()){

                   final String chattext =dsChat2.getValue().toString();
                   final String textt=chattext.substring(chattext.indexOf("_"),chattext.indexOf("__"));
                   final String fromm=chattext.substring(0,chattext.indexOf("_"));
                   final String timee=chattext.substring(chattext.indexOf("__"),chattext.length()-1);
                   final String mailson=fromm;
                   texts.add(0, textt);
                   //Collections.reverse(texts);

                   strings = texts.toArray(strings);
                   //Toast.makeText(getApplicationContext(),friendImgLink,Toast.LENGTH_SHORT).show();

                   mRef.orderByChild("mail").equalTo(mailson)
                           .addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   for (DataSnapshot dsFriendDetail: dataSnapshot.getChildren()){
                                       Map<String, String> mapFriendDetail = (Map<String ,String>)dsFriendDetail.getValue();

                                       friendImgLink = mapFriendDetail.get("userImage");

                                       //Toast.makeText(getApplicationContext(),friendImgLink,Toast.LENGTH_SHORT).show();
                                       //mListadapter2.add(0, new customChatadapter(chattext,friendImgLink));
                                   }
                                   listViewchat.invalidateViews();
                               }
                               @Override
                               public void onCancelled(DatabaseError databaseError) {
                               }
                            });
                    //Toast.makeText(getApplicationContext(),friendImgLink,Toast.LENGTH_SHORT).show();
                    mListadapter2.add(0,new customChatadapter(fromm,friendImgLink));
                   listViewchat.invalidateViews();
               }
               //Collections.reverse(texts);
               listViewchat.setAdapter(adap2);
               listViewchat.invalidateViews();
               nm.notify(uniqeID,notification.build());
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
           }
       });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                });

        //Toast.makeText(getApplicationContext(),friendImgLink,Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder mBuilder1 = new AlertDialog.Builder(this);
        listViewchat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                View dialogView = getLayoutInflater().inflate(R.layout.message,null);
                mBuilder1.setView(dialogView);
                final AlertDialog dialogAddNewFriend= mBuilder1.create();
                dialogAddNewFriend.show();
                //chattextoo=(EditText) view.findViewById(R.id.textView);
                final TextView newtextview = (TextView) dialogView.findViewById(R.id.textView2);
                final EditText et_message_text = (EditText)dialogView.findViewById(R.id.reply_message_content);
                final Button btn_send_message =(Button)dialogView.findViewById(R.id.reply_message);
                final  TextView tvFriendMail=(TextView)view.findViewById(R.id.txtMessage);
                //bprogressBarmessage = (ProgressBar) dialogView.findViewById(R.id.progressBarmessage);
                final int positionlistview = position;
                //Toast.makeText(getApplication(),positionlistview+ "  ",Toast.LENGTH_SHORT).show();
                final int stringlength=0;
                final ImageView inComingPhoto=(ImageView)dialogView.findViewById(R.id.inComingPhotoMessage);
                ly_photo=(LinearLayout)dialogView.findViewById(R.id.photolayout);

                mRef.orderByChild("mail").equalTo(mAuth.getCurrentUser().getEmail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                                    Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();
                                                                    final String childurl="to_"+usrname;
                final DatabaseReference ref3 =FirebaseDatabase.getInstance().getReference("messaging").child("messages").child(childurl);
                ref3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot ds_Chat = dataSnapshot.child("from");
                        for (DataSnapshot dsChat2 : ds_Chat.getChildren()) {
                            List<String> texts = new ArrayList<String>();
                            final String chattext = dsChat2.getValue().toString();
                            textt = chattext.substring(chattext.indexOf("_") + 1, chattext.indexOf("__"));
                            final String fromm = chattext.substring(0, chattext.indexOf("_"));
                            //final String timee=chattext.substring(chattext.indexOf("__"),chattext.length());
                            String urlphoto = "_https://firebasestorage.googleapis.com";

                           if(strings[positionlistview].length()>159){
                               String onemli=strings[positionlistview].substring(0,urlphoto.length());
                                if(onemli.equals(urlphoto)){


                                    ly_photo.setVisibility(View.VISIBLE);
                                    newtextview.setVisibility(View.INVISIBLE);
                                    Uri myUri=Uri.parse(strings[positionlistview].substring(1,strings[positionlistview].length()));

                                   Picasso.with(MainActivity.this)
                                            .load(myUri)
                                            .resize(768,1024)
                                            .into(inComingPhoto);
                                }
                                else {
                                    newtextview.setText(strings[positionlistview].substring(1,strings[positionlistview].length()));
                                    ly_photo.setVisibility(View.INVISIBLE);
                                    //Toast.makeText(getApplication(), onemli+"  ",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {

                                newtextview.setText(strings[positionlistview].substring(1,strings[positionlistview].length()));
                               ly_photo.setVisibility(View.INVISIBLE);
                                //Toast.makeText(getApplication(), strings[positionlistview].length() +"  ",Toast.LENGTH_SHORT).show();
                            }


                            final String message1 = et_message_text.getText().toString();

                            /*
                                       messageRef.child("messages").child("to_"+parsed).child("message").push().setValue(message);
                                     //messageRef.child("messages").child("to").push().setValue(tvFriendMail.getText().toString());
                                       messageRef.child("messages").child("to_"+parsed).child("time").push().setValue(currentTime.toString().substring(10,16));
                                        */

                           // progressBarmessage.setVisibility(View.VISIBLE);
                            btn_send_message.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Date currentTime = Calendar.getInstance().getTime();
                                    String message1 = et_message_text.getText().toString();
                                    if(et_message_text.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "Null text is not available :)", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        messageRef.child("messages").child("to_" + tvFriendMail.getText().toString()).child("from").push().setValue(usrname + "_" + message1 + "__" + currentTime.toString().substring(11, 16));
                                        String message = et_message_text.getText().toString();
                                        Toast.makeText(getApplicationContext(), chattext, Toast.LENGTH_SHORT).show();


                                        Query delete = ref3.child("from").orderByValue().equalTo(chattext);
                                        delete.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        dialogAddNewFriend.hide();
                                        //progressBarmessage.setVisibility(View.GONE);
                                        et_message_text.setText("");
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                                                                }
                                                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        });

                ////////
            }
        });
        Collections.reverse(texts);
        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("allUsers").child(mAuth.getCurrentUser().getUid());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dsArkadaslar = dataSnapshot.child("friends");
                for (DataSnapshot friend : dsArkadaslar.getChildren())
                {
                    mListadapter.clear();

                    String friendEmail = friend.getValue(String.class);

                    mRef.orderByChild("mail").equalTo(friendEmail)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dsFriendDetail: dataSnapshot.getChildren()){
                                        Map<String, String> mapFriendDetail = (Map<String ,String>)dsFriendDetail.getValue();
                                        String friendImgLink = mapFriendDetail.get("userImage");
                                        mListadapter.add(new adapter(mapFriendDetail.get("mail"), friendImgLink));
                                    }
                                    listviewFriend.invalidateViews();
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

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        listviewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View dialogView = getLayoutInflater().inflate(R.layout.send_message,null);
                mBuilder.setView(dialogView);
                final AlertDialog dialogAddNewFriend= mBuilder.create();

                dialogAddNewFriend.show();
                final  TextView tvFriendMail=(TextView)view.findViewById(R.id.textView2FrienMail);
                final EditText et_message_text = (EditText)dialogView.findViewById(R.id.message_text);
                final String parsed = tvFriendMail.getText().toString();
                Button btn_send_message =(Button)dialogView.findViewById(R.id.send_message);
                btn_send_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Date d = new Date();

                        final Date currentTime = Calendar.getInstance().getTime();

                        final String message=et_message_text.getText().toString();

                        if(et_message_text.getText().toString().isEmpty())
                        {
                            Toast.makeText(getApplicationContext(), "Null text is not available :)", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            mRef.orderByChild("mail").equalTo(parsed)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                                                Map<String, String> mapUsername = (Map<String, String>) postSnapshot.getValue();

                                                oppositeusername=mapUsername.get("userName");
                                                Toast.makeText(getApplicationContext(), oppositeusername, Toast.LENGTH_SHORT).show();
                                                messageRef.child("messages").child("to_" + oppositeusername).child("from").push().setValue(usrname + "_" + message + "__" + currentTime.toString().substring(11, 16));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            //messageRef.child("messages").child("to_" + oppositeusername).child("from").push().setValue(usrname + "_" + message + "__" + currentTime.toString().substring(11, 16));
                        }/*
                        messageRef.child("messages").child("to_"+parsed).child("message").push().setValue(message);
                        //messageRef.child("messages").child("to").push().setValue(tvFriendMail.getText().toString());
                        messageRef.child("messages").child("to_"+parsed).child("time").push().setValue(currentTime.toString().substring(10,16));
                        */
                        et_message_text.setText("");
                        dialogAddNewFriend.hide();

                    }
                });
            }
        });
        adap =new listAdapter(getApplicationContext(),mListadapter);
        listviewFriend=(ListView)findViewById(R.id.listview_friendss);
        listviewFriend.setAdapter(adap);
        listviewFriend.invalidateViews();



        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle(getString(R.string.app_name));
        //setSupportActionBar(toolbar);


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };



        btnChangeEmail = (Button) findViewById(R.id.change_email_button);
        btnChangePassword = (Button) findViewById(R.id.change_password_button);
        btnSendResetEmail = (Button) findViewById(R.id.sending_pass_reset_button);
        btnRemoveUser = (Button) findViewById(R.id.remove_user_button);
        changeEmail = (Button) findViewById(R.id.changeEmail);
        changePassword = (Button) findViewById(R.id.changePass);
        sendEmail = (Button) findViewById(R.id.send);
        remove = (Button) findViewById(R.id.remove);
        signOut = (Button) findViewById(R.id.sign_out);

        oldEmail = (EditText) findViewById(R.id.old_email);
        newEmail = (EditText) findViewById(R.id.new_email);
        password = (EditText) findViewById(R.id.password);
        newPassword = (EditText) findViewById(R.id.newPassword);

        oldEmail.setVisibility(View.GONE);
        newEmail.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        newPassword.setVisibility(View.GONE);
        changeEmail.setVisibility(View.GONE);
        changePassword.setVisibility(View.GONE);
        sendEmail.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
            }
        });

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newEmail.getText().toString().trim().equals("")) {
                    user.updateEmail(newEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                        signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else if (newEmail.getText().toString().trim().equals("")) {
                    newEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.GONE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.VISIBLE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                sendEmail.setVisibility(View.GONE);
                remove.setVisibility(View.GONE);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.getText().toString().trim().equals("")) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            signOut();
                                            progressBar.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnSendResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldEmail.setVisibility(View.VISIBLE);
                newEmail.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                changeEmail.setVisibility(View.GONE);
                changePassword.setVisibility(View.GONE);
                sendEmail.setVisibility(View.VISIBLE);
                remove.setVisibility(View.GONE);
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!oldEmail.getText().toString().trim().equals("")) {
                    auth.sendPasswordResetEmail(oldEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Reset password email is sent!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                } else {
                    oldEmail.setError("Enter email");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        btnRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALERI_INDEX && resultCode==RESULT_OK){
            Uri uri = data.getData();
            StorageReference path = mStorageRef.child("userimages").child(mAuth.getCurrentUser().getEmail());
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.setValue(taskSnapshot.getDownloadUrl().toString());
                    Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(requestCode==GALERI_INDEX2 && resultCode==RESULT_OK){
            Uri uri = data.getData();

            Date currentTime = Calendar.getInstance().getTime();
            StorageReference path = mStorageRef.child("photomessage").child(mAuth.getCurrentUser().getEmail()+currentTime.toString());
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Intent intent = new Intent(MainActivity.this, ChooseFriend.class);
                    intent.putExtra("imagephoto",taskSnapshot.getDownloadUrl().toString());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

}



