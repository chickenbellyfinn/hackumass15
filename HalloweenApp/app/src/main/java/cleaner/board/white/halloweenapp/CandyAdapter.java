package cleaner.board.white.halloweenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Akshay on 10/24/2015.
 */
public class CandyAdapter extends ArrayAdapter<Candy> {

    class Holder {
        @Bind(R.id.name) TextView name;
        @Bind(R.id.count) TextView count;
        public Holder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public CandyAdapter(Context context) {
        super(context, R.layout.item_candy);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup root){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_candy, null);
            view.setTag(new Holder(view));
        }

        Holder holder = (Holder)view.getTag();
        Candy item = getItem(position);

        holder.name.setText(item.name);
        holder.count.setText(item.count+"");

        return view;
    }
}
