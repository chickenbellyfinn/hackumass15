package cleaner.board.white.halloweenapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cleaner.board.white.halloweenapp.model.Candy;
import cleaner.board.white.halloweenapp.R;

/**
 * Created by Akshay on 10/24/2015.
 */
public class CandyAdapter extends ArrayAdapter<Candy> {

    private boolean numbersEnabled = true;

    class Holder {
        @Bind(R.id.address) TextView name;
        @Bind(R.id.count) TextView count;
        public Holder(View view){
            ButterKnife.bind(this, view);
        }
    }

    public CandyAdapter(Context context) {
        super(context, R.layout.item_candy);
    }

    public void setNumbersEnabled(boolean enabled){
        numbersEnabled = false;
        notifyDataSetChanged();
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

        holder.name.setText(String.valueOf(item.name.charAt(0)).toUpperCase() + item.name.substring(1));
        holder.count.setText(item.count+"");

        if(!numbersEnabled){
            holder.count.setVisibility(View.GONE);
        } else {
            holder.count.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
