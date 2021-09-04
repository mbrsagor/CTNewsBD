package me.shagor.ctnewsbd;

/**
 * Created by shagor on 7/15/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Movie> movieItems;
    private int lastPosition = -1;
    ImageLoader imageLoader = null;

    public CustomListAdapter(Context context, List<Movie> movieItems) {
        this.context = context;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance(context).getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView postid = (TextView) convertView.findViewById(R.id.postid);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        // getting movie data for the row
        Movie m = movieItems.get(position);
        postid.setText(m.getId());

        imageLoader.get(m.getThumbnailUrl(), ImageLoader.getImageListener(thumbNail,
                R.drawable.error, R.drawable.error));
        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        thumbNail.setDefaultImageResId(R.drawable.error);
        thumbNail.setErrorImageResId(R.drawable.error);
        // title
        title.setText(m.getTitle());
        // release year
        date.setText(String.valueOf(m.getDate()));
        Animation animation = AnimationUtils.loadAnimation(convertView.getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

}