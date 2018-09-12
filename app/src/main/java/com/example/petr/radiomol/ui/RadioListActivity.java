package com.example.petr.radiomol.ui;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.petr.radiomol.R;
import com.example.petr.radiomol.data.DataRadia;
import com.example.petr.radiomol.data.SaveLoad;
import com.example.petr.radiomol.data.XMLPullParserHandler;

import java.io.IOException;
import java.util.List;


public class RadioListActivity extends AppCompatActivity {

    SaveLoad saveLoad;


    ListView listView;
    @Override
    protected void onCreate (final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_list);

        // zakazani zmeny orientace obrazovky
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        listView = (ListView) findViewById(R.id.list);

        List<DataRadia> dataRadias = null;

        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            dataRadias = parser.parse(getAssets().open("list_cz.xml"));

            ArrayAdapter<DataRadia> adapter =
                    new ArrayAdapter<DataRadia>(this, R.layout.list_item, dataRadias);
            listView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<DataRadia> finalDataRadias = dataRadias;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String name = finalDataRadias.get(position).getStanice();
                String web = finalDataRadias.get(position).getWww();
                String url = finalDataRadias.get(position).getSpeed128();
                String facebook = finalDataRadias.get(position).getFacebook();
                String call = finalDataRadias.get(position).getCall();
                String car = finalDataRadias.get(position).getCar();


                saveLoad.saveRadioName(name);
                saveLoad.saveRadioWeb(web);
                saveLoad.saveRadioFacebook(facebook);
                saveLoad.saveRadioCall(call);
                saveLoad.saveRadioCar(car);
                saveLoad.saveRadioUrl(url);

                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }


    @Override
    public void onStart(){
        super.onStart();

        saveLoad = new SaveLoad(this);
    }




}









/*listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectRadioName = radiaName[position];
                selectRadioUrl = radiaUrl[position];

                Toast.makeText(getApplicationContext(),
                        ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

                SaveLoad saveLoad = new SaveLoad(getApplicationContext());
                saveLoad.saveRadioName(selectRadioName);
                saveLoad.saveRadioUrl(selectRadioUrl);

                finish();
            }
        });*/
