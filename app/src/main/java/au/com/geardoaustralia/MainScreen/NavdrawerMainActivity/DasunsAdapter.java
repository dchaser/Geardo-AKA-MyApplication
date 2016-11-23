package au.com.geardoaustralia.MainScreen.NavdrawerMainActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/2/16.
 */

public class DasunsAdapter extends RecyclerView.Adapter<DasunsAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    Context context;
    private ClickListener clickListener;

    public DasunsAdapter(Context context, List<Information> data){

        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    //where every row is initially created
    @Override
    public DasunsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.i("Dasun's Adapter","onCreateViewHolder");
        View view = inflater.inflate(R.layout.custom_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //re-binding and recycling logic per each scroll
    @Override
    public void onBindViewHolder(DasunsAdapter.MyViewHolder holder, final int position) {

     try{

         Log.i("Dasun's Adapter","onBindViewHolder "+position);
         Information current = data.get(position);

         holder.title.setText(current.title);
         //use the image downloader here with url if needed
         holder.icon.setImageResource(current.iconId);

         //you can declare handle click here but you should not keep a reference to the position outside of this method

         //or simply implement View.OnclickListener on VH itself & use getAdapterPosition() instead of keeping a reference

     }
     catch (Exception e){
         e.printStackTrace();
     }

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    public void deleteItem(int position){

        try{
            data.remove(position);
            //with recycerview no need to call NotifiyDatasetHasChanged()

            //NotifiyDatasetHasChanged() has to be called on two types to data
            //structural change to object structires
            //or item changes where single item's data is uodated

            notifyItemRemoved(position);

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //deleteItem(getAdapterPosition());

            if(clickListener != null){
                clickListener.itemClicket(v,getAdapterPosition());
            }

        }

    }

    public interface ClickListener{
        public void itemClicket(View view, int position);
    }
}
