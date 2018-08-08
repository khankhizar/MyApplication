package android.example.com.myapplication.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.example.com.myapplication.R;
import android.example.com.myapplication.model.Row;
import android.example.com.myapplication.network.ApiService;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class ItemUserViewModel extends BaseObservable {

    private Row user;
    private Context context;

    public ItemUserViewModel(Row user, Context context){
        this.user = user;
        this.context = context;
    }



    public Object getProfileThumb() {

            return user.getImageHref();
        }

    // Loading Image using Picasso Library.
    @BindingAdapter("imageUrl") public static void setImageUrl(ImageView imageView, String url) {


            if (url == null || url.isEmpty()|| url.length()==0){

                Picasso.with(imageView.getContext()).load(R.drawable.img_placeholder).into(imageView);
            }else

        {
                Picasso.with(imageView.getContext()).load(url).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder)
                        .into(imageView);
            }

    }


    public String getTitle() { return user.getTitle(); }

    public String getDescription() { return user.getDescription(); }


    public void onItemClick(View v){

    }

    public void setUser(Row user) {
        this.user = user;
        notifyChange();
    }
}