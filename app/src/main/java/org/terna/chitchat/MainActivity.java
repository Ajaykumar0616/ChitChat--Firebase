package org.terna.chitchat;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<chatMessage> adapter ;
    RelativeLayout activity_main;
    FloatingActionButton fab;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main,"You have been signed Out",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            Snackbar.make(activity_main,"SUCCESSFULLY signed in. WELCOME !!!",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(activity_main,"We couldn't sign in you. Please try later...",Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = (RelativeLayout)findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new chatMessage(input.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        //sign in or not
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            displayChatMessage();
        }

        //load content
        //displayChatMessage();
    }

    private void displayChatMessage() {

        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<chatMessage>(this,chatMessage.class,R.layout.lst_item,FirebaseDatabase.getInstance().getReference()) {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            protected void populateView(View v, chatMessage model, int position) {
                TextView messageUser, messageText, messageTime;

                messageText = (TextView)v.findViewById(R.id.message_text);
                messageTime = (TextView)v.findViewById(R.id.message_time);
                messageUser = (TextView)v.findViewById(R.id.message_user);

                messageText.setText(model.getMessagetext());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (hh:mm:ss)",model.getMessageTime()));
                messageUser.setText(model.getMessageUser());

            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
