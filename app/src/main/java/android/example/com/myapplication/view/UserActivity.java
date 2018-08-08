package android.example.com.myapplication.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.example.com.myapplication.ConnectivityReceiver;
import android.example.com.myapplication.R;
import android.example.com.myapplication.app.AppController;
import android.example.com.myapplication.databinding.ActivityUsersBinding;
import android.example.com.myapplication.model.Data;
import android.example.com.myapplication.model.Row;
import android.example.com.myapplication.view.adapter.UserAdapter;
import android.example.com.myapplication.viewModel.UserViewModel;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class UserActivity extends AppCompatActivity implements Observer, ConnectivityReceiver.ConnectivityReceiverListener {
    private ActivityUsersBinding userActivityBinding;
    private UserViewModel userViewModel;
    CoordinatorLayout coordinatorLayout;
    private Data user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkConnection();
        initDataBinding();
        setSupportActionBar(userActivityBinding.toolbar);
        getSupportActionBar().setTitle("About Canada");
        setUpListOfUsersView(userActivityBinding.listUser);
        setUpObserver(userViewModel);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {

        String message;
        int color;
        if (isConnected) {
            message = "";
            color = Color.TRANSPARENT;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
    }


    private void initDataBinding() {
        userActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        userViewModel = new UserViewModel(this);
        userActivityBinding.setUserViewModel(userViewModel);
    }

    // set up the list of user with recycler view
    private void setUpListOfUsersView(RecyclerView listUser) {
        UserAdapter userAdapter = new UserAdapter();
        listUser.setAdapter(userAdapter);
        listUser.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setUpObserver(Observable observable) {
        observable.addObserver(this);
    }


    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof  UserViewModel) {
            UserAdapter userAdapter = (UserAdapter) userActivityBinding.listUser.getAdapter();
            UserViewModel userViewModel = (UserViewModel) o;
            userAdapter.setUserList(userViewModel.getUserList());
            Log.d("data:",userViewModel.getUserList().toString());
        }
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }




    @Override protected void onDestroy() {
        super.onDestroy();
        userViewModel.reset();
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}