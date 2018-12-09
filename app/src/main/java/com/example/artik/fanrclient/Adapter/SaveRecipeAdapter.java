package com.example.artik.fanrclient.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artik.fanrclient.other.FanrClient;
import com.example.artik.fanrclient.other.Helper;
import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class SaveRecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> values;
    Retrofit builder;
    public SaveRecipeAdapter(Context context, List<Recipe> values) {
        super(context, R.layout.list_item_recipe, values);

        this.context = context;
        this.values = values;

            builder = new Retrofit.Builder()
                    .baseUrl(Helper.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;


        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_recipe, parent, false);
        }

        TextView name = row.findViewById(R.id.RecipeName);
        TextView content = row.findViewById(R.id.RecipeContents);
        TextView date = row.findViewById(R.id.RecipeDate);
        final ImageView imageView = row.findViewById(R.id.image_unread);
        final Recipe item = values.get(position);
        name.setText(item.getName());
        content.setText(item.getContents());
        date.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date(item.getDate())));
        FanrClient client = builder.create(FanrClient.class);
        Call<Integer> call = client.findVersion(new Helper(context).getToken(), item.getId());
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer ver = response.body();
                if(ver != null)
                    if(ver  != item.getVersion())
                        imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });



        return row;
    }

    private Integer getRecipe(Context context, int id)
    {
        final int[] version = new int[1];

        return version[0];
    }
}
