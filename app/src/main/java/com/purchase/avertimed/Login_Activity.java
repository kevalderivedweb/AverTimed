package com.purchase.avertimed;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.purchase.avertimed.API.LoginRequest;
import com.purchase.avertimed.API.LoginSocialRequest;
import com.purchase.avertimed.API.UserSession;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

public class Login_Activity extends AppCompatActivity {

    TextView m_sign;
    TextView m_forgot;
    ImageView m_done;
    private EditText email_id;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private EditText password;
    private RequestQueue requestQueue;
    private UserSession session;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.white));

        session = new UserSession(getApplicationContext());
        requestQueue = Volley.newRequestQueue(Login_Activity.this);//Creating the RequestQueue

        printKeyHash();

        m_sign = findViewById(R.id.m_sign);
        m_forgot = findViewById(R.id.m_forgot);
        m_done = findViewById(R.id.m_done);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password);


        FacebookSdk.sdkInitialize(this);
        LoginManager.getInstance().logOut();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loginwith_FB();
            }
        });

        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sign_in_with_gmail();
            }
        });



        m_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_id.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Email Address",Toast.LENGTH_SHORT).show();
                }else if (!email_id.getText().toString().trim().matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }else {
                    GetLogin(email_id.getText().toString(),password.getText().toString(),session.GetFirebasetoken());
                }

            }
        });

        m_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this,SignUp_Activity.class);
                startActivity(intent);
            }
        });
        m_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });
    }



    public void GetLogin2(String Email,String Password){
        AndroidNetworking.post("http://chessmafia.com/php/Avertimed/api/login")
                .addBodyParameter("Email",Email)
                .addBodyParameter("Password",Password)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response + " null");
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("Response", anError.getErrorBody() + " null");
                    }
                });
    }
    public void GetLogin(String Email,String Password,String DeviceToken) {

        final KProgressHUD progressDialog = KProgressHUD.create(Login_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        LoginRequest loginRequest = new LoginRequest(Email,Password,DeviceToken, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Toast.makeText(Login_Activity.this,jsonObject.getString("ResponseMsg"),Toast.LENGTH_SHORT).show();
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

                        Intent intent = new Intent(Login_Activity.this,ProductActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(Login_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Login_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Login_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

    public void printKeyHash()  {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName() , PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhash" , Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    //facebook implimentation
    public void Loginwith_FB(){

        LoginManager.getInstance()
                .logInWithReadPermissions(Login_Activity.this,
                        Arrays.asList("email", "public_profile", "user_friends"));

        // initialze the facebook sdk and request to facebook for login
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>()  {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d("resp_token",loginResult.getAccessToken()+"");
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(Login_Activity.this, "Login Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("resp",""+error.toString());
                Toast.makeText(Login_Activity.this, "Login Error"+error.toString(), Toast.LENGTH_SHORT).show();
            }

        });


    }
    private void handleFacebookAccessToken(final AccessToken token) {
        // if user is login then this method will call and
        // facebook will return us a token which will user for get the info of user
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("resp_token",token.getToken()+"");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Show_loader(Login_Activity.this,false,false);
                            final String id = com.facebook.Profile.getCurrentProfile().getId();
                            GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject user, GraphResponse graphResponse) {

                                    cancel_loader();
                                    Log.d("resp",user.toString());
                                    //after get the info of user we will pass to function which will store the info in our server

                                    String fname=""+user.optString("first_name");
                                    String lname=""+user.optString("last_name");


                                    if(fname.equals("") || fname.equals("null"))
                                        fname=getResources().getString(R.string.app_name);

                                    if(lname.equals("") || lname.equals("null"))
                                        lname="";

                                 /*   Call_Api_For_Signup(""+id,fname
                                            ,lname,
                                            "https://graph.facebook.com/"+id+"/picture?width=500&width=500",
                                            "facebook");
*/
                                    makeLoginwithSocialid("login-with-facebook",fname,lname,"Email","FacebookID",id);


                                }
                            });

                            // here is the request to facebook sdk for which type of info we have required
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "last_name,first_name,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } else {
                            cancel_loader();
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    GoogleSignInClient mGoogleSignInClient;
    public void Sign_in_with_gmail(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Login_Activity.this);
        if (account != null) {
            String id=account.getId();
            String fname=""+account.getGivenName();
            String lname=""+account.getFamilyName();
            String email = ""+account.getEmail();

            String pic_url;
            if(account.getPhotoUrl()!=null) {
                pic_url = account.getPhotoUrl().toString();
            }else {
                pic_url="null";
            }



            if(fname.equals("") || fname.equals("null"))
                fname=getResources().getString(R.string.app_name);

            if(lname.equals("") || lname.equals("null"))
                lname="User";
            // Call_Api_For_Signup(id,fname,lname,pic_url,"gmail");
            makeLoginwithSocialid("login-with-google",fname,lname,email,"GoogleID",id);




        }
        else {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 123);
        }

    }
    //Relate to google login
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String id=account.getId();
                String fname=""+account.getGivenName();
                String lname=""+account.getFamilyName();
                String email = ""+account.getEmail();

                // if we do not get the picture of user then we will use default profile picture

                String pic_url;
                if(account.getPhotoUrl()!=null) {
                    pic_url = account.getPhotoUrl().toString();
                }else {
                    pic_url="null";
                }


                if(fname.equals("") || fname.equals("null"))
                    fname=getResources().getString(R.string.app_name);

                if(lname.equals("") || lname.equals("null"))
                    lname="User";

                makeLoginwithSocialid("login-with-google",fname,lname,email,"GoogleID",id);


                Log.e("DataReg","id : " + id +" fname : " + fname +" lname : " + lname +" pic : " + pic_url );
                //   Call_Api_For_Signup(id,fname,lname,pic_url,"gmail");


            }
        } catch (ApiException e) {
            Log.w("Error message", "signInResult:failed code=" + e.getStatusCode());
        }

    }
    public static KProgressHUD dialog;
    public static void Show_loader(Context context, boolean outside_touch, boolean cancleable) {

         dialog = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();



        if(!outside_touch)
            dialog.setCancellable(false);

        if(!cancleable)
            dialog.setCancellable(false);

        dialog.show();
    }
    public static void cancel_loader(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
    public void makeLoginwithSocialid(String Method,String Fristname,String Lastname,String Email,String Methodname,String MethodId){

        final KProgressHUD progressDialog = KProgressHUD.create(Login_Activity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        LoginSocialRequest loginRequest = new LoginSocialRequest(Method,Fristname,Lastname,Email,Methodname,MethodId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();
                // Response from the server is in the form if a JSON, so we need a JSON Object
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //create shared preference and store data
                  //  session.createLoginSession("UserID","FirstName","LastName","Email","UserType","Language","Currency","APIToken",0,0);

                    Intent intent = new Intent(Login_Activity.this,ProductActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(Login_Activity.this, "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Login_Activity.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Login_Activity.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        });
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        if(requestCode==123){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else if(mCallbackManager!=null)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {

        if(session.isLoggedIn()){
            Intent intent = new Intent(Login_Activity.this,ProductActivity.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserSession userSession = new UserSession(this);
        setLocale(userSession.getLanguageCode());
    }

}