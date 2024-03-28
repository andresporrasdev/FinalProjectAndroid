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

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private List<LocationEntity> locations;
    private LayoutInflater inflater;

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

    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }

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

