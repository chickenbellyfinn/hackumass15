package cleaner.board.white.halloweenapp.view;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cleaner.board.white.halloweenapp.R;
import cleaner.board.white.halloweenapp.model.Address;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    @Bind(R.id.list) ListView list;
    @Bind(R.id.totmsg) TextView msg;

    AddressAdapter adapter;

    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_address, container, false);
        ButterKnife.bind(this, view);
        adapter = new AddressAdapter(getActivity());
        list.setAdapter(adapter);
        update();
        return view;
    }

    public void update(){
        if(adapter == null){
            return;
        }
        adapter.clear();
        adapter.addAll(Address.all());
        adapter.notifyDataSetChanged();

        if(adapter.isEmpty()){
            msg.setVisibility(View.VISIBLE);
        } else {
            msg.setVisibility(View.GONE);
        }

    }

}
