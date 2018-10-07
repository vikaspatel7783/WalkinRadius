package com.walkinradius.beacon.feasybeacon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.walkinradius.beacon.R;

import java.util.List;

public class FeasyScanBeaconsListAdapter extends RecyclerView.Adapter<FeasyScanBeaconsListAdapter.ViewHolder> {

    private final BeaconSelectListener mBeaconSelectListener;
    private List<BluetoothDeviceWrapper> mBeaconsInfo;

    public FeasyScanBeaconsListAdapter(List<BluetoothDeviceWrapper> beaconsInfo, BeaconSelectListener beaconSelectListener) {
        mBeaconsInfo = beaconsInfo;
        this.mBeaconSelectListener = beaconSelectListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewDeviceName;
        TextView mTextViewDeviceAddress;
        TextView mTextViewDeviceRssi;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextViewDeviceName = itemView.findViewById(R.id.device_name);
            mTextViewDeviceAddress = itemView.findViewById(R.id.device_address);
            mTextViewDeviceRssi = itemView.findViewById(R.id.device_rssi);
        }
    }

    @NonNull
    @Override
    public FeasyScanBeaconsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout beacon_view_row = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_scanned_feasy_beacon_list, parent, false);
        ViewHolder beacon_view_holder = new ViewHolder(beacon_view_row);
        return beacon_view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeasyScanBeaconsListAdapter.ViewHolder holder, final int position) {
        BluetoothDeviceWrapper beaconInfo = getBeacon(position);
        holder.mTextViewDeviceAddress.setText(beaconInfo.getAddress());
        holder.mTextViewDeviceName.setText(beaconInfo.getName());
        holder.mTextViewDeviceRssi.setText(""+beaconInfo.getRssi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeaconSelectListener.onBeaconSelected(getBeacon(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBeaconsInfo.size();
    }

    private BluetoothDeviceWrapper getBeacon(int position) {
        return mBeaconsInfo.get(position);
    }

    public interface BeaconSelectListener {
        void onBeaconSelected(BluetoothDeviceWrapper beaconInfo);
    }

}
