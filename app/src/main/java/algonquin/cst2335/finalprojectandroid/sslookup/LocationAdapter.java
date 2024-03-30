/*
 * Purpose: Adapter class for managing the display of location entities in a RecyclerView within the Sunrise & Sunset Lookup App.
 * Author:
 * Lab Section: 022
 * Creation Date: Mar 26, 2024
 */
package algonquin.cst2335.finalprojectandroid.sslookup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalprojectandroid.R;

/**
 * Adapter class for managing the display of location entities in a RecyclerView. It handles the
 * creation and binding of view holders to display location data.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    /**
     * List of LocationEntity objects that the adapter uses to bind data to the views.
     */
    private List<LocationEntity> locations;

    /**
     * LayoutInflater used to inflate the layout for each item in the RecyclerView.
     */
    private LayoutInflater inflater;

    /**
     * Constructs a LocationAdapter with the specified context and list of locations.
     *
     * @param context   The context where the adapter is used.
     * @param locations The list of location entities to be displayed.
     */
    public LocationAdapter(Context context, List<LocationEntity> locations) {
        this.inflater = LayoutInflater.from(context);
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationEntity location = locations.get(position);
        holder.latitudeTextView.setText(String.valueOf(location.latitude));
        holder.longitudeTextView.setText(String.valueOf(location.longitude));
        holder.formattedLocationTextView.setText(location.formattedLocation);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(inflater.getContext(), LocationDetailsActivity.class);
            intent.putExtra("location_id", location.id);
            intent.putExtra("latitude", location.latitude);
            intent.putExtra("longitude", location.longitude);
            intent.putExtra("formattedLocation", location.formattedLocation);
            inflater.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return locations.size();
    }

    /**
     * Updates the list of locations and notifies the adapter to refresh the display.
     *
     * @param locations The new list of locations to display.
     */
    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class to represent each item in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView latitudeTextView;
        TextView longitudeTextView;
        TextView formattedLocationTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
            formattedLocationTextView = itemView.findViewById(R.id.locationTextView);

        }
    }
}

