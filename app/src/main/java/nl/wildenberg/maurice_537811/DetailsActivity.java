package nl.wildenberg.maurice_537811;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;


import nl.wildenberg.maurice_537811.Models.Article;
import nl.wildenberg.maurice_537811.Models.AuthToken;
import nl.wildenberg.maurice_537811.Services.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mdwil on 11-10-2017.
 */

public class DetailsActivity extends Activity implements Callback {
    public static final String CONTENT = "Article";
    private WebService mService;
    Button like;
    Button dislike;
    AuthToken authTokenInstance;
    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_main);
        Context context = this;
        authTokenInstance = AuthToken.getInstance(context);
        authToken = authTokenInstance.GetAuthToken();

        TextView url = findViewById(R.id.url);
        final Article content = getIntent().getParcelableExtra(CONTENT);
        ImageView image = findViewById(R.id.relative_views_image);
        TextView title = findViewById(R.id.title_text);
        TextView summary = findViewById(R.id.description_text);
        Glide.with(this).load(content.Image).centerCrop().crossFade().into(image);
        like = findViewById(R.id.like_button);
        dislike = findViewById(R.id.disliked_button);
        title.setText(content.Title);

        summary.setText(content.Summary);
        url.setText(content.Url);
        Linkify.addLinks(url, Linkify.WEB_URLS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(WebService.class);



        if (authTokenInstance.CheckLoggedIn()) {
            if (content.IsLiked) {
                like.setVisibility(View.GONE);
                dislike.setVisibility((View.VISIBLE));
            } else {
                like.setVisibility(View.VISIBLE);
                dislike.setVisibility(View.GONE);
            }
        } else {
            like.setVisibility(View.GONE);
            dislike.setVisibility(View.GONE);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int articleId = content.Id;
                likeArticle(articleId);
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int articleId = content.Id;
                dislikeArticle(articleId);
            }
        });


    }
    public void likeArticle(int articleId){
        mService.Like(authToken, articleId ).enqueue(this);
        like.setText("Dit item is geliked");

        setResult(2);
        finish();

    }
    public void dislikeArticle(int articleId){
        mService.UnLike(authToken, articleId ).enqueue(this);
        dislike.setText("Dit item is gedisliked");

        setResult(2);
        finish();
    }

    @Override
    public void onResponse(Call call, Response response) {


    }

    @Override
    public void onFailure(Call call, Throwable t) {

    }
}
