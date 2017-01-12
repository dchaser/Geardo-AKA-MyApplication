package au.com.geardoaustralia.cartNew;


import android.support.v4.app.Fragment;

/**
 * Created by b_ashish on 17-Jan-16.
 */
public class CommonTabFragment extends Fragment {

    public interface TabListener<T> {

        public void onTabFragmentViewCreated(Fragment fragment);

        public void onTabFragmentAttached(Fragment fragment);

        public void onTabFragmentDetached(Fragment fragment);
    }
}
