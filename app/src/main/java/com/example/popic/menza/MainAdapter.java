package com.example.popic.menza;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainAdapterViewHolder> {

    private MainAdapterClickListener mainAdapterClickListener;
    private List<City> cityList;


    public MainAdapter() {

        cityList = new ArrayList<>();
    }


    public MainAdapter(MainAdapterClickListener mainAdapterClickListener, List<City> cityList) {

        this.mainAdapterClickListener = mainAdapterClickListener;
        this.cityList = cityList;

    }

    @NonNull
    @Override
    public MainAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MainAdapterViewHolder(view, mainAdapterClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterViewHolder holder, int position) {

        City city = cityList.get(position);
        holder.bind(city);

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }


    public class MainAdapterViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.textView_cityName)
        TextView textView_cityName;


        public MainAdapterViewHolder(@NonNull View itemView, MainAdapterClickListener mainAdapterClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(City city) {

            textView_cityName.setText(city.getCityName());
        }

        @OnClick
        public void onItemClick() {
            mainAdapterClickListener.onClick(cityList.get(getAdapterPosition()));
        }

    }
}
