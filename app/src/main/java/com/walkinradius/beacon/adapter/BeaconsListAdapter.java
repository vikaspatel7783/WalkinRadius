package com.walkinradius.beacon.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.networking.model.BeaconInfo;

import java.util.List;

public class BeaconsListAdapter extends RecyclerView.Adapter<BeaconsListAdapter.ViewHolder> {

    private List<BeaconInfo> mBeaconsInfo;

    public BeaconsListAdapter(List<BeaconInfo> beaconsInfo) {
        mBeaconsInfo = beaconsInfo;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewBeaconUUID;
        TextView mTextViewBeaconModel;
        TextView mTextViewBeaconTempName;
        TextView mTextViewBeaconTempLink;
        TextView mTextViewBeaconLocation;
        TextView mTextViewBeaconStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewBeaconUUID = itemView.findViewById(R.id.textView_uuid_value);
            mTextViewBeaconModel = itemView.findViewById(R.id.textView_model_value);
            mTextViewBeaconTempName = itemView.findViewById(R.id.textView_temp_name_value);
            mTextViewBeaconTempLink = itemView.findViewById(R.id.textView_temp_link_value);
            mTextViewBeaconLocation = itemView.findViewById(R.id.textView_location_value);
            mTextViewBeaconStatus = itemView.findViewById(R.id.textView_status_value);
        }
    }

    @NonNull
    @Override
    public BeaconsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout beacon_view_row = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_beacon_list, parent, false);
        ViewHolder beacon_view_holder = new ViewHolder(beacon_view_row);
        return beacon_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BeaconsListAdapter.ViewHolder holder, int position) {
        BeaconInfo beaconInfo = getBeacon(position);
        holder.mTextViewBeaconStatus.setText(beaconInfo.status);
        holder.mTextViewBeaconLocation.setText(beaconInfo.location);
        holder.mTextViewBeaconTempLink.setText(beaconInfo.temp_link);
        holder.mTextViewBeaconTempName.setText(beaconInfo.temp_name);
        holder.mTextViewBeaconModel.setText(beaconInfo.ibeacon_model_no);
        holder.mTextViewBeaconUUID.setText(beaconInfo.uuid_no);
    }

    @Override
    public int getItemCount() {
        return mBeaconsInfo.size();
    }

    private BeaconInfo getBeacon(int position) {
        return mBeaconsInfo.get(position);
    }
}
