/*
	Copyright © 2022 Nathanne Isip

	Permission is hereby granted, free of charge,
	to any person obtaining a copy of this software
	and associated documentation files (the “Software”),
	to deal in the Software without restriction,
	including without limitation the rights to use, copy,
	modify, merge, publish, distribute, sublicense,
	and/or sell copies of the Software, and to permit
	persons to whom the Software is furnished to do so,
	subject to the following conditions:

	The above copyright notice and this permission notice
	shall be included in all copies or substantial portions
	of the Software.

	THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF
	ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
	TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
	PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
	THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
	DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
	CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
	IN THE SOFTWARE.
*/

package xyz.nathannestein.lanzaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.nathannestein.lanzaplicacion.R;

public class MainActivity extends Activity {
    private final ArrayList<Pair<String, String>> apps = new ArrayList<Pair<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) this.findViewById(R.id.launcher_date)).setText(new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(new Date()));
        this.loadApps();
    }

    private void loadApps() {
        final PackageManager manager = this.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        
        List<ResolveInfo> availableApps = manager.queryIntentActivities(i, 0);
        for(ResolveInfo info : availableApps)
            this.apps.add(new Pair<String, String>(info.loadLabel(manager).toString(), info.activityInfo.packageName));

        Collections.sort(this.apps, new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> pair1, Pair<String, String> pair2) {
                return pair1.first.compareToIgnoreCase(pair2.first);
            }
        });

        ArrayList<String> appLabels = new ArrayList<String>();
        for(Pair<String, String> pair : this.apps)
            appLabels.add(pair.first);

        ListView appList = (ListView) this.findViewById(R.id.app_list);
        appList.setAdapter(new ArrayAdapter<String>(this, R.layout.app_list_layout, R.id.app_label, appLabels.toArray(new String[] { })));
        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = manager.getLaunchIntentForPackage(MainActivity.this.apps.get(pos).second);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}