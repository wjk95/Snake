/* StartActivity.java
 * Created by Wojciech Krajewski
 * 2018
 */

package wjk95.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        ImageButton startButton = findViewById(R.id.startButton);
        ImageButton quitButton = findViewById(R.id.quitButton);

        startButton.setOnClickListener(
                new ImageButton.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                }

        );

        quitButton.setOnClickListener(

                new ImageButton.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                    finish();
                    System.exit(0);
                    }
                }


        );




    }
}
