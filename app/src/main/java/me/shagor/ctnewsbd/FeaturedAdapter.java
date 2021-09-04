package me.shagor.ctnewsbd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
  Created by shagor on 7/23/2017.
 */

public class FeaturedAdapter  extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<FeathareConfig> featuarelsit;
    ImageLoader imageLoader1 = null;

    //TextView first_text,first_title,first_date;

    public FeaturedAdapter(Context context, List<FeathareConfig> featuareitems) {
        this.context = context;
        this.featuarelsit = featuareitems;
    }

    @Override
    public int getCount() {
        return featuarelsit.size();
    }

    @Override
    public Object getItem(int position) {
        return featuarelsit.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.featured_lv, null);
        if (imageLoader1 == null)
            imageLoader1 = AppController.getInstance(context).getImageLoader();
        NetworkImageView first_image = (NetworkImageView) convertView
                .findViewById(R.id.first_image);
        NetworkImageView feature_left_img = (NetworkImageView) convertView
                .findViewById(R.id.feature_left_img);
        NetworkImageView feature_right_img = (NetworkImageView) convertView
                .findViewById(R.id.feature_right_img);
        TextView first_text = (TextView) convertView.findViewById(R.id.first_text);
        TextView first_title = (TextView) convertView.findViewById(R.id.first_title);
        TextView first_date = (TextView) convertView.findViewById(R.id.first_date);
        TextView second_tilte = (TextView) convertView.findViewById(R.id.second_tilte);
        TextView second_date = (TextView) convertView.findViewById(R.id.second_date);
        TextView third_title = (TextView) convertView.findViewById(R.id.third_title);
        TextView third_date = (TextView) convertView.findViewById(R.id.third_date);
        TextView fourth_title = (TextView) convertView.findViewById(R.id.fourth_title);
        TextView fourth_date = (TextView) convertView.findViewById(R.id.fourth_date);


        FeathareConfig fconfig  = featuarelsit.get(position);
            first_text.setText(fconfig.getFirst_text());
            first_title.setText(fconfig.getFirst_title());
            first_date.setText(fconfig.getFirst_date());
            second_tilte.setText(fconfig.getSecond_tilte());
            second_date.setText(fconfig.getSecond_date());
            third_title.setText(fconfig.getThird_title());
            third_date.setText(fconfig.getThird_title());
            fourth_title.setText(fconfig.getFourth_title());
            fourth_date.setText(fconfig.getFourth_date());



        imageLoader1.get(fconfig.getThumbnailUrl(), ImageLoader.getImageListener(first_image,
                R.drawable.error, R.drawable.error));
        first_image.setImageUrl(fconfig.getThumbnailUrl(), imageLoader1);

        first_image.setDefaultImageResId(R.drawable.error);
        first_image.setErrorImageResId(R.drawable.error);


//            imageLoader1.get(fconfig.getThumbnails(), ImageLoader.getImageListener(first_image,
//                    R.drawable.error, R.drawable.error));
//            first_image.setImageUrl(fconfig.getThumbnails(), imageLoader1);
//            first_image.setDefaultImageResId(R.drawable.error);
//            first_image.setErrorImageResId(R.drawable.error);
//            imageLoader1.get(fconfig.getThumbnailUrl(), ImageLoader.getImageListener(feature_left_img,
//                    R.drawable.error, R.drawable.error));
//            feature_left_img.setImageUrl(fconfig.getThumbnailUrl(), imageLoader1);
//            feature_left_img.setDefaultImageResId(R.drawable.error);
//            feature_left_img.setErrorImageResId(R.drawable.error);
//            imageLoader1.get(fconfig.getThumbnailUrl(), ImageLoader.getImageListener(feature_right_img,
//                        R.drawable.error, R.drawable.error));
//            feature_right_img.setImageUrl(fconfig.getThumbnailUrl(), imageLoader1);
//            feature_right_img.setDefaultImageResId(R.drawable.error);
//            feature_right_img.setErrorImageResId(R.drawable.error);

           //first_image.setImageResource(fconfig.getThumbnails());

        return convertView;
    }
}
