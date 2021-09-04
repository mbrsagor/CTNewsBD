package me.shagor.ctnewsbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nineoldandroids.animation.ObjectAnimator;

public class SplashScreen extends AppCompatActivity {
    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.anim_down);
        ImageView img =(ImageView)findViewById(R.id.spalsh_image);
        img.setAnimation(anim1);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(4000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        splashScreen();

    }

    public void splashScreen() {
        Thread ctThread = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in,R.anim.slide_splash);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ctThread.start();
    }
}
