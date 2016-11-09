package au.com.slidenerdnavdrawer.staggeredgridlayout;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by DasunPC on 11/8/16.
 */

public class VerticalStaggeredGridFragment extends RecyclerFragment {

    public static VerticalStaggeredGridFragment newInstance(int position) {
        VerticalStaggeredGridFragment fragment = new VerticalStaggeredGridFragment();
        Bundle args = new Bundle();
        args.putString("position",String.valueOf(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new InsetDecoration(getActivity());
    }

    @Override
    protected int getDefaultItemCount() {
        return 15;
    }

    @Override
    protected SimpleAdapter getAdapter() {
        return new SimpleStaggeredAdapter(getActivity());
    }
}
