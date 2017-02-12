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

import com.emobi.convoy.R;
import com.emobi.convoy.pojo.testimonial.TestimonialPOJO;
import com.emobi.convoy.pojo.testimonial.TestimonialResultPOJO;
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

public class TestimonialActivity extends AppCompatActivity implements WebServicesCallBack {
    private final static String CALL_TESTIMONIAL_API = "call_testimonial_api";
    private final static int TESTIMONIAL_RESPONSE = 100;
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_testimonial)
    RecyclerView rv_testimonial;
    @BindView(R.id.fab_add_testimonial)
    FloatingActionButton fab_add_testimonial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testimonial);
        ButterKnife.bind(this);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Testimonial");

        callTestimonialAPI();
        fab_add_testimonial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TestimonialActivity.this, NewPostActivity.class);
                startActivityForResult(i, TESTIMONIAL_RESPONSE);
            }
        });
    }

    public void callTestimonialAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tes_id", Pref.GetStringPref(getApplicationContext(), StringUtils.LOG_ID, "")));
        new WebServiceBase(nameValuePairs, this, CALL_TESTIMONIAL_API).execute(WebServicesUrls.TESTIMONIAL_API);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case CALL_TESTIMONIAL_API:
                parseTestimonialResponse(response);
                break;
        }
    }

    public void parseTestimonialResponse(String response) {
        Gson gson = new Gson();
        TestimonialPOJO pojo = gson.fromJson(response, TestimonialPOJO.class);
        if (pojo != null) {
            try {
                if (pojo.getSuccess().equals("true")) {
                    Log.d(TAG, "response:-" + pojo.getList_pojo().toString());
                    if(pojo.getList_pojo().size()>0){
                        TestimonialAdapter adapter = new TestimonialAdapter(getApplicationContext(),
                                pojo.getList_pojo());
                        LinearLayoutManager horizontalLayoutManagaer
                                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv_testimonial.setLayoutManager(horizontalLayoutManagaer);
                        rv_testimonial.setAdapter(adapter);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "error:-" + e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TESTIMONIAL_RESPONSE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public class TestimonialAdapter extends RecyclerView.Adapter<TestimonialAdapter.MyViewHolder> {

        private List<TestimonialResultPOJO> post_response;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public ImageView iv_profile;
            public TextView tv_msg;
            public TextView tv_profile_name;

            public MyViewHolder(View view) {
                super(view);
                iv_profile = (CircleImageView) view.findViewById(R.id.iv_profile);
                tv_msg = (TextView) view.findViewById(R.id.tv_msg);
                tv_profile_name = (TextView) view.findViewById(R.id.tv_profile_name);
            }
        }


        public TestimonialAdapter(Context context, List<TestimonialResultPOJO> horizontalList) {
            this.post_response = horizontalList;
            this.context = context;
        }

        @Override
        public TestimonialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.inflate_testimonial_response, parent, false);

            return new TestimonialAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TestimonialAdapter.MyViewHolder holder, final int position) {
            holder.tv_profile_name.setText(post_response.get(position).getLog_name());
//            Glide.with(context).load(Pref.GetStringPref(getApplicationContext(),StringUtils.LOG_PICS,"")).error(R.drawable.ic_profile).into(holder.iv_profile);
            String image_url = WebServicesUrls.IMAGE_BASE_URL + post_response.get(position).getLog_pics();
            Log.d("sunil", "image urls:-" + image_url);
            Picasso.with(context)
                    .load(image_url)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.iv_profile);
            holder.tv_msg.setText(post_response.get(position).getTes_msg());
        }

        @Override
        public int getItemCount() {
            return post_response.size();
        }
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
}
