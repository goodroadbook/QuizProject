package com.example.hardwaretest.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hardwaretest.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothMainActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback
{
    private static final String BTNAME = "BTNAME";

    private final int BT_STATE_SUCCESS = 0;
    private final int BT_STATE_FAILURE = -1;
    private final int BT_STATE_NOTFOUND = -2;
    private final int BT_STATE_DISABLED = -3;

    private final int RES_CODE_BT_ENABLE = 1000;
    private final int RES_CODE_BT_LIST = 1001;

    private final int HANDLE_SEND_MSG = 0;
    private final int HANDLE_CONN_NAME = 1;

    private final UUID BT_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private String mBTAddress = null;
    private BTAcceptThread mBTAcceptThread = null;
    private BTConnectThread mBTConnectThread = null;
    private BTMessageControlThread mBTMessageControlThread =
            null;
    private BluetoothAdapter mBTAdapter = null;
    private Handler mHandler = null;
    private int mBTState = BT_STATE_NOTFOUND;

    class BTAcceptThread extends Thread
    {
        private final BluetoothServerSocket mmServerSocket;
        public BTAcceptThread()
        {
            BluetoothServerSocket tmp = null;
            try
            {
                tmp = mBTAdapter.listenUsingRfcommWithServiceRecord(BTNAME, BT_UUID);
            }
            catch (IOException e)
            {
                ;
            }
            mmServerSocket = tmp;
        }

        public void run()
        {
            BluetoothSocket socket = null;

            int state = 0;
            while (0 == state)
            {
                try
                {
                    socket = mmServerSocket.accept();
                    Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
                    for(BluetoothDevice device : pairedDevices)
                    {
                        mHandler.obtainMessage(HANDLE_CONN_NAME, device.getName() + "\n" + device.getAddress()
                        ).sendToTarget();
                    }
                }
                catch (IOException e)
                {
                    break;
                }

                if (socket != null)
                {
                    createMessageControl(socket,
                            socket.getRemoteDevice());
                    state = 1;
                }
            }
        }

        public void cancel()
        {
            try
            {
                mmServerSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    class BTMessageControlThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public BTMessageControlThread(BluetoothSocket socket)
        {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try
            {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            }
            catch(IOException e)
            {
                ;
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            while (true)
            {
                try
                {
                    int count = mmInStream.read(buffer);
                    if(count >= 0)
                    {
                        String data = new String(buffer, "UTF-8");
                        mHandler.obtainMessage(HANDLE_SEND_MSG,
                                (String)data.trim()).sendToTarget();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void write(byte[] buffer)
        {
            try
            {
                mmOutStream.write(buffer);
            }
            catch (IOException e)
            {
                ;
            }
        }

        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            {
                ;
            }
        }
    }

    class BTConnectThread extends Thread
    {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public BTConnectThread(BluetoothDevice device)
        {
            mmDevice = device;
            BluetoothSocket tmp = null;

            try
            {
                tmp = device.createRfcommSocketToServiceRecord(BT_UUID);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }

        @Override
        public void run()
        {
            try
            {
                mBTAdapter.cancelDiscovery();
                mmSocket.connect();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            createMessageControl(mmSocket, mmDevice);
        }

        public void cancel()
        {
            try
            {
                mmSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = new Handler(this);

        TextView btstatetxt = (TextView) this.findViewById(R.id.btstatetxt);
        mBTState = checkBlueTooth();
        switch (mBTState)
        {
            case BT_STATE_SUCCESS:
                btstatetxt.setText(
                        "블루투스를 사용할 수 있습니다.");
                break;
            case BT_STATE_FAILURE:
                btstatetxt.setText(
                        "블루투스를 사용할 권한이 없습니다.");
                break;
            case BT_STATE_NOTFOUND:
                btstatetxt.setText(
                        "단말에서 블루투스를 지원하지 않습니다.");
                break;
            case BT_STATE_DISABLED:
                btstatetxt.setText(
                        "블루투스가 비활성화되어 있습니다.");
                enableBlueTooth();
                break;
            default:
                break;
        }

        Button scanBt = (Button)findViewById(R.id.scanbt);
        scanBt.setOnClickListener(this);

        Button conBt = (Button)findViewById(R.id.conbt);
        conBt.setOnClickListener(this);

        Button sendmsg = (Button)findViewById(R.id.sendmsg);
        sendmsg.setOnClickListener(this);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(BT_STATE_SUCCESS == mBTState)
        {
            start();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (mBTAcceptThread != null)
        {
            mBTAcceptThread.cancel();
            mBTAcceptThread = null;
        }

        if (mBTConnectThread != null)
        {
            mBTConnectThread.cancel();
            mBTConnectThread = null;
        }

        if (mBTMessageControlThread != null)
        {
            mBTMessageControlThread.cancel();
            mBTMessageControlThread = null;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.scanbt:
                startScanBluetooth();
                break;
            case R.id.conbt:
                startConnectBluetooth();
                break;
            case R.id.sendmsg:
                EditText btmsg = (EditText) findViewById(R.id.editmsg);
                String msg = btmsg.getText().toString();
                mBTMessageControlThread.write(msg.getBytes());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        switch(msg.what)
        {
            case HANDLE_SEND_MSG:
                TextView btmsg = (TextView)
                        findViewById(R.id.btmsg);
                btmsg.setText((String)msg.obj);
                break;
            case HANDLE_CONN_NAME:
                TextView btInfotxt = (TextView)
                        findViewById(R.id.btdevice);
                btInfotxt.setText((String)msg.obj);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {
        switch (requestCode)
        {
            case RES_CODE_BT_LIST:
                if (null == data)
                {
                    return;
                }
                String btInfo = data.getStringExtra("BTINFO");
                TextView btInfotxt = (TextView)
                        findViewById(R.id.btdevice);
                btInfotxt.setText(btInfo);

                String[] btInfoStr = btInfo.split("\\n");
                mBTAddress = btInfoStr[1];

                break;
            case RES_CODE_BT_ENABLE:
                TextView btstatetxt = (TextView)
                        findViewById(R.id.btstatetxt);
                if (resultCode == Activity.RESULT_OK)
                {
                    startScanBluetooth();
                    btstatetxt.setText("블루투스를 사용할 수 있습니다.");
                }
                else
                {
                    btstatetxt.setText("블루투스가 비활성화되어 있습니다.");
                }
                break;
            default:
                break;
        }
    }

    private void startConnectBluetooth()
    {
        BluetoothDevice device = mBTAdapter.getRemoteDevice(mBTAddress);
        connect(device);
    }

    private void startScanBluetooth()
    {
        Intent i = new Intent(this, BluetoothListActivity.class);
        startActivityForResult(i, RES_CODE_BT_LIST);
    }

    private void enableBlueTooth()
    {
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, RES_CODE_BT_ENABLE);
    }

    private int checkBlueTooth()
    {
        try
        {
            if (null == mBTAdapter)
            {
                return BT_STATE_NOTFOUND;
            }

            if (false == mBTAdapter.isEnabled())
            {
                return BT_STATE_DISABLED;
            }
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
            return BT_STATE_FAILURE;
        }
        return BT_STATE_SUCCESS;
    }

    public synchronized void start()
    {
        mBTAcceptThread = new BTAcceptThread();
        mBTAcceptThread.start();
    }

    public synchronized void connect(BluetoothDevice device)
    {
        mBTConnectThread = new BTConnectThread(device);
        mBTConnectThread.start();
    }

    private void createMessageControl(BluetoothSocket socket,
                                      BluetoothDevice device)
    {
        mBTMessageControlThread = new
                BTMessageControlThread(socket);
        mBTMessageControlThread.start();
    }
}