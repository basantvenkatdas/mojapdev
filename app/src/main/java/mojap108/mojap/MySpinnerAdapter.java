package mojap108.mojap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by gollaba on 9/14/16.
 */
public class MySpinnerAdapter extends BaseAdapter {
    private final Context mContext;
    String[] mantra_array = null;
    public MySpinnerAdapter(String[] array, Context context){
        mantra_array = array;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mantra_array.length;
    }

    @Override
    public String getItem(int i) {
        return mantra_array[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)  {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
             view = inflater.inflate(R.layout.spinner_item, null);
        }
        TextView mtext = (TextView)view.findViewById(R.id.spinner_textview);
        mtext.setText(getItem(i));
        return view;
    }
}
