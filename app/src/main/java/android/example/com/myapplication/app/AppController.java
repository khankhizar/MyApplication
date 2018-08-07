package android.example.com.myapplication.app;

import android.app.Application;
import android.content.Context;
import android.example.com.myapplication.ConnectivityReceiver;
import android.example.com.myapplication.network.ApiFactory;
import android.example.com.myapplication.network.ApiService;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class AppController extends Application {
    private ApiService usersService;
    private Scheduler scheduler;
    private static AppController mInstance;

    private static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    public static AppController create(Context context) {
        return AppController.get(context);
    }

    public ApiService getUserService() {
        if (usersService == null) {
            usersService = ApiFactory.create();
        }

        return usersService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public void setUserService(ApiService usersService) {
        this.usersService = usersService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


}