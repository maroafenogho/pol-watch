package com.maro.pol_watch.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressTask extends AsyncTask<Location, Void, String> {

    private final String TAG = FetchAddressTask.class.getSimpleName();
    private Context mContext;
    private OnTaskCompleted mListener;

    public FetchAddressTask(Context applicationContext, OnTaskCompleted listener) {
        this.mContext = applicationContext;
        this.mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Location location = locations[0];
        List<Address> addresses = null;
        String resultMessage = "";
        try{
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),1
            );
        } catch (IOException e) {
            e.printStackTrace();
            resultMessage = mContext.getString(Integer.parseInt("N"));
            Log.e(TAG, resultMessage, e);
        }

        if (addresses == null || addresses.size() == 0) {
            if (resultMessage.isEmpty()){
                resultMessage =  mContext.getString(Integer.parseInt("N"));
                Log.e(TAG, resultMessage);
            }
        }else {
            Address address = addresses.get(0);
            ArrayList<String> addressParts = new ArrayList<>();

            for (int i = 0; i == address.getMaxAddressLineIndex(); i++){
                addressParts.add(address.getAddressLine(i));
            }
            resultMessage = TextUtils.join("\n", addressParts);
            Log.i(TAG, "doInBackground: " + address);
        }
        return resultMessage;
    }

    @Override
    protected void onPostExecute(String address) {
        mListener.onTaskCompleted(address);
        super.onPostExecute(address);
    }

 public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
