package nl.wildenberg.maurice_537811;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nl.wildenberg.maurice_537811.Adapters.RecycleViewAdapters;
import nl.wildenberg.maurice_537811.Models.Article;
import nl.wildenberg.maurice_537811.Models.AuthToken;
import nl.wildenberg.maurice_537811.Models.RootObject;
import nl.wildenberg.maurice_537811.Services.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Callback<RootObject>, RecycleViewAdapters.ArticleListener {
    private RecyclerView mRecyclerView;
    private List<Article> articleList;
    private RecycleViewAdapters mAdapter;
    private WebService mService;
    private LinearLayoutManager mLayoutManager;
    private int nextId;
    public AuthToken authTokenInstance;
    public String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Newsreader");
        Context context = this;
        authTokenInstance = AuthToken.getInstance(context);
        authToken = authTokenInstance.GetAuthToken();

        articleList = new ArrayList<>();
        mRecyclerView = findViewById(R.id.overview_list);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(WebService.class);
        getArticles();



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if(firstVisibleItem + visibleItemCount >= totalItemCount){
                    LoadMoreItems loader = new LoadMoreItems();
                    loader.execute();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            articleList.clear();
            getArticles();
            mAdapter.notifyDataSetChanged();
        }
        if(id == R.id.btn_login){
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }


    private class LoadMoreItems extends AsyncTask {
        @Override
        protected List<Article> doInBackground(Object[] params) {
            try {
                Response<RootObject> response;

                if(authToken != null){
                    response = mService.articles(authToken, nextId, 20).execute();
                }
                else {
                    response = mService.articles(nextId, 20).execute();
                }
                    if (response.isSuccessful()) {
                        mAdapter.AddArticles(response.body().Results);
                        nextId = response.body().NextId;
                        mAdapter.notifyDataSetChanged();

                        return response.body().Results;
                    } else {
                        return null;
                    }

            } catch (Exception e) {
                return null;
            }
        }
    }

    public void getArticles()
    {
        if(authTokenInstance.CheckLoggedIn()){
            articleList.clear();
            mService.articles(authToken).enqueue(this);

        }
        else{
            mService.articles().enqueue(this);

        }

    }


    @Override
    public void onResponse(Call<RootObject> call, Response<RootObject> response)
    {
        if (response.body() != null && response.isSuccessful()) {
            RootObject rootObject = response.body();
            articleList.addAll(rootObject.Results);
            nextId = rootObject.NextId;
            mAdapter = new RecycleViewAdapters(this, articleList, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Toast.makeText(getBaseContext(), "Something went wrong, please login again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<RootObject> call, Throwable t) {

    }

    @Override
    public void onItemClick(View view, Article content) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.CONTENT, content);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        getArticles();
    }


}

