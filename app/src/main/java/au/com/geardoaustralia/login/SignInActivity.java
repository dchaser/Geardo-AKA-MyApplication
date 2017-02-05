package au.com.geardoaustralia.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.User;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGW;
//http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    //Facebook
    private CallbackManager mCallbackManager;
    public ProgressDialog mProgressDialog;

    //Google
    private SignInButton google_sign_in_button;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private static final String TAG = "FacebookLogin";
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    LinearLayout ll_fb_signin;
    LinearLayout ll_goo_signin;
    Button sign_in_button;
    TextView tvForgotPassword;
    Button btn_create_account;
    ImageButton ibClose;


    User globalUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SignInActivity.this);
        // set the view now
        setContentView(R.layout.activity_sign_in);

        //Get Firebase auth instance
        auth = GlobalContext.getFAuthInstance();

        utilKit.hideSoftKeyboard(SignInActivity.this);

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }

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


        ll_fb_signin = (LinearLayout) findViewById(R.id.ll_fb_signin);
        ll_fb_signin.setOnClickListener(this);
        ll_goo_signin = (LinearLayout) findViewById(R.id.ll_goo_signin);
        ll_goo_signin.setOnClickListener(this);
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


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btn_create_account = (Button) findViewById(R.id.btn_create_account);


        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email) || !utilKit.isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Enter valid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() > 30) {
                    Toast.makeText(getApplicationContext(), "Password too long, enter less than 30 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (password.matches("[A-Za-z0-9]+")) {

                } else {
                    Toast.makeText(getApplicationContext(), "Passwords should consist of Numbers and Digits!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(SignInActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });


        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]


    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }
// [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }

    }
// [END on_stop_remove_listener]


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.ll_fb_signin) {
            com.facebook.login.widget.LoginButton btn = new com.facebook.login.widget.LoginButton(SignInActivity.this);
            btn.performClick();
        }

        if (i == R.id.ll_goo_signin) {
            signIn();
        }
    }


    private void createLocalUser(User user) {

        DatabaseManager.getInstance().saveUserToDB(user, SignInActivity.this);

        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //trySetupSwipeRefresh();
        //updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            LOGW(TAG, "No view with ID main_content to fade in.");
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
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
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
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
                            bitmap = MediaStore.Images.Media.getBitmap(SignInActivity.this.getContentResolver(), photo);
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

    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]




}
