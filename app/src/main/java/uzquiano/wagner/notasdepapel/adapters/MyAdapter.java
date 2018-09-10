package uzquiano.wagner.notasdepapel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import io.realm.RealmResults;
import uzquiano.wagner.notasdepapel.R;
import uzquiano.wagner.notasdepapel.models.Note;

public class MyAdapter extends BaseAdapter {

    private List<Note> list;
    private int layout;
    private Context context;

    public MyAdapter(RealmResults<Note> list, int layout, Context context) {
        this.list = list;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.id = (TextView) convertView.findViewById(R.id.textViewId);
            vh.Note = (TextView) convertView.findViewById(R.id.textViewNote);
            vh.color = (LinearLayout) convertView.findViewById(R.id.layout_linea);
            //vh.dogs = (TextView) convertView.findViewById(R.id.textViewDogs);
            convertView.setTag(vh);


        } else {
            vh = (ViewHolder) convertView.getTag();

        }
        Note p = list.get(position);
        vh.id.setText(p.getId() + "");
        vh.Note.setText("\n" + p.getDescripcion());
        if (p.getColor() == 0){
            vh.color.setBackgroundColor(Color.WHITE);
        }
        else if(p.getColor()==1) {
            vh.color.setBackgroundColor(Color.CYAN);
        }
        else if(p.getColor()==2) {
            vh.color.setBackgroundColor(Color.GREEN);
        }
        else if(p.getColor()==3) {
            vh.color.setBackgroundColor(Color.BLUE);
        }
        else if(p.getColor()==4) {
            vh.color.setBackgroundColor(Color.RED);
        }
        return convertView;
    }
    public class ViewHolder {
        TextView id;
        TextView Note;
        LinearLayout color;

    }
}