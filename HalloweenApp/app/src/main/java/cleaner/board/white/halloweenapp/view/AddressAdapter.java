package cleaner.board.white.halloweenapp.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.model.Address;

/**
 * Created by Akshay on 10/24/2015.
 */
public class AddressAdapter extends ArrayAdapter<Address> {

    private static final String TAG = AddressAdapter.class.getSimpleName();

    class Holder {
        @Bind(R.id.address) TextView address;
        @Bind(R.id.icon) ImageView icon;
        @Bind(R.id.candy) LinearLayout layout;

        public Holder(View view){
            ButterKnife.bind(this, view);
            layout.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);

        }

        @OnClick(R.id.icon)
        public void toggle(){
            Log.d(TAG, "click");
            if(layout.getVisibility() == View.GONE){
                layout.setVisibility(View.VISIBLE);
                icon.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
            } else {
                layout.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
            }
        }
    }


    public AddressAdapter(Context context){
        super(context, R.layout.item_address);
    }

    @Override
    public View getView(int pos, View convert, ViewGroup parent){
        View view = convert;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if(view == null){
            view = inflater.inflate(R.layout.item_address, null);
            view.setTag(new Holder(view));
        }

        Holder holder = (Holder)view.getTag();
        Address item = getItem(pos);
        holder.address.setText(item.address);

        holder.layout.removeAllViews();

        for(int i = 0; i < item.len(); i++){
            View citem = inflater.inflate(R.layout.item_candy_embed, null);
            ((TextView)citem.findViewById(R.id.name)).setText(item.getName(i));
            ((TextView)citem.findViewById(R.id.count)).setText(""+item.getCount(i));
            holder.layout.addView(citem);
        }

        return view;
    }
}
