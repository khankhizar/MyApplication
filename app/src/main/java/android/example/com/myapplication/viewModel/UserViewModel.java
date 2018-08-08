package android.example.com.myapplication.viewModel;


import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.example.com.myapplication.ConnectivityReceiver;
import android.example.com.myapplication.R;
import android.example.com.myapplication.app.AppController;
import android.example.com.myapplication.model.Data;
import android.example.com.myapplication.model.Row;
import android.example.com.myapplication.network.ApiService;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class UserViewModel extends Observable {
    public ObservableInt progressBar;
    public ObservableInt userRecycler;
    public ObservableInt userLabel;
    public ObservableField<String> messageLabel;

    private List<Row> userList;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UserViewModel(@NonNull Context context) {
        this.context = context;
        this.userList = new ArrayList<>();
        progressBar = new ObservableInt(View.GONE);
        userRecycler = new ObservableInt(View.GONE);
        userLabel = new ObservableInt(View.VISIBLE);
        messageLabel = new ObservableField<>(context.getString(R.string.default_message_loading_users));
        fetchUsersList();
    }

    public void onClickFabToLoad(View view) {
        checkConnection();

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            initializeViews();
            fetchUsersList();
        } else {
            Toast.makeText(context, "Please check u internet connection", Toast.LENGTH_LONG).show();
        }
    }

    //It is "public" to show an example of test
    public void initializeViews() {
        userLabel.set(View.GONE);
        userRecycler.set(View.GONE);
        progressBar.set(View.VISIBLE);
    }

    private void fetchUsersList() {

        AppController appController = AppController.create(context);
        ApiService usersService = appController.getUserService();

        Disposable disposable = usersService.fetchUsers()
                .subscribeOn(appController.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Data>() {
                    @Override public void accept(Data userResponse) throws Exception {
                        updateUserDataList(userResponse.getRows());
                        progressBar.set(View.GONE);
                        userLabel.set(View.GONE);
                        userRecycler.set(View.VISIBLE);
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        messageLabel.set(context.getString(R.string.error_message_loading_users));
                        progressBar.set(View.GONE);
                        userLabel.set(View.VISIBLE);
                        userRecycler.set(View.GONE);
                    }
                });

        compositeDisposable.add(disposable);
    }

    private void updateUserDataList(List<Row> peoples) {
        userList.addAll(peoples);
        setChanged();
        notifyObservers();
    }

    public List<Row> getUserList() {
        return userList;
    }

    private void unSubscribeFromObservable() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public void reset() {
        unSubscribeFromObservable();
        compositeDisposable = null;
        context = null;
    }
}