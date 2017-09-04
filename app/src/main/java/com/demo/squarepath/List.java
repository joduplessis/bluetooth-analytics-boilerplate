package com.demo.squarepath;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class List extends BaseAdapter {

    Context context;
    ArrayList<HashMap<String,String>> data;

    public List(Context context, ArrayList<HashMap<String,String>> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list, null);

            HashMap<String,String> obj = data.get(position);

            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            ImageView unread = (ImageView) convertView.findViewById(R.id.unread);
            ImageView image = (ImageView) convertView.findViewById(R.id.icon);
            String upToNCharacters = obj.get("description").substring(0, Math.min(obj.get("description").length(),65)) + "...";
            title.setText(obj.get("title"));
            description.setText(upToNCharacters);

            // fonts

            Typeface fontLight = Typeface.createFromAsset(context.getAssets(), "HelveticaNeueLTStd-Lt_0.otf");
            Typeface fontBold = Typeface.createFromAsset(context.getAssets(), "HelveticaNeueLTStd-Bd_0.otf");

            // set the icons based on the icon hash detail

            if (obj.get("icon").equals("promotion"))
                image.setImageResource(R.drawable.listicon_promotion);

            if (obj.get("icon").equals("voucher"))
                image.setImageResource(R.drawable.listicon_voucher);

            if (obj.get("icon").equals("basket"))
                image.setImageResource(R.drawable.listicon_basket);

            if (obj.get("icon").equals("message"))
                image.setImageResource(R.drawable.listicon_message);

            // set whether this list item has been read

            if (obj.get("read").equals("yes")) {

                unread.setAlpha(0.0f);
                title.setTypeface(fontLight);
                description.setTypeface(fontLight);

            } else {

                unread.setAlpha(1.0f);
                title.setTypeface(fontBold);
                description.setTypeface(fontBold);

            }

        }

        return convertView;

    }

}