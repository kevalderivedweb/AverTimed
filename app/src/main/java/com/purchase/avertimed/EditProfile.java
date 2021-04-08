package com.purchase.avertimed;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.purchase.avertimed.API.ServerUtils;
import com.purchase.avertimed.API.UpdateProfile;
import com.purchase.avertimed.API.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;

    private TextView first_name,last_name,email,country,state,city,address,mobile;
    private RequestQueue requestQueue;
    private UserSession session;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private ImageView upload_result,re_change,upload_img;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileedit);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView Update = findViewById(R.id.update);
        first_name = findViewById(R.id.fs);
        last_name = findViewById(R.id.ls);
        email = findViewById(R.id.email);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.mobile);

        requestQueue = Volley.newRequestQueue(EditProfile.this);//Creating the RequestQueue

        upload_result = (ImageView) findViewById(R.id.upload_result);
        re_change = (ImageView) findViewById(R.id.re_change);
        upload_img = (ImageView) findViewById(R.id.upload_img);



        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        re_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        session = new UserSession(getApplicationContext());

        first_name.setText(session.getFirstName());
        last_name.setText(session.getLastName());
        email.setText(session.getEmail());

        country.setText(session.getProfileCountry());
        state.setText(session.getProfileState());
        city.setText(session.getProfileCity());
        address.setText(session.getProfileAddress());
        mobile.setText(session.getProfileMobile());



        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(first_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter First Name",Toast.LENGTH_SHORT).show();
                }else if(last_name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Last Name",Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email Id",Toast.LENGTH_SHORT).show();
                }else if(country.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Country",Toast.LENGTH_SHORT).show();
                }else if(state.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter State",Toast.LENGTH_SHORT).show();
                }else if(city.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter City",Toast.LENGTH_SHORT).show();
                }else if(address.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_SHORT).show();
                }else if(mobile.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Mobile",Toast.LENGTH_SHORT).show();
                }else {
                    UpdateProfile(first_name.getText().toString(),
                            last_name.getText().toString(),
                            email.getText().toString(),
                            country.getText().toString(),
                            state.getText().toString(),
                            city.getText().toString(),
                            address.getText().toString(),
                            mobile.getText().toString(),destination);
                }
            }
        });

    }

    private void GetChangePassword(String oldPassword, String newPasseord, String confrimPassword) {


        final KProgressHUD progressDialog = KProgressHUD.create(EditProfile.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        UpdateProfile loginRequest = new UpdateProfile(oldPassword,newPasseord,confrimPassword, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                session.logout();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(EditProfile.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
                    if (jsonObject.getString("ResponseCode").equals("200")) {

                        JSONObject object = jsonObject.getJSONObject("data");
                        String UserID = object.getString("UserID");
                        String FirstName = object.getString("FirstName");
                        String LastName = object.getString("LastName");
                        String Email = object.getString("Email");
                        String UserType = object.getString("UserType");
                        String Language = object.getString("Language");
                        String Currency = object.getString("Currency");
                        String APIToken = object.getString("APIToken");
                        String mCurrency = object.getString("Currency");
                        String CurrencySign = object.getString("CurrencySign");
                        int IsEnablePushNotification = object.getInt("IsEnablePushNotification");
                        int IsActive = object.getInt("IsActive");
                        session.createLoginSession(UserID,FirstName,LastName,Email,UserType,Language,Currency,APIToken,IsEnablePushNotification,IsActive,mCurrency,CurrencySign);


                        finish();
                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(EditProfile.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(EditProfile.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(EditProfile.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }){@Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Accept", "application/json");
            params.put("Authorization","Bearer "+ session.getAPIToken());
            return params;
        }};
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }


    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {getString(R.string.take_photo), getString(R.string.gallary),getString(R.string.cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
                builder.setTitle(R.string.select_option);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals(getResources().getString(R.string.take_photo))) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals(getResources().getString(R.string.gallary))) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals(getResources().getString(R.string.cancel))) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                checkAndroidVersion();
            //Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            checkAndroidVersion();
            //Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "BonCopBadCop2");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        "BonCopBadCop2", "IMG_" + timeStamp + ".jpg");
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
                upload_img.setVisibility(View.INVISIBLE);
                re_change.setVisibility(View.VISIBLE);
                imgPath = destination.getAbsolutePath();
                Glide.with(Objects.requireNonNull(EditProfile.this)).asBitmap().load(bitmap).circleCrop().into(upload_result);
                //txt_injury.setText(imgPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                Log.e("Activity", "Pick from Gallery::>>> ");

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                //  txt_injury.setText(imgPath);
                Glide.with(Objects.requireNonNull(EditProfile.this)).asBitmap().load(bitmap).circleCrop().into(upload_result);

                upload_img.setVisibility(View.INVISIBLE);
                re_change.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        } else {
            // code for lollipop and pre-lollipop devices
        }

    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(EditProfile.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(EditProfile.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS= 7;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(EditProfile.this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditProfile.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    public void UpdateProfile(String FS, String LS, String Email, final String Country, final String State, final String City, final String Address, final String Mob, File file){
        final KProgressHUD progressDialog = KProgressHUD.create(EditProfile.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        progressDialog.show();


        AndroidNetworking.upload(ServerUtils.BASE_URL+"update-profile")
                .addMultipartFile("ProfileImage",file)
                .addHeaders("Accept","application/json")
                .addHeaders("Authorization","Bearer "+ session.getAPIToken())
                .addMultipartParameter("FirstName",FS)
                .addMultipartParameter("LastName",LS)
                .addMultipartParameter("Email",Email)
                .addMultipartParameter("Country",Country)
                .addMultipartParameter("Longitude","1")
                .addMultipartParameter("State",State)
                .addMultipartParameter("City",City)
                .addMultipartParameter("Address",Address)
                .addMultipartParameter("MobileNo",Mob)
                .addMultipartParameter("Latitude","1")
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                }).getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                Log.e("Response : ", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.getInt("ResponseCode")==200) {

                        JSONObject object = jsonObject.getJSONObject("data");
                        String UserID = object.getString("UserID");
                        String FirstName = object.getString("FirstName");
                        String LastName = object.getString("LastName");
                        String Email = object.getString("Email");
                        String UserType = object.getString("UserType");
                        String Language = object.getString("Language");
                        String Currency = object.getString("Currency");
                        String APIToken = object.getString("api_token");
                        String mCurrency = object.getString("Currency");
                        session.createProfileSession(Country,State,City,Address,Mob);
                        String CurrencySign = object.getString("CurrencySign");
                        int IsEnablePushNotification = object.getInt("IsEnablePushNotification");
                        int IsActive = object.getInt("IsActive");
                        session.createLoginSession(UserID,FirstName,LastName,Email,UserType,Language,Currency,APIToken,IsEnablePushNotification,IsActive,mCurrency,CurrencySign);

                        finish();
                    }else {

                        Toast.makeText(EditProfile.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(ANError anError) {
                finish();
                progressDialog.dismiss();
                Log.e("Error",anError.getErrorBody());
                Toast.makeText(EditProfile.this,"Unauthenticated",Toast.LENGTH_SHORT).show();
            }
        });

    }

}