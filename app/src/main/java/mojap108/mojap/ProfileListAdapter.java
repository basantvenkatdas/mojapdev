package mojap108.mojap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by gollaba on 7/29/16.
 */
public class ProfileListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProfileDrawerListItem> mItems = null;

    public ProfileListAdapter(Context context, ArrayList<ProfileDrawerListItem> items) {
        mContext = context;
        mItems = items;
    }

    public int getItemType(int i) {
        return mItems.get(i).getType();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ProfileDrawerListItem getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder mHolder = null;
        if(view == null) {
            mHolder = new Holder();
            if(getItemType(i) == Constants.PROFILE_HEADER_ITEM ) {
                view = LayoutInflater.from(mContext).inflate(R.layout.profile_header_layout, null);
                mHolder.itemType = Constants.PROFILE_HEADER_ITEM;
                mHolder.imageView = (ImageView)view.findViewById(R.id.profileimageview1);
                mHolder.textView = (TextView)view.findViewById(R.id.profilename1);
            }else {
                view = LayoutInflater.from(mContext).inflate(R.layout.profile_row_layout, null);
                mHolder.itemType = Constants.PROFILE_ROW_ITEM;
                mHolder.imageView = (ImageView)view.findViewById(R.id.rowIcon);
                mHolder.textView = (TextView)view.findViewById(R.id.rowText);
            }
            view.setTag(mHolder);
        }else {
            mHolder = (Holder)view.getTag();
        }
        mHolder.textView.setText(getItem(i).getText());
        if(mHolder.itemType == Constants.PROFILE_HEADER_ITEM) {
            mHolder.imageView.setVisibility(View.VISIBLE);
            mHolder.imageView.setImageResource(getItem(i).getImageResource());
        }else {
            mHolder.imageView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    public class Holder {
        public ImageView imageView;
        public TextView textView;
        public int itemType;
    }
}
