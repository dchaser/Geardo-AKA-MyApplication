package au.com.geardoaustralia.cart;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.GlobalContext;

/**
 * Created by DasunPC on 12/5/16.
 */

public class ViewCartPopup extends DialogFragment implements SCClickListener {

    RelativeLayout rlContinueShopping;
    ImageButton ibCancel;
    TextView tvContinueShopping;

    TextView tvSetItemTotal;
    TextView tvSetEstdDelivery;
    TextView tvSetOrderTotal;

    Button btnConfirmCheckout;
    LinearLayout llTopSmallContent;

    List<ProductInfoModel> itemsInCart = new ArrayList<>();
    RecyclerView rvShopingCart;

    ShoppingCartAdapter adapter;
    Button b1;

    private BottomSheetBehavior bottomSheetBehavior;
    int flag = 0;


    public ViewCartPopup() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.YELLOW));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
    }

    public static ViewCartPopup newInstance(String title) {

        ViewCartPopup frag = new ViewCartPopup();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog, null);

        rlContinueShopping = (RelativeLayout) rootView.findViewById(R.id.rlContinueShopping);
        llTopSmallContent = (LinearLayout) rootView.findViewById(R.id.llTopSmallContent);

        rlContinueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        ibCancel = (ImageButton) rootView.findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        tvContinueShopping = (TextView) rootView.findViewById(R.id.tvContinueShopping);
        ibCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvSetItemTotal = (TextView) rootView.findViewById(R.id.tvItemTotal);
        tvSetEstdDelivery = (TextView) rootView.findViewById(R.id.tvSetEstdDelivery);
        tvSetOrderTotal = (TextView) rootView.findViewById(R.id.tvSetOrderTotal);

        rvShopingCart = (RecyclerView) rootView.findViewById(R.id.rvShopingCart);

        btnConfirmCheckout = (Button) rootView.findViewById(R.id.btnConfirmCheckout);

        b1 = (Button) rootView.findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (flag == 1) {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        View bsm = rootView.findViewById(R.id.bsmShoppingCart);
        bottomSheetBehavior = BottomSheetBehavior.from(bsm);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == bottomSheetBehavior.STATE_EXPANDED) {

                    b1.setText("Collapse");
                    flag = 1;
                } else if (newState == bottomSheetBehavior.STATE_HIDDEN) {

                    b1.setText("Expand");
                    flag = 0;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        return rootView;
    }

    public void setCartOpeningViews() {

        if (ShoppingCartHelper.getCartList().size() > 0) {

            llTopSmallContent.setVisibility(View.VISIBLE);
            rvShopingCart.setVisibility(View.VISIBLE);
            tvContinueShopping.setText("Continue Shopping");
            btnConfirmCheckout.setText("Checkout Now");

            //cart has items in it
            rvShopingCart.setHasFixedSize(true);
            adapter = new ViewCartPopup.ShoppingCartAdapter(ShoppingCartHelper.getCartList());
            adapter.setClickListener(this);
            adapter.setHasStableIds(true);
            rvShopingCart.setAdapter(adapter);
//            final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
            final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rvShopingCart.setLayoutManager(llm);

        } else {

            //no items in cart
            llTopSmallContent.setVisibility(View.GONE);
            rvShopingCart.setVisibility(View.GONE);
            tvContinueShopping.setText("Your cart is empty");
            btnConfirmCheckout.setText("Continue Shopping");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setCartOpeningViews();
    }


    @Override
    public void itemRemoved(View v, int position, Bundle batton) {

        adapter.removeItem(position);
        Toast.makeText(getActivity(), "itemRemove", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemEdit(View v, int position, Bundle batton) {
        Toast.makeText(getActivity(), "itemEdit", Toast.LENGTH_SHORT).show();
    }

    private static class ShoppingCartAdapter extends RecyclerView.Adapter<ViewCartPopup.ShoppingCartAdapter.SCVH> {


        private List<ProductInfoModel> productList = new ArrayList<>();
        private SCClickListener scClickListener;


        // Provide a suitable constructor (depends on the kind of dataset)
        ShoppingCartAdapter(List<ProductInfoModel> myDataset) {
            this.productList = myDataset;
        }

        @Override
        public ViewCartPopup.ShoppingCartAdapter.SCVH onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
            View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_cart_card, parent, false);
            ViewCartPopup.ShoppingCartAdapter.SCVH vh = new ViewCartPopup.ShoppingCartAdapter.SCVH(mview);
            return vh;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        public void removeItem(int position) {
            this.productList.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public void onBindViewHolder(ViewCartPopup.ShoppingCartAdapter.SCVH holder, int position) {

            holder.ivProductImage.setImageResource(productList.get(position).thumnailUrl);
            holder.tvProductTitle.setText(productList.get(position).Title);
            holder.tvProductSize.setText("45");
            holder.tvQuantity.setText("34");
            holder.tvShippingTotal.setText("$34.00");
            holder.tvShipDuration.setText("Dec 20 - Dec 27");
            //rbProduct
            holder.tvNoOfReviews.setText("45");
            holder.tvHowManyLeft.setText("5");
            holder.tvReducedPrice.setText("$65.00");
            holder.tvCurrentPrice.setText("$45.00");

            //Edit
            SpannableString editContent = new SpannableString("Edit");
            editContent.setSpan(new UnderlineSpan(), 0, editContent.length(), 0);
            holder.tvEditButton.setText(editContent);

            //Remove
            SpannableString removeContent = new SpannableString("Remove");
            removeContent.setSpan(new UnderlineSpan(), 0, removeContent.length(), 0);
            holder.tvRemoveButton.setText(removeContent);

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        void setClickListener(SCClickListener listenerOfClicks) {
            this.scClickListener = listenerOfClicks;
        }


        class SCVH extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView ivProductImage;
            TextView tvProductTitle;
            TextView tvProductSize;
            TextView tvQuantity;
            TextView tvShippingTotal;
            TextView tvShipDuration;
            RatingBar rbProduct;
            TextView tvNoOfReviews;
            TextView tvHowManyLeft;
            TextView tvReducedPrice;
            TextView tvCurrentPrice;
            TextView tvEditButton;
            TextView tvRemoveButton;



            public SCVH(View itemView) {
                super(itemView);


                ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
                tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
                tvProductSize = (TextView) itemView.findViewById(R.id.tvProductSize);
                tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
                tvShippingTotal = (TextView) itemView.findViewById(R.id.tvShippingTotal);
                tvShipDuration = (TextView) itemView.findViewById(R.id.tvShipDuration);
                rbProduct = (RatingBar) itemView.findViewById(R.id.rbProduct);
                tvNoOfReviews = (TextView) itemView.findViewById(R.id.tvNoOfReviews);
                tvHowManyLeft = (TextView) itemView.findViewById(R.id.tvHowManyLeft);
                tvReducedPrice = (TextView) itemView.findViewById(R.id.tvReducedPrice);
                tvCurrentPrice = (TextView) itemView.findViewById(R.id.tvCurrentPrice);
                tvEditButton = (TextView) itemView.findViewById(R.id.tvEditButton);
                tvEditButton.setOnClickListener(this);
                tvRemoveButton = (TextView) itemView.findViewById(R.id.tvRemoveButton);
                tvRemoveButton.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.tvEditButton) {
                    if (scClickListener != null) {

                        scClickListener.itemEdit(v, getAdapterPosition(), new Bundle());
                    }

                }


                if (v.getId() == R.id.tvRemoveButton) {
                    if (scClickListener != null) {

                        scClickListener.itemRemoved(v, getAdapterPosition(), new Bundle());
                    }

                }


            }


        }

    }


}
