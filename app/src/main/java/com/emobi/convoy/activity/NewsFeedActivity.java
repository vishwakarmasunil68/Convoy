package com.emobi.convoy.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.PublicPost.PublicPostResponsePOJO;
import com.emobi.convoy.pojo.PublicPost.PublicPostUserResponsePOJO;
import com.emobi.convoy.pojo.PublicPost.publicPostPOJO;
import com.emobi.convoy.utility.Pref;
import com.emobi.convoy.utility.StringUtils;
import com.emobi.convoy.webservices.WebServiceBase;
import com.emobi.convoy.webservices.WebServicesCallBack;
import com.emobi.convoy.webservices.WebServicesUrls;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedActivity extends AppCompatActivity implements WebServicesCallBack{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_news_feed)
    RecyclerView rv_news_feed;
    @BindView(R.id.fab_add_post)
    FloatingActionButton fab_add_post;

    private final int POST_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News Feeds");
        callNewFeedApi();

//        Log.d(TAG,"user:-"+Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_ID,""));

        fab_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewsFeedActivity.this, NewPostActivity.class);
                startActivityForResult(i, POST_REQUEST);
            }
        });
    }

    public void callNewFeedApi(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID,"")));
        new WebServiceBase(nameValuePairs, this, "newsfeedapi").execute(WebServicesUrls.NEWS_FEED_API);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case "newsfeedapi":
                    parseNewsFeedData(response);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                if(result.equals("posted")){
                    callNewFeedApi();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private final String TAG=getClass().getName();
    public void parseNewsFeedData(String response){
        Log.d(TAG,"response:-"+response);
        try {
            Gson gson = new Gson();
            publicPostPOJO publicPostPOJOobj = gson.fromJson(response, publicPostPOJO.class);
            if (publicPostPOJOobj != null) {
                if (publicPostPOJOobj.getSuccess().equals("true")) {
                    if (publicPostPOJOobj.getList_news() != null && publicPostPOJOobj.getList_news().size() > 0) {
                        NewFeedAdapter adapter = new NewFeedAdapter(getApplicationContext(), publicPostPOJOobj.getList_news(), publicPostPOJOobj.getList_user_names());
                        LinearLayoutManager horizontalLayoutManagaer
                                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv_news_feed.setLayoutManager(horizontalLayoutManagaer);
                        rv_news_feed.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e){
            Log.d(TAG,e.toString());
            Toast.makeText(getApplicationContext(),"SOmething went wrong",Toast.LENGTH_LONG).show();
        }
    }

    public class NewFeedAdapter extends RecyclerView.Adapter<NewFeedAdapter.MyViewHolder> {

        private List<PublicPostResponsePOJO> post_response;
        private List<PublicPostUserResponsePOJO> log_response;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView iv_profile;
            public TextView tv_name;
            public TextView tv_type;
            public TextView tv_post;
            public ImageView iv_post_image;

            public MyViewHolder(View view) {
                super(view);
                iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_type = (TextView) view.findViewById(R.id.tv_type);
                tv_post = (TextView) view.findViewById(R.id.tv_post);
                iv_post_image = (ImageView) view.findViewById(R.id.iv_post_image);


            }
        }


        public NewFeedAdapter(Context context, List<PublicPostResponsePOJO> horizontalList,
                              List<PublicPostUserResponsePOJO> horizontalList1) {
            this.post_response = horizontalList;
            this.log_response = horizontalList1;
            this.context=context;
        }

        @Override
        public NewFeedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_news_feed, parent, false);

            return new NewFeedAdapter.MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final NewFeedAdapter.MyViewHolder holder, final int position) {
            holder.tv_name.setText(log_response.get(position).getLog_name());
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(holder.iv_profile);
            String image_url=WebServicesUrls.IMAGE_BASE_URL+post_response.get(position).getLog_pics();
            Log.d("sunil","image urls:-"+image_url);
            Picasso.with(context)
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.iv_profile);
            holder.tv_post.setText(post_response.get(position).getPost_msg());

            holder.tv_type.setText(post_response.get(position).getPost_cat_id());

            Picasso.with(context)
                    .load(WebServicesUrls.IMAGE_BASE_URL+post_response.get(position).getPost_image())
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.iv_post_image);
        }

        @Override
        public int getItemCount() {
            return log_response.size();
        }
    }
}
