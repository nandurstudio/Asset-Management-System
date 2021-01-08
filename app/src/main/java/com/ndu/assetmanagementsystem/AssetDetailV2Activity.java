package com.ndu.assetmanagementsystem;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_DECACQUISITION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_DTMTIMESTAMP;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTAREA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTASSETCATEGORY;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTASSETDESCRIPTION;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTEMAIL;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTFIXEDASSETCODE;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTIMGLINK;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTLOBPENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTLOKASIPENGGUNA;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNAME;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNICK;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTNOTES;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTPENGGUNAID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTRFID;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTSTATUS;
import static com.ndu.assetmanagementsystem.sqlite.database.model.AssetV2.COLUMN_TXTSUPERVISORID;

public class AssetDetailV2Activity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_detail_v2);
        TextInputEditText assetCode = findViewById(R.id.textInput_assetCode);
        TextInputEditText assetDesc = findViewById(R.id.textInput_assetDesc);
        TextInputEditText assetCategory = findViewById(R.id.textInput_assetCategory);
        TextInputEditText assetSupervisorID = findViewById(R.id.textInput_assetSupervisorID);
        TextInputEditText assetAcquisition = findViewById(R.id.textInput_assetAcquisition);
        TextInputEditText assetPicName = findViewById(R.id.textInput_assetPicName);
        TextInputEditText assetPicNick = findViewById(R.id.textInput_assetPicNick);
        TextInputEditText assetPicEmail = findViewById(R.id.textInput_assetPicEmail);
        TextInputEditText assetUser = findViewById(R.id.textInput_assetUser);
        TextInputEditText assetLocation = findViewById(R.id.textInput_assetLocation);
        TextInputEditText assetLob = findViewById(R.id.textInput_assetLOB);
        TextInputEditText assetArea = findViewById(R.id.textInput_assetArea);
        TextInputEditText assetRfid = findViewById(R.id.textInput_assetRfid);
        TextInputEditText assetStatus = findViewById(R.id.textInput_assetStatus);
        TextInputEditText assetImgLink = findViewById(R.id.textInput_assetImageLink);
        TextInputEditText assetNotes = findViewById(R.id.textInput_assetNotes);
        TextView assetTimestamp = findViewById(R.id.textView_timestamp);

        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.asset_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        /*OnClick Handling*/
        toolbar.setNavigationOnClickListener(view -> finish());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AssetDetailV2Activity.this);

        String assetCodeSh = sharedPreferences.getString(COLUMN_TXTFIXEDASSETCODE, "");
        String assetDescSh = sharedPreferences.getString(COLUMN_TXTASSETDESCRIPTION, "");
        String assetCategorySh = sharedPreferences.getString(COLUMN_TXTASSETCATEGORY, "");
        String assetSupervisorIDSh = sharedPreferences.getString(COLUMN_TXTSUPERVISORID, "");
        int assetAcuisitionSh = sharedPreferences.getInt(COLUMN_DECACQUISITION, 0);
        String assetUserNameSh = sharedPreferences.getString(COLUMN_TXTNAME, "");
        String assetUserNickSh = sharedPreferences.getString(COLUMN_TXTNICK, "");
        String assetUserEmailSh = sharedPreferences.getString(COLUMN_TXTEMAIL, "");
        String assetUserSh = sharedPreferences.getString(COLUMN_TXTPENGGUNAID, "");
        String assetLocationSh = sharedPreferences.getString(COLUMN_TXTLOKASIPENGGUNA, "");
        String assetLOBPenggunaSh = sharedPreferences.getString(COLUMN_TXTLOBPENGGUNA, "");
        String assetAreaSh = sharedPreferences.getString(COLUMN_TXTAREA, "");
        String assetRfidSh = sharedPreferences.getString(COLUMN_TXTRFID, "");
        String assetStatusSh = sharedPreferences.getString(COLUMN_TXTSTATUS, "");
        String assetImageLinkSh = sharedPreferences.getString(COLUMN_TXTIMGLINK, "");
        String assetNotesSh = sharedPreferences.getString(COLUMN_TXTNOTES, "");
        String assetTimestampSh = sharedPreferences.getString(COLUMN_DTMTIMESTAMP, "");

        assetCode.setText(assetCodeSh);
        assetDesc.setText(assetDescSh);
        assetCategory.setText(assetCategorySh);
        assetSupervisorID.setText(assetSupervisorIDSh);
//        NumberFormat format = NumberFormat.getCurrencyInstance();
//        format.setMaximumFractionDigits(0);
//        format.setCurrency(Currency.getInstance("IDR"));
//        assetAcquisition.setText(format.format(assetAcuisitionSh));

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        assetAcquisition.setText(formatRupiah.format((double)assetAcuisitionSh));

        assetPicName.setText(assetUserNameSh);
        assetPicNick.setText(assetUserNickSh);
        assetPicEmail.setText(assetUserEmailSh);
        assetUser.setText(assetUserSh);
        assetLocation.setText(assetLocationSh);
        assetLob.setText(assetLOBPenggunaSh);
        assetArea.setText(assetAreaSh);
        assetRfid.setText(assetRfidSh);
        assetStatus.setText(assetStatusSh);
        assetImgLink.setText(assetImageLinkSh);
        assetNotes.setText(assetNotesSh);
        assetTimestamp.setText("Last sync: " + assetTimestampSh);

    }
}