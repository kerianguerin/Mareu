package com.openclassrooms.mareu.ui.meeting_list.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Place;
import com.openclassrooms.mareu.ui.meeting_list.filters.FilterListFragment;

import java.util.List;

public class PlaceFilterAdapter extends RecyclerView.Adapter<PlaceFilterAdapter.ViewHolder> {

    private final List<Place> mValues;
    private final FilterListFragment.OnPlaceFragmentInteractionListener mListener;
    private boolean isSelected;

    public PlaceFilterAdapter(List<Place> mValues, FilterListFragment.OnPlaceFragmentInteractionListener mListener) {
        this.mValues = mValues;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_filter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Place place = mValues.get(position);

        holder.fPlace = mValues.get(position);
        holder.fPlaceText.setText(mValues.get(position).getName());
        holder.fCheckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    if (holder.fCheckView.isChecked()) {
                        addPlace(mValues.get(position));
                        isSelected = true;
                    } else {
                        removePlace(mValues.get(position));
                        isSelected = false;
                    }
                }
            }

        });
        holder.fView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                if (isSelected) {
                    holder.fCheckView.setChecked(false);
                    removePlace(mValues.get(position));
                    isSelected = false;
                } else {
                    holder.fCheckView.setChecked(true);
                    addPlace(mValues.get(position));
                    isSelected = true;
                }
            }
        });
    }

    private void removePlace(Place place) {
        mListener.onPlaceFragmentInteraction(place, false);
    }

    private void addPlace(Place pPlace) {
        mListener.onPlaceFragmentInteraction(pPlace, true);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View fView;
        TextView fPlaceText;
        AppCompatCheckBox fCheckView;
        Place fPlace;

        ViewHolder(View view) {
            super(view);
            fView = view;
            fPlaceText = view.findViewById(R.id.place_name);
            fCheckView = view.findViewById(R.id.chbx_place);
        }


    }
}
