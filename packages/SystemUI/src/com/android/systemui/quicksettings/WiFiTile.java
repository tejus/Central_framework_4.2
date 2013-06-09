/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.quicksettings;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.phone.QuickSettingsController;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NetworkController.NetworkSignalChangedCallback;
import android.content.BroadcastReceiver;

public class WiFiTile extends QuickSettingsTile implements NetworkSignalChangedCallback{
    private NetworkController mController;
    public static QuickSettingsTile mInstance;

    public static QuickSettingsTile getInstance(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, final QuickSettingsController qsc, Handler handler, String id, BroadcastReceiver controller) {
        mInstance = null;
        mInstance = new WiFiTile(context, inflater, container, qsc, (NetworkController) controller);
        return mInstance;
    }

    public WiFiTile(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, QuickSettingsController qsc, NetworkController controller) {
        super(context, inflater, container, qsc);
        mController = controller;

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WifiManager wfm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                wfm.setWifiEnabled(!wfm.isWifiEnabled());
            }
        };
        mOnLongClick = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                startSettingsActivity(android.provider.Settings.ACTION_WIFI_SETTINGS);
                return true;
            }
        };
    }

    @Override
    void onPostCreate() {
        mController.addNetworkSignalChangedCallback(this);
        super.onPostCreate();
    }

    @Override
    public void onDestroy() {
        mController.removeNetworkSignalChangedCallback(this);
        super.onDestroy();
    }

    @Override
    public void onWifiSignalChanged(boolean enabled, int wifiSignalIconId,
            String wifitSignalContentDescriptionId, String description) {
        boolean wifiConnected = enabled && (wifiSignalIconId > 0) && (description != null);
        boolean wifiNotConnected = (wifiSignalIconId > 0) && (description == null);
        if (wifiConnected) {
            mDrawable = wifiSignalIconId;
            mLabel = description.substring(1, description.length()-1);
        } else if (wifiNotConnected) {
            mDrawable = R.drawable.ic_qs_wifi_0;
            mLabel = mContext.getString(R.string.quick_settings_wifi_label);
        } else {
            mDrawable = R.drawable.ic_qs_wifi_no_network;
            mLabel = mContext.getString(R.string.quick_settings_wifi_off_label);
        }
        updateQuickSettings();
    }

    @Override
    public void onMobileDataSignalChanged(boolean enabled,
            int mobileSignalIconId, String mobileSignalContentDescriptionId,
            int dataTypeIconId, String dataTypeContentDescriptionId,
            String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAirplaneModeChanged(boolean enabled) {
        // TODO Auto-generated method stub

    }

}
