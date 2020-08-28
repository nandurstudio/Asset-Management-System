package com.ndu.assetmanagementsystem.sqlite.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndu.assetmanagementsystem.R;
import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetViewHolder> {

    private Context context;
    private List<Asset> assetList;

    public class AssetViewHolder extends RecyclerView.ViewHolder {
        public TextView asset_code;
        public TextView asset_rfid;
        public TextView asset_desc;
        public TextView asset_pic;
        public TextView asset_location;
        public TextView asset_status;
        public TextView dot;
        public TextView timestamp;

        public AssetViewHolder(View view) {
            super(view);
            dot = view.findViewById(R.id.dot);
            asset_code = view.findViewById(R.id.assetCode);
            asset_rfid = view.findViewById(R.id.assetRfid);
            asset_desc = view.findViewById(R.id.assetDesc);
            asset_pic = view.findViewById(R.id.assetPic);
            asset_location = view.findViewById(R.id.assetLocation);
            asset_status = view.findViewById(R.id.assetStatus);
            timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public AssetsAdapter(Context context, List<Asset> assetList) {
        this.context = context;
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.asset_list_row, parent, false);

        return new AssetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssetViewHolder holder, int position) {
        Asset asset = assetList.get(position);

        holder.asset_code.setText(asset.getAsset_code());
        holder.asset_rfid.setText(asset.getAsset_rfid());
        holder.asset_desc.setText(asset.getAsset_desc());
        holder.asset_pic.setText(asset.getAsset_pic());
        holder.asset_location.setText(asset.getAsset_location());
        holder.asset_status.setText(asset.getAsset_status());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        holder.timestamp.setText(formatDate(asset.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
