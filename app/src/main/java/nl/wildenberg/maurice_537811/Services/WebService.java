package nl.wildenberg.maurice_537811.Services;


import java.util.ArrayList;

import nl.wildenberg.maurice_537811.Models.RootObject;
import nl.wildenberg.maurice_537811.Models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mdwil on 8-10-2017.
 */

public interface WebService {

    @GET("articles")
    Call<RootObject> articles();

    @GET("articles")
    Call<RootObject> articles(@Header("x-authtoken") String token);

    @GET("articles")
    Call<RootObject> articles(@Query("id")int nextid, @Query("id")int count);

    @GET("articles")
    Call<RootObject> articles(@Header("x-authtoken") String token, @Query("id")int nextid, @Query("id")int count);

    @FormUrlEncoded
    @POST("Users/login")
    Call<User> getAuth(@Field("username") String username, @Field("password") String password);

    /*@POST("Users/Register")
    Call<User> Register(@Field("username") String username, @Field("password") String password);*/

    @PUT("Articles/{article_id}/like")
    Call<ResponseBody> Like(@Header("x-authtoken") String token, @Path(value = "article_id", encoded = true) int articleId);

    @DELETE("Articles/{article_id}/like")
    Call<ResponseBody> UnLike(@Header("x-authtoken") String token, @Path(value = "article_id", encoded = true) int articleId);

}
