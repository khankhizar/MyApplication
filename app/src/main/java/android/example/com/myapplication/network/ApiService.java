package android.example.com.myapplication.network;

import android.example.com.myapplication.model.Data;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {
    @GET("facts.json")
    /*Observable<Data> fetchUsers(@Url String url);*/
    Observable<Data> fetchUsers();

}