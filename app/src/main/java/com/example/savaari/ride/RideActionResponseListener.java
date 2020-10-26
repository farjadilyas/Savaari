package com.example.savaari.ride;

import android.content.Intent;

public interface RideActionResponseListener {
    void onDataLoaded(Intent intent);
    void onDriverFound(Intent intent);
    void onFindStatusReceived(Intent intent);
}
