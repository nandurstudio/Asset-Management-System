package com.ndu.assetmanagementsystem.sqlite.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndu.assetmanagementsystem.R;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelperV2;
import com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssetsAdapterV2 extends RecyclerView.Adapter<AssetsAdapterV2.AssetViewHolder> {

    private List<AssetV2> assetList;

    public static class AssetViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView asset_code;
        public TextView asset_rfid;
        public TextView asset_desc;
        public TextView asset_pic;
        public TextView asset_location;
        public TextView asset_status;
        //public TextView dot;
        public TextView timestamp;

        public AssetViewHolder(View view) {
            super(view);
            //dot = view.findViewById(R.id.dot);
            id = view.findViewById(R.id.assetId);
            asset_code = view.findViewById(R.id.assetCode);
            asset_rfid = view.findViewById(R.id.editText_assetRfid);
            asset_desc = view.findViewById(R.id.assetDesc);
            //asset_pic = view.findViewById(R.id.assetPic);
            //asset_location = view.findViewById(R.id.assetLocation);
            asset_status = view.findViewById(R.id.assetStatus);
            //timestamp = view.findViewById(R.id.timestamp);
        }
    }


    public AssetsAdapterV2(List<AssetV2> assetList) {
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
    public void onBindViewHolder(AssetViewHolder holder, final int position) {
        AssetV2 asset = assetList.get(position);
        String s = asset.getTxtAssetDescription();
        holder.id.setText(String.valueOf(position + 1));
        /*
        * Beberapa element di hidden
        * */

        //holder.id.setText(String.valueOf(asset.getId()));
        holder.asset_code.setText(asset.getTxtFixedAssetCode());
        holder.asset_rfid.setText(asset.getTxtRfid());
        if (s.length() <= 23) {
            holder.asset_desc.setText(s);
        } else {
            holder.asset_desc.setText(s.substring(0, Math.min(s.length(), 23)).concat("..."));
        }
        //holder.asset_pic.setText(asset.getAsset_pic());
        //holder.asset_location.setText(asset.getAsset_location());
        holder.asset_status.setText(asset.getTxtStatus());

        // Displaying dot from HTML character code
        //holder.dot.setText(Html.fromHtml("&#8226;"));

        // Formatting and displaying timestamp
        //holder.timestamp.setText(formatDate(asset.getTimestamp()));
        holder.itemView.setOnClickListener(v -> Log.d("TAG", "onClick: " + position));
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

    /*https://stackoverflow.com/questions/42363135/get-position-of-specific-cardview-in-recyclerview-without-clicking-scrolling*/
    public int getRfidPosition(String rfid) {
        for (int i = 0; i < assetList.size(); i++) {
            if (assetList.get(i).getTxtRfid().equals(rfid)) {
                return i;
            }
        }
        return 0;
    }

    public String getAssetDesc(int position) {
        return assetList.get(position).getTxtAssetDescription();
    }

    public String getAssetStatus(int position) {
        return assetList.get(position).getTxtStatus();
    }

    /*https://stackoverflow.com/a/37562572/7772358*/
    public void filter(String text, DatabaseHelperV2 db, String assetLocation) {
        assetList.clear();
        if (text.isEmpty()) {
            assetList.addAll(db.getAllAssetsByDept(assetLocation));
        } else {
            text = text.toLowerCase();
            try {
                for (AssetV2 asset : db.getAllAssetsByDept(assetLocation)) {
                    if (asset.getTxtFixedAssetCode().toLowerCase().contains(text) ||
                            asset.getTxtAssetDescription().toLowerCase().contains(text) ||
                            asset.getTxtAssetCategory().toLowerCase().contains(text) ||
                            asset.getTxtSupervisorID().toLowerCase().contains(text) ||
                            asset.getDecAcquisition().toLowerCase().contains(text) ||
                            asset.getTxtName().toLowerCase().contains(text) ||
                            asset.getTxtNick().toLowerCase().contains(text) ||
                            asset.getTxtEmail().toLowerCase().contains(text) ||
                            asset.getTxtPenggunaID().toLowerCase().contains(text) ||
                            asset.getTxtLokasiPengguna().toLowerCase().contains(text) ||
                            asset.getTxtLOBPengguna().toLowerCase().contains(text) ||
                            asset.getTxtArea().toLowerCase().contains(text) ||
                            asset.getTxtRfid().toLowerCase().contains(text) ||
                            asset.getTxtStatus().toLowerCase().contains(text) ||
                            asset.getTxtImgLink().toLowerCase().contains(text) ||
                            asset.getTxtNotes().toLowerCase().contains(text) ||
                            asset.getDtmTimestamp().toLowerCase().contains(text)) {
                        assetList.add(asset);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyDataSetChanged();
    }
}
