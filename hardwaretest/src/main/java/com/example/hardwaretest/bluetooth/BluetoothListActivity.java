package com.example.hardwaretest.bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hardwaretest.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothListActivity extends ListActivity
{
    private BluetoothAdapter mBTAdapter = null;
    private ArrayAdapter<String> mAdapter = null;

    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mAdapter.add(device.getName() + "\n" + device.getAddress());
                mAdapter.notifyDataSetChanged();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                Toast.makeText(context,
                        "블루투스 검색이 완료되었습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(
                BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver, filter);

        filter = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBTReceiver, filter);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices =
                mBTAdapter.getBondedDevices();

        ArrayList<String> data = new ArrayList<String>();
        data.add("[Bonded Devices]");
        if (pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                data.add(device.getName() + "\n" +
                        device.getAddress());
            }
        }

        data.add("[Scaned Devices]");
        mAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_btlist,
                R.id.label,
                data);
        setListAdapter(mAdapter);

        doDiscovery();
    }

    @Override
    protected void onListItemClick(ListView l, View v,
                                   int aPosition, long id)
    {
        String item = (String)
                getListAdapter().getItem(aPosition);
        Toast.makeText(this,  item + " selected",
                Toast.LENGTH_SHORT).show();

        Intent i = new Intent();
        i.putExtra("BTINFO", item);
        setResult(RESULT_OK, i);
        finish();
    }

    private void doDiscovery()
    {
        if (mBTAdapter.isDiscovering())
        {
            mBTAdapter.cancelDiscovery();
        }

        mBTAdapter.startDiscovery();
    }
}


