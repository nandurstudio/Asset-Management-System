package com.ndu.assetmanagementsystem.sqlite.view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ndu.assetmanagementsystem.R;
import com.ndu.assetmanagementsystem.sqlite.database.DatabaseHelper;
import com.ndu.assetmanagementsystem.sqlite.database.model.Asset;

import java.util.List;

public class AssetsAdapter extends RecyclerView.Adapter<AssetsAdapter.AssetViewHolder> {

    private final List<Asset> assetList;

    public static class AssetViewHolder extends RecyclerView.ViewHolder {
        public final TextView id;
        public final TextView fixed_asset_code;
        public final TextView nama_asset;
        public final TextView asset_rfid;
        public final TextView status;
        //public TextView dot;
        //public TextView timestamp;

        public AssetViewHolder(View view) {
            super(view);
            //dot = view.findViewById(R.id.dot);
            id = view.findViewById(R.id.assetId);
            fixed_asset_code = view.findViewById(R.id.assetCode);
            asset_rfid = view.findViewById(R.id.editText_assetRfid);
            nama_asset = view.findViewById(R.id.assetDesc);
            status = view.findViewById(R.id.assetStatus);
        }
    }


    public AssetsAdapter(List<Asset> assetList) {
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
        Asset asset = assetList.get(position);
        String s = asset.getTxtNamaAsset();
        holder.id.setText(String.valueOf(position + 1));
        /*
        * Beberapa element di hidden
        * */

        //holder.id.setText(String.valueOf(asset.getId()));
        holder.fixed_asset_code.setText(asset.getTxtFixedAssetCode());
        holder.asset_rfid.setText(asset.getTxtRfid());
        if (s.length() <= 23) {
            holder.nama_asset.setText(s);
        } else {
            holder.nama_asset.setText(s.substring(0, Math.min(s.length(), 23)).concat("..."));
        }
        //holder.asset_pic.setText(asset.getAsset_pic());
        //holder.asset_location.setText(asset.getAsset_location());
        holder.status.setText(asset.getTxtStatus());

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
/*    private String formatDate(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(Objects.requireNonNull(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }*/

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
        return assetList.get(position).getTxtNamaAsset();
    }

    public String getAssetStatus(int position) {
        return assetList.get(position).getTxtStatus();
    }

    /*https://stackoverflow.com/a/37562572/7772358*/
    public void filter(String text, DatabaseHelper db, String assetLocation) {
        assetList.clear();
        if (text.isEmpty()) {
            assetList.addAll(db.getAllAssetsByDept(assetLocation));
        } else {
            text = text.toLowerCase();
            try {
                for (Asset asset : db.getAllAssetsByDept(assetLocation)) {
                    if (
                            asset.getTxtFixedAssetCode().toLowerCase().contains(text) ||
                                    asset.getTxtNamaAsset().toLowerCase().contains(text) ||
                                    asset.getDtmTanggalBeli().toLowerCase().contains(text) ||
                                    asset.getTxtStatus().toLowerCase().contains(text) ||
                                    asset.getTxtDeptLob().toLowerCase().contains(text) ||
                                    asset.getTxtDeptLobUpdate().toLowerCase().contains(text) ||
                                    asset.getTxtLokasiAssetBySystem().toLowerCase().contains(text) ||
                                    asset.getTxtLokasiUpdate().toLowerCase().contains(text) ||
                                    asset.getTxtNamaPengguna().toLowerCase().contains(text) ||
                                    asset.getTxtNamaPenggunaUpdate().toLowerCase().contains(text) ||
                                    asset.getTxtNamaPenanggungJawab().toLowerCase().contains(text) ||
                                    asset.getTxtNamaPenanggungJawabUpdate().toLowerCase().contains(text) ||
                                    asset.getTxtKeterangan().toLowerCase().contains(text) ||
                                    asset.getTxtRfid().toLowerCase().contains(text) ||
                                    asset.getTxtImageLink().toLowerCase().contains(text) ||
                                    asset.getTxtAssetArea().toLowerCase().contains(text) ||
                                    asset.getTimestamp().toLowerCase().contains(text)) {
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
