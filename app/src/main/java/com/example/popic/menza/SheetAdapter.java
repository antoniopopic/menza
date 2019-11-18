package com.example.popic.menza;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class SheetAdapter extends RecyclerView.Adapter<SheetAdapter.SheetAdapterViewHolder> {

    private SheetAdapterClickListener sheetAdapterClickListener;
    private List<Restaurant> restaurantList;

    public SheetAdapter() {

        this.restaurantList = new ArrayList<>();
    }

    public SheetAdapter(SheetAdapterClickListener sheetAdapterClickListener,
                        List<Restaurant> restaurantList) {

        this.sheetAdapterClickListener = sheetAdapterClickListener;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public SheetAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sheet_item, parent, false);
        return new SheetAdapterViewHolder(view, sheetAdapterClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SheetAdapterViewHolder holder, int position) {

        Restaurant restaurant = restaurantList.get(position);
        holder.bind(restaurant);

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }


    public class SheetAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_restName)
        TextView textView_restName;
        @BindView(R.id.textView_ratingMain)
        TextView textView_ratingMain;

        public SheetAdapterViewHolder(@NonNull View itemView, SheetAdapterClickListener sheetAdapterClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Restaurant restaurant) {
            textView_restName.setText(restaurant.getName());
            double votesSum = Double.parseDouble(restaurant.getVotesSum());
            double noVotes = Double.parseDouble(restaurant.getNoVotes());
            double rating = votesSum / noVotes;
            NumberFormat numberFormat = new DecimalFormat("#0.00");
            String stringRating = numberFormat.format(rating);
            textView_ratingMain.setText(stringRating);
        }

        @OnClick
        public void onItemClick() {
            sheetAdapterClickListener.onClick(restaurantList.get(getAdapterPosition()));
        }
    }
}
