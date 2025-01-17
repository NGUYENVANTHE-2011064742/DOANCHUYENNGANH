package com.sinhvien.orderdrinkapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinhvien.orderdrinkapp.R;

public class SplashActivity extends AppCompatActivity {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String FROM_SPLASH_KEY = "fromSplash";
    private static int SPLASH_TIMER = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        //tạo đối tượng view
        ImageView IMGLogo = (ImageView)findViewById(R.id.imgLogo);
        TextView TXTCoffeeshop = (TextView)findViewById(R.id.txtCoffeeshop);
        TextView TXTPowered = (TextView)findViewById(R.id.txtPowered);

        //lấy đối tượng animation
        Animation sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        Animation bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        //thiết lập animation cho component
        IMGLogo.setAnimation(sideAnim);
        TXTCoffeeshop.setAnimation(sideAnim);
        TXTPowered.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(FROM_SPLASH_KEY, true);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish(); //destroy activity khi back sẽ ko về splash
            }
        },SPLASH_TIMER);
    }
}