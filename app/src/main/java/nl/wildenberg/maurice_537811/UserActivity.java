package nl.wildenberg.maurice_537811;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.wildenberg.maurice_537811.Models.AuthToken;
import nl.wildenberg.maurice_537811.Models.User;
import nl.wildenberg.maurice_537811.Services.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class UserActivity extends AppCompatActivity implements Callback<User> {

    public User user;
    public AuthToken authToken;
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private Button logoutBtn;
    private WebService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.btn_login);
        Context context = this;
        authToken = AuthToken.getInstance(context);
        logoutBtn = findViewById(R.id.btn_logout);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(WebService.class);

        if(authToken.CheckLoggedIn()){
            loginBtn.setVisibility(View.GONE);
            username.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);

        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                login();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                logout();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // TODO: Implement successful signup logic here
            // By default we just finish the Activity and log them in automatically
            this.finish();
        }
    }


    private void login(){

        loginBtn.setEnabled(false);

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        mService.getAuth(usernameText, passwordText).enqueue(this);

    }

    private void logout(){
        logoutBtn.setEnabled(false);
        authToken.DeleteAuthToken(this);

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        loginBtn.setVisibility(View.VISIBLE);
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        logoutBtn.setVisibility(View.VISIBLE);
    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(), "Your username or password was incorrect", Toast.LENGTH_LONG).show();

        loginBtn.setEnabled(true);
    }

    public void onLoginSuccess(){
        Toast.makeText(getBaseContext(), "You're logged in", Toast.LENGTH_LONG).show();
        logoutBtn.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        return;
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
           user = response.body();

            authToken.SetAuthToken(user.AuthToken);

            onLoginSuccess();



        } else {
            onLoginFailed();

        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        onLoginFailed();
    }

}