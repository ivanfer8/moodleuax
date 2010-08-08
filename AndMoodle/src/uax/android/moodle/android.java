package uax.android.moodle;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class android extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hola mundo
        /*TextView tv = new TextView(this);
        tv.setText("hola, mundo!");
        setContentView(tv);*/
        
        //lista de elementos
        String[] countries = getResources().getStringArray(R.array.countries_array);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, countries));

        
        
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            // When clicked, show a toast with the TextView text
            Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
          }
        });
    }
    
    
    
}