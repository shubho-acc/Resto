package acc.resto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class SecurityCode extends Activity {

    EditText code;
    Button submit;
    TextView msg;
    public static final String TABLE_NAME = "SecurityCode";
    public static final String COLUMN_NAME = "code";
    protected SharedPreferences preferences;
    protected SharedPreferences.Editor editor;
    protected static final String VISIT_COUNT = "visit_count";
    private int buttonNumber;

    protected Context mContext;

    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_code);

        mContext = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = preferences.edit();

        b = getIntent().getExtras();
        buttonNumber = b.getInt("button_number");

        code = (EditText) findViewById(R.id.code);
        code.setGravity(Gravity.CENTER);
        submit = (Button) findViewById(R.id.one);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sCode = code.getText().toString();
                ParseQuery<ParseObject> query = ParseQuery.getQuery(TABLE_NAME);
                query.getInBackground("nAa7IviZBl", new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String code1 = object.getString(COLUMN_NAME);

                            if (sCode.equalsIgnoreCase(code1)) {
                                editor.putInt(VISIT_COUNT, buttonNumber);
                                editor.commit();

                                Intent intent = new Intent(SecurityCode.this, PrizeActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_push,R.anim.right_push);
                                finish();

                            } else {
                                //RestoUI.showError();
                                Toast.makeText(getBaseContext(), "Wrong code !", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //RestoUI.connectionError();
                            Toast.makeText(getBaseContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_security_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
