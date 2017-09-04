package com.demo.squarepath;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Activity {

    ArrayList<HashMap<String,String>> maps = new ArrayList<HashMap<String,String>>();

    ListView list ;
    TextView welcome ;
    TextView unread ;
    TextView nomessages ;
    TextView line1 ;
    TextView line2 ;
    TextView line3 ;
    Button button ;
    LinearLayout popup ;

    boolean popupIsOpen = false ;
    boolean popupCallHasBeenMade = false ;

    int readCount = 0  ;

    // edit popup wording

    public void setPopup(String one, String two, String three) {

        Log.e("JOJOJO", "setPopup() is called ... "+popupIsOpen);

        if (popupIsOpen == false) {

            line1.setText(one);
            line2.setText(two);
            line3.setText(three);

            Log.e("JOJOJO", "setPopup() is made visible ...");

            popup.setVisibility(View.VISIBLE);

            popupIsOpen = true ;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // set the custom actionbar drawable
        // ----------------------------------------------------------

        ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.actionbar_replacement, null);

        final ActionBar actionBar = getActionBar();

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

        // find the UI
        // ----------------------------------------------------------

        list = (ListView) findViewById(R.id.listView);

        welcome = (TextView) findViewById(R.id.welcome);
        unread = (TextView) findViewById(R.id.unread);

        line1 = (TextView) findViewById(R.id.line1);
        line2 = (TextView) findViewById(R.id.line2);
        line3 = (TextView) findViewById(R.id.line3);

        nomessages = (TextView) findViewById(R.id.nomessages);

        button = (Button) findViewById(R.id.button);

        popup = (LinearLayout) findViewById(R.id.popup_panel) ;

        // make the popup invisible
        // ----------------------------------------------------------

        popup.setVisibility(View.INVISIBLE);

        // make the popup button cklickable

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                popup.setVisibility(View.INVISIBLE);
            }

        });

        // create the data
        // ----------------------------------------------------------

        // 1: promotion

        HashMap<String, String> hashMapPromotion = new HashMap<String, String>();

        hashMapPromotion.put("icon", "promotion");
        hashMapPromotion.put("title", "promotion");
        hashMapPromotion.put("description", "Take 2 for R350 & save R50 when you buy 2 chenille throws (140 x 180cm) for R199.99");
        hashMapPromotion.put("read", "no");

        readCount++;

        maps.add(hashMapPromotion);

        // 2: voucher

        HashMap<String, String> hashMapVoucher = new HashMap<String, String>();

        hashMapVoucher.put("icon", "voucher");
        hashMapVoucher.put("title", "voucher");
        hashMapVoucher.put("description", "Thanks for visiting our Gateway store for the 4th time this month! Take R100 off your next purchase, just ... because.");
        hashMapVoucher.put("read", "no");

        readCount++;

        maps.add(hashMapVoucher);

        // 3: abandoned basket

        HashMap<String, String> hashMapBasket = new HashMap<String, String>();

        hashMapBasket.put("icon", "basket");
        hashMapBasket.put("title", "abandoned basket");
        hashMapBasket.put("description", "We have your stock. You recently bagged pillows online and didn't check out - we have them in store if you want.");
        hashMapBasket.put("read", "no");

        readCount++;

        maps.add(hashMapBasket);

        // 4: alert

        HashMap<String, String> hashMapAlert = new HashMap<String, String>();

        hashMapAlert.put("icon", "message");
        hashMapAlert.put("title", "notification");
        hashMapAlert.put("description", "Sorry about the load shedding! We have generators in store so you can still purchase awesome homeware.");
        hashMapAlert.put("read", "no");

        readCount++;

        maps.add(hashMapAlert);

        // set the unread count

        unread.setText(readCount+ " unread messages");

        // display the tooltip if there are no messages

        if ( maps.size() != 0)
            nomessages.setVisibility(View.INVISIBLE);
        else
            nomessages.setVisibility(View.VISIBLE);

        // set the adaptor & make it clickable
        // ----------------------------------------------------------

        final List listAdaptor = new List(this, maps) ;

        list.setAdapter(listAdaptor);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // edit the popups

                switch(position) {
                    case 0:
                        line1.setText("take 2 for R350 & save");
                        line2.setText("R50");
                        line3.setText("when you buy 2 chenille throws (140 x 180cm) for R199.99");
                        break ;
                    case 1:
                        line1.setText("take bucks off");
                        line2.setText("R200");
                        line3.setText("Thanks for visiting our Gateway store for the 4th time this month!");
                        break ;
                    case 2:
                        line1.setText("we have stock");
                        line2.setText("hold it");
                        line3.setText("You recently bagged pillows online and didn't check out - we have them in store if you want.");
                        break ;
                    case 3:
                        line1.setText("ah, sadface");
                        line2.setText(":(");
                        line3.setText("Sorry about the load shedding! We have generators in store so you can still purchase awesome homeware.");
                        break ;

                }

                popup.setVisibility(View.VISIBLE);

                // edit the hashmap

                HashMap<String,String> currentMap = maps.get(position) ;

                // readcount

                if (currentMap.get("read").equals("no"))
                    readCount-- ;

                // assign it AFTER we deduct

                currentMap.put("read","yes");

                // update text

                unread.setText(readCount+ " unread messages");

                // re-assign the list

                list.setAdapter(listAdaptor);

            }

        });

        // fonts
        // ----------------------------------------------------------

        Typeface fontExtraLight = Typeface.createFromAsset(this.getAssets(), "HelveticaNeueLTStd-UltLt_0.otf");
        Typeface fontLight = Typeface.createFromAsset(this.getAssets(), "HelveticaNeueLTStd-Lt_0.otf");
        Typeface fontBold = Typeface.createFromAsset(this.getAssets(), "HelveticaNeueLTStd-Bd_0.otf");

        welcome.setTypeface(fontLight);
        unread.setTypeface(fontLight);
        line1.setTypeface(fontBold);
        line2.setTypeface(fontExtraLight);
        line3.setTypeface(fontLight);
        button.setTypeface(fontLight);
        nomessages.setTypeface(fontLight);

        // bluetooth stuff
        // ----------------------------------------------------------

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mHandler = new Handler();
        handler = new Handler();

        scheduleSendLocation() ;

    }

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 5000 ;
    private boolean mScanning;
    private Handler mHandler, handler;
    private static final long SCAN_PERIOD = 1000;
    private ArrayList<String> deviceList ;

    // scheule the scans

    public void scheduleSendLocation() {
        handler.postDelayed(new Runnable() {
            public void run() {
                scanLeDevice(true);
                handler.postDelayed(this, SCAN_PERIOD*2);
            }
        }, SCAN_PERIOD*2);
    }

    // scan device

    private void scanLeDevice(final boolean enable) {

        if (enable) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    aggregateDeviceList() ;

                }
            }, SCAN_PERIOD);

            mScanning = true;
            deviceList = new ArrayList<String>() ;
            mBluetoothAdapter.startLeScan(mLeScanCallback);

        } else {

            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            aggregateDeviceList() ;

        }

    }

    /**
     * bytesToHex method
     * Found on the internet
     * http://stackoverflow.com/a/9855338
     */

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Device scan callback.

    int strength ;

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            int startByte = 2;
                            boolean patternFound = false;
                            String uuid = "" ;

                            while (startByte <= 5) {
                                if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                                    patternFound = true;
                                    break;
                                }
                                startByte++;
                            }

                            if (patternFound) {

                                //Convert to hex String

                                byte[] uuidBytes = new byte[16];
                                System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
                                String hexString = bytesToHex(uuidBytes);

                                //Here is your UUID

                                uuid =  hexString.substring(0,8) + "-" +
                                        hexString.substring(8,12) + "-" +
                                        hexString.substring(12,16) + "-" +
                                        hexString.substring(16,20) + "-" +
                                        hexString.substring(20,32);

                                //Here is your Major value

                                int major = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);

                                //Here is your Minor value

                                int minor = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
                            }

                            String deviceCode = uuid ;

                            strength = rssi ;

                            if ( !deviceList.contains(deviceCode) )
                                deviceList.add(deviceCode) ;

                        }
                    });
                }
            };


    private void aggregateDeviceList() {

        Log.d("JOJOJO", deviceList.toString());

        String deviceToSearchFor = "B0702880-A295-A8AB-F734-031A98A512DE" ;

        if ( !deviceList.contains(deviceToSearchFor) || strength<-65) {

            // out of range

            welcome.setText("No stores available");
            list.setAlpha(0.0f);
            popupIsOpen = false ;
            popupCallHasBeenMade = false ;

        } else {

            // in range

            welcome.setText("Head office ("+strength+")");
            list.setAlpha(1.0f);

            // network calls stuff
            // ----------------------------------------------------------

            if (!popupCallHasBeenMade && !popupIsOpen) {
                new JsonParser().execute("http://joduplessis.com/massive/get.event.php?user=1");
                popupCallHasBeenMade = true;
            }

        }

    }

    // class functions

    @Override
    protected void onResume() {

        super.onResume();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            finish();
            return;
        }

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "No LE Support.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    JSONObject jObj ;
    OkHttpClient client = new OkHttpClient();

    public class JsonParser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() { }

        @Override
        protected String doInBackground(String... params) {

            try {

                Log.e("JOJOJO", " :: Getting URL ...");

                String url = params[0] ;

                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();

                String returnText = response.body().string();
                String name = new String(returnText.getBytes("ISO-8859-1"), "UTF-8");
                String decodedName = Html.fromHtml(name).toString();

                jObj = new JSONObject( decodedName );

            } catch (Exception e) {

                Log.e("JSON_OKHTTP", " :: " + e.toString());

            }

            return null;

        }

        @Override
        protected void onPostExecute(String json) {

            try {

                Log.e("JOJOJO", " :: Opening popup ...");

                line3.setText("PRESS OKAY TO GET A VOUCHER");
                line1.setText(jObj.get("heading").toString()) ;
                line2.setText(jObj.get("description").toString()) ;

                popup.setVisibility(View.VISIBLE);

                popupIsOpen = true ;

                // vibrate the popup

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                v.vibrate(500);

                // set the intent for when they click on the notification

                Intent resultIntent = new Intent(getApplicationContext(), Main.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                getApplicationContext(),
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                // create the notification

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.logo)
                                .setContentIntent(resultPendingIntent)
                                .setContentTitle(jObj.get("heading").toString())
                                .setContentText(jObj.get("description").toString());

                int mNotificationId = 001;

                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            } catch (Exception e) {

                Log.e("JSON_OKHTTP", " :: OnPostExsecute " + e.toString());

            }

        }

    }


}
