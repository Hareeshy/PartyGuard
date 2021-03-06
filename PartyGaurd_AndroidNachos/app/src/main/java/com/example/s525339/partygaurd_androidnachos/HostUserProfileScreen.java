package com.example.s525339.partygaurd_androidnachos;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This is where Host user can see his profile.He can also edit the details.
 */

public class HostUserProfileScreen extends AppCompatActivity {

    EditText fname;
    EditText lname;
    EditText email;
    EditText mobileNumber;
    EditText dateOfBirth;
    Button edit;
    Button save;

    String firstName;
    String lastName;
    String emailId;
    String phone;



    String userChoosenTask;
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 1;
    ImageView profileImageView;
    Button logOutBTN;
    ComplaintSingleton complaintSingleton;

    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginEditor;
    private final static String USERNAME_KEY = "partyGuardUser";
    private final static String PASSWORD_KEY = "partyGuardPwd";
    String azure_server = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_user_profile_screen);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            azure_server = bundle.getString("azure_server");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("AZURE_SERVICE", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        loginPrefs= getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginEditor=loginPrefs.edit();

        Button profileBTN = (Button) findViewById(R.id.profileTabBTN);
        profileBTN.setBackgroundColor(getResources().getColor(R.color.IndianRed));
        profileBTN.setTextColor(getResources().getColor(R.color.colorWhite));

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar, null);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        fname = (EditText) findViewById(R.id.profileFirstNameET);
        lname = (EditText) findViewById(R.id.profileLastNameET);
        email = (EditText) findViewById(R.id.profileEmailET);
        mobileNumber = (EditText) findViewById(R.id.profileContactET);
        dateOfBirth = (EditText) findViewById(R.id.profileDOBET);

        edit = (Button)findViewById(R.id.profileEditBtn);
        save = (Button)findViewById(R.id.profileSaveBtn);
        logOutBTN= (Button) findViewById(R.id.logOutBtn);

        save.setVisibility(View.INVISIBLE);
        fname.setEnabled(false);
        lname.setEnabled(false);
        email.setEnabled(false);
        mobileNumber.setEnabled(false);
        dateOfBirth.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname.setEnabled(true);
                fname.setCursorVisible(true);
                lname.setEnabled(true);
                email.setEnabled(true);
                mobileNumber.setEnabled(true);
                dateOfBirth.setEnabled(true);
                fname.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                lname.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                email.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                mobileNumber.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                dateOfBirth.setBackground(getResources().getDrawable(R.drawable.edittextstyle));
                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname.setEnabled(false);
                lname.setEnabled(false);
                email.setEnabled(false);
                mobileNumber.setEnabled(false);
                dateOfBirth.setEnabled(false);
                fname.setBackgroundResource(0);
                lname.setBackgroundResource(0);
                email.setBackgroundResource(0);
                mobileNumber.setBackgroundResource(0);
                dateOfBirth.setBackgroundResource(0);
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
            }
        });

        logOutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEditor.remove(USERNAME_KEY);
                loginEditor.remove(PASSWORD_KEY);
                loginEditor.clear();
                loginEditor.commit();
                finish();
//                loginPrefs.getString(USERNAME_KEY,"");
//                String sharedPassword = loginPrefs.getString(PASSWORD_KEY,"");
                String vv=loginPrefs.getString(USERNAME_KEY,"");
                String cc=loginPrefs.getString(PASSWORD_KEY,"");
                if (loginPrefs.getString(USERNAME_KEY,"")=="" && loginPrefs.getString(PASSWORD_KEY,"")==""){
                    Intent logOutIntent=new Intent(HostUserProfileScreen.this,LoginActivity.class);
                    startActivity(logOutIntent);
                }
            }
        });


                Button alertBTN = (Button) findViewById(R.id.alertTabBTN);
        alertBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alertIntent = new Intent(getApplicationContext(), HostAlertsActivity.class);
                startActivity(alertIntent);
            }
        });

        Button pgTeamBTN = (Button) findViewById(R.id.pgTeamTabBTN);
        pgTeamBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pgTeamIntent = new Intent(getApplicationContext(), PgteamActivity.class);
                startActivity(pgTeamIntent);
            }
        });


        Button historyBTN = (Button) findViewById(R.id.historyTabBTN);
        historyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), History.class);
                startActivity(historyIntent);
            }
        });

        complaintSingleton = ComplaintSingleton.getInstance();
        firstName = complaintSingleton.getFirstName_UserInfo();
        lastName = complaintSingleton.getLastName_UserInfo();
        emailId = complaintSingleton.getEmail_UserInfo();
        phone = complaintSingleton.getPhoneNumber_UserInfo();

        fname.setText(firstName);
        lname.setText(lastName);
        email.setText(emailId);
        mobileNumber.setText(phone);
        dateOfBirth.setText("10-08-1990");

    }
    public void imageUpload(View v){
        selectImage();
    }



//Selects the image from the library
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(HostUserProfileScreen.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(HostUserProfileScreen.this);


                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
//                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
//                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImageView.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        profileImageView.setImageBitmap(thumbnail);
    }


    }

