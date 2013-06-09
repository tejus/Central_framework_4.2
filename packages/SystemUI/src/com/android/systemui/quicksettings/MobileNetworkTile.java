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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnLongClickListener;

import com.android.systemui.R;
import com.android.systemui.statusbar.phone.QuickSettingsController;
import com.android.systemui.statusbar.phone.QuickSettingsContainerView;
import com.android.systemui.statusbar.policy.NetworkController;
import com.android.systemui.statusbar.policy.NetworkController.NetworkSignalChangedCallback;
import android.content.BroadcastReceiver;

public class MobileNetworkTile extends QuickSettingsTile implements NetworkSignalChangedCallback{
    private NetworkController mController;
    private int mDataTypeIconId;
    private String dataContentDescription;
    private String signalContentDescription;
    private boolean wifiOn = false;
    public static QuickSettingsTile mInstance;

    public static QuickSettingsTile getInstance(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, final QuickSettingsController qsc, Handler handler, String id, BroadcastReceiver controller) {
        mInstance = null;
        mInstance = new MobileNetworkTile(context, inflater, container, qsc, (NetworkController) controller);
        return mInstance;
    }

    public MobileNetworkTile(Context context, LayoutInflater inflater,
            QuickSettingsContainerView container, QuickSettingsController qsc, NetworkController controller) {
        super(context, inflater, container, qsc);
        mController = controller;
        mTileLayout = R.layout.quick_settings_tile_rssi;

        mOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        };
        mOnLongClick = new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                startSettingsActivity(intent);
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
        wifiOn = enabled;

    }

    @Override
    public void onMobileDataSignalChanged(boolean enabled,
            int mobileSignalIconId, String mobileSignalContentDescriptionId,
            int dataTypeIconId, String dataTypeContentDescriptionId,
            String description) {
        if (deviceSupportsTelephony()) {
            // TODO: If view is in awaiting state, disable
            Resources r = mContext.getResources();
            mDrawable = enabled && (mobileSignalIconId > 0)
                    ? mobileSignalIconId
                    : R.drawable.ic_qs_signal_no_signal;
            signalContentDescription = enabled && (mobileSignalIconId > 0)
                    ? signalContentDescription
                    : r.getString(R.string.accessibility_no_signal);
            mDataTypeIconId = enabled && (dataTypeIconId > 0) && !wifiOn
                    ? dataTypeIconId
                    : 0;
            dataContentDescription = enabled && (dataTypeIconId > 0) && !wifiOn
                    ? dataContentDescription
                    : r.getString(R.string.accessibility_no_data);
            mLabel = enabled
                    ? removeTrailingPeriod(description)
                    : r.getString(R.string.quick_settings_rssi_emergency_only);
            updateQuickSettings();
        }
    }

    @Override
    public void onAirplaneModeChanged(boolean enabled) {
        // TODO Auto-generated method stub

    }

    boolean deviceSupportsTelephony() {
        PackageManager pm = mContext.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    @Override
    void updateQuickSettings() {
        TextView tv = (TextView) mTile.findViewById(R.id.rssi_textview);
        ImageView iv = (ImageView) mTile.findViewById(R.id.rssi_image);
        ImageView iov = (ImageView) mTile.findViewById(R.id.rssi_overlay_image);
        iv.setImageResource(mDrawable);
        if (mDataTypeIconId > 0) {
            iov.setImageResource(mDataTypeIconId);
        } else {
            iov.setImageDrawable(null);
        }
        tv.setText(mLabel);
        tv.setTextSize(1, mTileTextSize);
        if (mTileTextColor != -2) {
            tv.setTextColor(mTileTextColor);
        }
        mTile.setContentDescription(mContext.getResources().getString(
                R.string.accessibility_quick_settings_mobile,
                signalContentDescription, dataContentDescription,
                mLabel));
    }

 // Remove the period from the network name
    public static String removeTrailingPeriod(String string) {
        if (string == null) return null;
        final int length = string.length();
        if (string.endsWith(".")) {
            string.substring(0, length - 1);
        }
        return string;
    }

}
