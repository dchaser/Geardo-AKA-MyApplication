package au.com.geardoaustralia.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.User;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;

public class SignupActivity extends au.com.geardoaustralia.cartNew.BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = SignupActivity.class.getSimpleName();


    private ProgressBar progressBar;
    private FirebaseAuth auth;

    //Facebook
    private CallbackManager mCallbackManager;

    //Google
    private SignInButton google_sign_in_button;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    LinearLayout ll_fb_signin;
    LinearLayout ll_goo_signin;

    EditText etEnterEmail;
    EditText etEnterPassword;
    EditText et_retype_password;
    TextView tvReadAgreement;
    Button create_new_account_button;
    TextView tvSignIn;
    ImageButton ibClose;

    User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SignupActivity.this);

        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = GlobalContext.getFAuthInstance();

        utilKit.hideSoftKeyboard(SignupActivity.this);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }

        google_sign_in_button = (SignInButton) findViewById(R.id.google_sign_in_button);
        google_sign_in_button.setOnClickListener(this);

        ll_fb_signin = (LinearLayout) findViewById(R.id.ll_fb_signin);
        ll_fb_signin.setOnClickListener(this);
        ll_goo_signin = (LinearLayout) findViewById(R.id.ll_goo_signin);
        ll_goo_signin.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        etEnterEmail = (EditText) findViewById(R.id.etEnterEmail);
        etEnterPassword = (EditText) findViewById(R.id.etEnterPassword);
        et_retype_password = (EditText) findViewById(R.id.et_retype_password);
        tvReadAgreement = (TextView) findViewById(R.id.tvReadAgreement);
        create_new_account_button = (Button) findViewById(R.id.create_new_account_button);
        tvSignIn = (TextView) findViewById(R.id.tvSingIn);
        ibClose = (ImageButton) findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // [start initialize_fblogin]
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookLoginResult(loginResult);


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, SignInActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        create_new_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //                imageUrlThumb
                //                        imageUrlSmall
                //                firstname
                //                        lastname
                //                email
                //                        oauth_provider
                //                oauth_uid
                //                        username
                //                password
                //                        etc

                String email = etEnterEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email) || !utilKit.isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Enter valid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = null;

                if (TextUtils.isEmpty(et_retype_password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etEnterPassword.getText().toString().equals(et_retype_password.getText().toString())) {

                    password = et_retype_password.getText().toString();

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() > 30) {
                        Toast.makeText(getApplicationContext(), "Password too long, enter less than 30 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (et_retype_password.getText().toString().matches("[A-Za-z0-9]+")) {

                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords should consist of Numbers and Digits!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //make an MD5 hash out of user password
                    password = utilKit.md5(et_retype_password.getText().toString().trim());

                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    globalUser = new User();
                                    FirebaseUser firebaseUser = task.getResult().getUser();

                                    if(firebaseUser.getDisplayName() != null){
                                        globalUser.display_name = firebaseUser.getDisplayName();
                                    }

                                    if(firebaseUser.getEmail() != null){
                                        globalUser.email = firebaseUser.getEmail();
                                    }

                                    if(firebaseUser.getPhotoUrl() != null){
                                        Bitmap bitmap = null;
                                        try {
                                            Uri photourl = firebaseUser.getPhotoUrl();

                                            if (photourl != null) {
                                                bitmap = MediaStore.Images.Media.getBitmap(SignupActivity.this.getContentResolver(), photourl);
                                                String imageName = globalUser.email.substring(0, 8) + ".png";
                                                utilKit.saveBitmapToInternalStorage(bitmap, imageName);
                                                globalUser.imageUrlSmall = imageName;
                                                globalUser.imageUrlSmall = imageName;

                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }


                                   if(firebaseUser.getUid() != null){
                                       String uid = firebaseUser.getUid();
                                       globalUser.oauth_uid = uid;
                                   }

                                if(globalUser.email != null){
                                    createLocalUser(globalUser);

                                }
//                                    List<UserInfo> info = (List<UserInfo>) firebaseUser.getProviderData();
//                                    String providerId = firebaseUser.getProviderId();
//                                    List<String> providers = firebaseUser.getProviders();



                                }
                            }
                        });

            }
        });
    }

    private void createLocalUser(User user) {

        DatabaseManager.getInstance().saveUserToDB(user, SignupActivity.this);

        startActivity(new Intent(SignupActivity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    // [START auth_with_facebook]
    private void handleFacebookLoginResult(final LoginResult result) {

        final AccessToken token = result.getAccessToken();
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                token,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("SignInActivity", response.toString());

                                        // Application code
                                        try {
                                            if (globalUser == null) {
                                                globalUser = new User();
                                            }
                                            globalUser.oauth_provider = "FACEBOOK";
                                            String facebook_id = object.getString("id");
                                            globalUser.oauth_uid = facebook_id;
                                            String firstname = object.getString("name");
                                            globalUser.display_name = firstname;
//                                            String lastname = object.getString("last_name");
//                                            globalUser.lastname = lastname;
//                                            String profileLink = object.getString("link");
//                                            globalUser.etc = profileLink;
//                                            globalUser.created_at = object.getString("updated_time");
                                            String email = object.getString("email");
                                            globalUser.email = email;

//                                            icon.setVisibility(View.VISIBLE);
//                                            icon.setProfileId(facebook_id);
//                                            ImageView imageViewProfilePicutre = (ImageView) icon.getChildAt(0);
//                                            Bitmap bitmap = ((BitmapDrawable) imageViewProfilePicutre.getDrawable()).getBitmap();
//                                            String imageName = globalUser.email.substring(0, 8) + ".png";
//                                            globalUser.imageUrlSmall = imageName;
//                                            globalUser.imageUrlThumb = imageName;
//                                            utilKit.saveBitmapToInternalStorage(bitmap, imageName);

//
//                                            inputEmail.setText(email);


//                                            display_name.setText(firstname);
//                                            inputEmail.setText(email);

                                            createLocalUser(globalUser);


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                        // [START_EXCLUDE]
                        hideProgressDialog();
//                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                        finish();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (globalUser == null) {
                            globalUser = new User();
                        }

                        String email = acct.getEmail();
                        globalUser.email = email;

                        String firstname = acct.getDisplayName();
                        globalUser.display_name = firstname;
                        String goo_id = acct.getId();
                        globalUser.oauth_provider = "GOOGLE";
                        globalUser.oauth_uid = goo_id;
                        Uri photo = acct.getPhotoUrl();

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(SignupActivity.this.getContentResolver(), photo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String imageName = globalUser.email.substring(0, 8) + ".png";
                        globalUser.imageUrlSmall = imageName;
                        globalUser.imageUrlThumb = imageName;
                        utilKit.saveBitmapToInternalStorage(bitmap, imageName);


//                        display_name.setText(firstname);
//                        inputEmail.setText(email);

                        //create lcal user
                        createLocalUser(globalUser);

                        // [START_EXCLUDE]
                        hideProgressDialog();
//                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
//                        finish();
                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.ll_fb_signin) {
            com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(SignupActivity.this);
            btn.performClick();
        }

        if (i == R.id.ll_goo_signin) {
            signIn();
        }
    }

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


}


