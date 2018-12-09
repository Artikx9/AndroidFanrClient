package com.example.artik.fanrclient.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artik.fanrclient.other.DBHelper;
import com.example.artik.fanrclient.Model.Recipe;
import com.example.artik.fanrclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by artik on 04.05.2018.
 */

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private List<Recipe> values;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    public RecipeAdapter(Context context, List<Recipe> values) {
        super(context, R.layout.list_item_recipe, values);

        this.context = context;
        this.values = values;
        sqlHelper = new DBHelper(context);
        db = sqlHelper.getWritableDatabase();
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
        ImageView imageView = row.findViewById(R.id.image_unread);
        Recipe item = values.get(position);
        name.setText(item.getName());
        content.setText(item.getContents());
        date.setText(new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date(item.getDate())));
        Cursor cursor = db.rawQuery("select * from "+ DBHelper.TABLE
                +" where id = "+ item.getId(), null);
        if(cursor.getCount() == 1)
        {
            cursor.moveToFirst();
            if(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_VERSION)) != item.getVersion())
               imageView.setVisibility(View.VISIBLE);
        }
        return row;
    }
}
