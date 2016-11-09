package au.com.slidenerdnavdrawer.staggeredgridlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import au.com.slidenerdnavdrawer.GlobalContext;
import au.com.slidenerdnavdrawer.R;
import au.com.slidenerdnavdrawer.gallery.Image;

/**
 * Created by DasunPC on 11/8/16.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {

    private ArrayList<Image> mItems;
    private Context mContext;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public SimpleAdapter(Context context) {
        mItems = new ArrayList<>();
        mContext =  context;
    }

    /*
     * A common adapter modification or reset mechanism. As with ListAdapter,
     * calling notifyDataSetChanged() will trigger the RecyclerView to update
     * the view. However, this method will not trigger any of the RecyclerView
     * animation features.
     */
    public void setItemCount(int count) {
        mItems.clear();
        fetchImages();
    }

    private void fetchImages() {

//        pDialog.setMessage("please wait...");
//        pDialog.show();show

        final String endpoint = "http://www.delaroystudios.com/glide/json/glideimages.json";

        JsonArrayRequest req = new JsonArrayRequest(endpoint,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("SimpleAdapter line 67 ", response.toString());
                        //pDialog.hide();

                        mItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                mItems.add(image);

                            } catch (JSONException e) {
                                Log.e("tag", "Json parsing error: " + e.getMessage());
                            }
                        }

                        notifyDataSetChanged();
                        //mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error: " + error.getMessage());
                //pDialog.hide();
            }
        });

        // Adding request to request queue
        GlobalContext.getInstance().addToRequestQueue(req);
    }



    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_match_item, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Image item = mItems.get(position);

        Glide.with(mContext).load(item.getMedium())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemHolder.iv_logo_team_away);

        Glide.with(mContext).load(item.getMedium())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemHolder.iv_logo_team_home);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

//

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView iv_logo_team_away;
        private ImageView iv_logo_team_home;
        private SimpleAdapter mAdapter;

        public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            iv_logo_team_away = (ImageView) itemView.findViewById(R.id.iv_logo_team_home);
            iv_logo_team_home = (ImageView) itemView.findViewById(R.id.iv_logo_team_home);

        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setIv_logo_team_away(Bitmap bitmap) {
            iv_logo_team_away.setImageBitmap(bitmap);
        }

        public void setIv_logo_team_home(Bitmap bitmap) {
            iv_logo_team_home.setImageBitmap(bitmap);
        }
    }

}
