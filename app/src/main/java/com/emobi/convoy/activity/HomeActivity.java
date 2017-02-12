package com.emobi.convoy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.emobi.convoy.R;
import com.emobi.convoy.pojo.friendlist.FriendListPOJO;
import com.emobi.convoy.pojo.friendlist.FriendListResponsePOJO;
import com.emobi.convoy.utility.GPSTracker;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback, LocationListener,WebServicesCallBack {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    ActionBarDrawerToggle drawerToggle;
    @BindView(R.id.nvView)
    NavigationView nvDrawer;
    @BindView(R.id.flContent)
    FrameLayout flContent;
    @BindView(R.id.ic_left_ham)
    ImageView ic_left_ham;

    @BindView(R.id.nvView_right)
    NavigationView nvView_right;
    @BindView(R.id.ic_right_ham)
    ImageView ic_right_ham;

    RecyclerView rv_friend_list;
    ActionBarDrawerToggle drawerToggle_right;
    GoogleMap mMap;
    GPSTracker gps;
    LinearLayout ll_news_feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        if (!isGooglePlayServicesAvailable()) {
//            finish();
//        }

        setUpLeftNavigationDrawer();
        setUpRightNavigationDrawer();
        setUpGoogleMap();

        ic_left_ham.setOnClickListener(this);
        ic_right_ham.setOnClickListener(this);
        checkForFcmToken();
        callFriendListAPI();
    }
    public void checkForFcmToken(){
//        if()
    }

    public void callFriendListAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, "friendlistapi").execute(WebServicesUrls.FRIEND_LIST_URL);

    }

    public void setUpGoogleMap(){
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setUpLeftNavigationDrawer() {
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

        nvDrawer.setItemIconTintList(null);
        setFirstItemNavigationView();
        drawerToggle.setDrawerIndicatorEnabled(false);
        setUpLeftHeaderLayout();
        setUpRightHeaderLayout();
    }

    private final String TAG=getClass().getName();
    public void setUpLeftHeaderLayout(){
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.inflate_home_left_header);

        CircleImageView ic_left_profile= (CircleImageView) headerLayout.findViewById(R.id.ic_left_profile);
        TextView tv_profile_name= (TextView) headerLayout.findViewById(R.id.tv_profile_name);
        String user_name=Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_NAME,"profile_name");
        Log.d(TAG,"user_name:-"+user_name);
        tv_profile_name.setText(user_name);
        Glide.with(this).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(ic_left_profile);
        ic_left_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });
    }

    public void setUpRightHeaderLayout(){
        View headerLayout = nvView_right.inflateHeaderView(R.layout.inflate_home_right_header);

        ImageView iv_search= (ImageView) headerLayout.findViewById(R.id.iv_search);
        EditText et_search= (EditText) headerLayout.findViewById(R.id.et_search);
        rv_friend_list= (RecyclerView) headerLayout.findViewById(R.id.rv_friend_list);
        ll_news_feeds= (LinearLayout) headerLayout.findViewById(R.id.ll_news_feeds);


        ll_news_feeds.setOnClickListener(this);
    }

    public void setUpRightNavigationDrawer() {
        setupRightDrawerContent(nvView_right);
        drawerToggle_right = setupDrawerToggleRight();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle_right);

        nvView_right.setItemIconTintList(null);
        setFirstRightItemNavigationView();
        drawerToggle_right.setDrawerIndicatorEnabled(false);
    }

    private void setFirstItemNavigationView() {
        nvDrawer.setCheckedItem(R.id.nav_whovisited);
        nvDrawer.getMenu().performIdentifierAction(R.id.nav_whovisited, 0);
    }
    private void setFirstRightItemNavigationView() {
        nvView_right.setCheckedItem(R.id.nav_whovisited);
        nvView_right.getMenu().performIdentifierAction(R.id.nav_whovisited, 0);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private ActionBarDrawerToggle setupDrawerToggleRight() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void setupRightDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectRightDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_whovisited:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_interest:
                break;
            case R.id.nav_diaries:
                break;
            case R.id.nav_in_app:
                break;
            case R.id.nav_testimonials:
                startActivity(new Intent(HomeActivity.this,TestimonialActivity.class));
                break;
            case R.id.nav_friend_request:
                break;
            case R.id.nav_logout:
                Pref.SetBooleanPref(getApplicationContext(),StringUtils.IS_LOGIN,false);
                startActivity(new Intent(this,SignupActivity.class));
                finishAffinity();
                break;
            case R.id.nav_notifications:
                break;

            default:
        }

        mDrawer.closeDrawers();
    }

    public void selectRightDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_whovisited:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_interest:
                break;
            case R.id.nav_diaries:
                break;
            case R.id.nav_in_app:
                break;
            case R.id.nav_testimonials:
                break;
            case R.id.nav_friend_request:
                break;
            case R.id.nav_logout:
                startActivity(new Intent(this,SignupActivity.class));
                finishAffinity();
                break;
            case R.id.nav_notifications:
                break;

            default:
        }

        mDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
        drawerToggle_right.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
        drawerToggle_right.onConfigurationChanged(newConfig);
    }

    public void SetTitleString(String title) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_left_ham:
                mDrawer.openDrawer(nvDrawer);
                break;
            case R.id.ic_right_ham:
                mDrawer.openDrawer(nvView_right);
                break;
            case R.id.ll_news_feeds:
                    startActivity(new Intent(this,NewsFeedActivity.class));
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        getLocation(mMap);
    }
    public void getLocation(GoogleMap map) {

        gps = new GPSTracker(HomeActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng latlng = new LatLng(latitude, longitude );
            String address=getAddress(latitude,longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) );
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
            Log.d("sun", latitude + "\n" + longitude);
        } else {
            gps.showSettingsAlert();
        }
    }
    public String getAddress(double latitude,double longitude){
        String address="";
//                    LocationAddress.getAddressFromLocation(latitude,longitude,LocationService.this,new GeocoderHandler());
        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Log.d("sunil",strReturnedAddress.toString());
                address=strReturnedAddress.toString();
                return address;
            }
            else{
                Log.d("sunil","No Address returned!");
                return "";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("sunil","Canont get Address!");
            return "Current Location";
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case "friendlistapi":
                parseFriendListResponse(response);
                break;
        }
    }
    public void parseFriendListResponse(String response){
        Log.d(TAG,response.toString());
        Gson gson=new Gson();
        FriendListPOJO friendListPOJO=gson.fromJson(response,FriendListPOJO.class);
        if(friendListPOJO!=null){
            if(friendListPOJO.getList_friends()!=null&&friendListPOJO.getList_friends().size()>0){
                FriendListAdapter adapter=new FriendListAdapter(getApplicationContext(),friendListPOJO.getList_friends());
                LinearLayoutManager horizontalLayoutManagaer
                        = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rv_friend_list.setLayoutManager(horizontalLayoutManagaer);
                rv_friend_list.setAdapter(adapter);
            }
        }
    }

    public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

        private List<FriendListResponsePOJO> horizontalList;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public CircleImageView iv_profile;
            public TextView tv_profile_name;
            public LinearLayout ll_friends;

            public MyViewHolder(View view) {
                super(view);
                iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
                tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
                ll_friends = (LinearLayout) view.findViewById(R.id.ll_friends);


            }
        }


        public FriendListAdapter(Context context, List<FriendListResponsePOJO> horizontalList) {
            this.horizontalList = horizontalList;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_friend_list, parent, false);

            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv_profile_name.setText(horizontalList.get(position).getLog_name());
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(holder.iv_profile);
                String image_url=WebServicesUrls.IMAGE_BASE_URL+horizontalList.get(position).getLog_pics();
                Log.d("sunil","image urls:-"+image_url);
            Picasso.with(context)
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.iv_profile);

            holder.ll_friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontalList.size();
        }
    }
}
