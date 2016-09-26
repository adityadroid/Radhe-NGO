package aditya.radhe;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bt;
    EditText nameEditText, amountEditText, customTextEditText, numberEditText;
    String message;
    String number;
    JSONParser jsonParser= new JSONParser();


    //Define Service URL HERE

    public final String SERVICE_URL = "";
    public  final String TAG_SUCCESS = "success";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = (Button)findViewById(R.id.sendSMSButton);
        nameEditText= (EditText)findViewById(R.id.userName);
        amountEditText = (EditText)findViewById(R.id.userAmount);
        customTextEditText= (EditText)findViewById(R.id.customText);
        numberEditText=(EditText)findViewById(R.id.userPhone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 number = numberEditText.getText().toString();
                String name =  nameEditText.getText().toString();
                String amount = amountEditText.getText().toString();
                if( !number.isEmpty()&&!name.isEmpty()&&!amount.isEmpty())
                {
                    if(customTextEditText.getText().toString().isEmpty())
                    {
                        message= "Jai Shree Krishna! Donation Successful:\n Name:"+name+"\nAmount"+amount;
                        new sendSMS().execute();
                    }
                    else
                    {

                        message= customTextEditText.getText().toString()+"\n Name:"+name+"\nAmount"+amount;
                        new sendSMS().execute();

                    }
                }
                else
                {
                    Snackbar.make(bt,"One or more fields empty",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }
    public  class sendSMS extends AsyncTask
    {
        boolean success = true;

        @Override
        protected void onPreExecute() {
            findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            boolean status=true;



            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("number",number));
            nameValuePair.add(new BasicNameValuePair("message",message));
            //  getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(SERVICE_URL,
                    "POST", nameValuePair,null,null);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                success = json.getBoolean(TAG_SUCCESS);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return status;

        }

        @Override
        protected void onPostExecute(Object o) {
            findViewById(R.id.login_progress).setVisibility(View.GONE);
            if(success)
            {
                Snackbar.make(bt,"Message Sent!",Snackbar.LENGTH_SHORT).show();
            }
            else

            {
                Snackbar.make(bt,"Message Failed!",Snackbar.LENGTH_SHORT).show();

            }
            super.onPostExecute(o);
        }
    }
}
