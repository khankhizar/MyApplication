package android.example.com.myapplication.view.adapter;

import android.databinding.DataBindingUtil;
import android.example.com.myapplication.R;
import android.example.com.myapplication.databinding.ListItemBinding;
import android.example.com.myapplication.model.Row;
import android.example.com.myapplication.viewModel.ItemUserViewModel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

    private List<Row> userList;

    public UserAdapter() {this.userList = Collections.emptyList();}

    @Override
    public UserAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ListItemBinding itemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.list_item ,parent, false);
        return new UserAdapterViewHolder(itemUserBinding);
    }

    @Override
    public void onBindViewHolder(UserAdapterViewHolder holder, int position) {
        holder.bindUser(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return  userList.size();
    }

    public void setUserList(List<Row> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    public static class UserAdapterViewHolder extends RecyclerView.ViewHolder {

        ListItemBinding mItemUserBinding;

        public UserAdapterViewHolder(ListItemBinding itemUserBinding) {
            super(itemUserBinding.itemPeople);
            this.mItemUserBinding = itemUserBinding;
        }

        void bindUser(Row user){
            if(mItemUserBinding.getUserViewModel() == null){
                mItemUserBinding.setUserViewModel(new ItemUserViewModel(user, itemView.getContext()));
            }else {
                mItemUserBinding.getUserViewModel().setUser(user);
            }
        }
    }
}