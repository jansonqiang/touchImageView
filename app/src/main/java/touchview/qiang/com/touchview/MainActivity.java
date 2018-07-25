package touchview.qiang.com.touchview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.janson.touchView.TouchImageView;

import java.lang.reflect.Field;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点我", Toast.LENGTH_SHORT).show();
            }
        });
        TouchImageView touchImageView = findViewById(R.id.imageView);

        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点我图片了", Toast.LENGTH_SHORT).show();
            }
        });


        // 获取屏幕宽高
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            statusBarHeight = getResources().getDimensionPixelSize(
                    Integer.parseInt(field.get(obj).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }


       int  mScreenHeight = getResources().getDisplayMetrics().heightPixels - statusBarHeight;

        touchImageView.setPosition(false,mScreenHeight/2);


        //touchView.setPosition(false,10);



    }


}





