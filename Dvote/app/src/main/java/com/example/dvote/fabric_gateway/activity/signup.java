package com.example.dvote.fabric_gateway.activity;

import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dvote.fabric_gateway.adapters.ImagePagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dvote.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.Arrays;
import java.util.List;

public class signup extends FragmentActivity {
    private EditText[] OTP;
    private Handler handler;
    private Runnable runnable;
    private int currentPage = 0;
    private long stored_pin;
    private static final int TIME_INTERVAL = 2000; // Time interval to detect double back press (2 seconds)
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long splashScreenStartTime = SystemClock.uptimeMillis();

        // Set a listener to keep the splash screen visible until the desired duration
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        // Set a listener to keep the splash screen visible until the desired duration
        splashScreen.setKeepOnScreenCondition(() -> {
            long elapsedTime = SystemClock.uptimeMillis() - splashScreenStartTime;
            return elapsedTime < 2500;
        });
        stored_pin = get_cached_pin();
        int layout = R.layout.signup;
        if (stored_pin != 0) {
            layout = R.layout.login;
        }
        setContentView(layout);

        MaterialTextView action_button = findViewById(R.id.btn);
        OTP = new EditText[]{findViewById(R.id.otp_edit_text_1), findViewById(R.id.otp_edit_text_2), findViewById(R.id.otp_edit_text_3), findViewById(R.id.otp_edit_text_4)};
        setUpOtpEditTexts();
        ViewPager2 viewPager = findViewById(R.id.pager);
        List<Integer> images = Arrays.asList(
                R.drawable.vote1,
                R.drawable.vote2,
                R.drawable.vote3,
                R.drawable.vote4,
                R.drawable.brand_text_foreground
        );

        action_button.setOnClickListener(v -> {
            if (stored_pin == 0 && get_user_input(OTP) != 0)// create a user
            {
                long pin = get_user_input(OTP);
                store_pin(pin,this);
                Toast.makeText(getBaseContext(), "User created", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(signup.this, signup.class);
                startActivity(i);
            } else {
                shakeEditText(OTP[0]);
                shakeEditText(OTP[1]);
                shakeEditText(OTP[2]);
                shakeEditText(OTP[3]);
            }
            if (stored_pin != 0 && get_user_input(OTP) != 0)// create a user
            {
                long pin = get_user_input(OTP);
                if (pin == get_cached_pin()) {
                    Toast.makeText(getBaseContext(), "LOGGED", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(signup.this, dashboard.class);
                    startActivity(i);
                } else {
                    shakeEditText(OTP[0]);
                    shakeEditText(OTP[1]);
                    shakeEditText(OTP[2]);
                    shakeEditText(OTP[3]);
                }
            }
        });

        final ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(this, images);
        viewPager.setAdapter(pagerAdapter);
        screen_slide_runner(pagerAdapter, viewPager);

    }

    private void screen_slide_runner(ImagePagerAdapter pagerAdapter, ViewPager2 viewPager) {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (pagerAdapter.getItemCount() == 0) return;
                currentPage = (currentPage + 1) % pagerAdapter.getItemCount();
                viewPager.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 3000); // Change page every 5 seconds
            }
        };
        handler.postDelayed(runnable, 3000); // Initial delay of 5 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Stop the runnable when the activity is destroyed
    }

    // TODO use on onBackPressDiapacher which is not deprecated
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    public void setNavigationBarColor(int color) {
        //int statusBarColor = ((ColorDrawable) CL.getBackground()).getColor();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
    }

    private void setUpOtpEditTexts() {
        for (int i = 0; i < OTP.length; i++) {
            final int index = i;
            OTP[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (charSequence.length() == 1) {
                        if (index < OTP.length - 1) {
                            OTP[index + 1].requestFocus();
                        }
                    } else if (charSequence.length() == 0) {
                        if (index > 0) {
                            OTP[index - 1].requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    private void shakeEditText(EditText editText) {
        // Calculate the desired translation distance
        int shakeDistance = 50;

        // Create the animation
        Animation shakeAnimation = new TranslateAnimation(0, shakeDistance, 0, 0);
        shakeAnimation.setDuration(100);
        shakeAnimation.setRepeatCount(7);
        shakeAnimation.setRepeatMode(Animation.REVERSE);

        // Apply the animation to the EditText
        /*int first_color = ContextCompat.getColor(this, R.color.white);
        int second_color = ContextCompat.getColor(this, R.color.red);
        editText.setBackgroundColor(second_color);
        editText.setBackgroundColor(first_color);*/
        editText.startAnimation(shakeAnimation);
    }

    private void store_pin(long pin, Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("pin", pin);
        editor.apply(); // Apply changes asynchronously
    }

    private long get_cached_pin() {
        // Get the SharedPreferences object
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        // Retrieve the stored PIN
        return sharedPreferences.getLong("pin", 0);
    }
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private long get_user_input(EditText[] editTexts) {
        String input;
        if (editTexts[0].getText().length() != 0 && editTexts[1].getText().length() != 0
                && editTexts[2].getText().length() != 0 && editTexts[3].getText().length() != 0) {
            input = editTexts[0].getText().toString()
                    + editTexts[1].getText().toString()
                    + editTexts[2].getText().toString()
                    + editTexts[3].getText().toString();
        }
        else {
            input = "0";
        }
        return Long.parseLong(input);
    }
}