package touchview.qiang.com.touchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.janson.touchView.TouchImageView;

import touchview.qiang.com.touchview.weight.DKDragView;

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


        //touchView.setPosition(false,10);



    }


}





