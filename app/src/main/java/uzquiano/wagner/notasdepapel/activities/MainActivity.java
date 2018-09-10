package uzquiano.wagner.notasdepapel.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import uzquiano.wagner.notasdepapel.R;
import uzquiano.wagner.notasdepapel.adapters.MyAdapter;
import uzquiano.wagner.notasdepapel.models.Note;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Note>> {

    //private ListView listView;
    private GridView gridView;
    private MyAdapter adapter;
    String descripcion, descripcion2, textoposicion;
    private Context context = this;
    int post, actual , nueva, colorposicion;


    private Realm realm;
    private RealmResults<Note> people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        gridView = (GridView) findViewById(R.id.GridView);

        people = getAllPeople();
        people.addChangeListener(this);

        adapter = new MyAdapter(people, R.layout.grid_item_view, this);

        gridView.setAdapter(adapter);
        registerForContextMenu(gridView);
    }

    @Override
    public void onChange(RealmResults<Note> element) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        realm.removeAllChangeListeners();
        realm.close();
        super.onDestroy();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(this.people.get(info.position).getId() + "");
        inflater.inflate(R.menu.context_menu, menu);


    }
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.item_delete2:
                realm.beginTransaction();
                people.deleteFromRealm(info.position);
                realm.commitTransaction();
                return true;
            case R.id.item_change: {
                post = info.position;
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.dialog, null);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                        context);
                mBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.descripcion);
                mBuilder.setCancelable(false).setPositiveButton("Modificar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        descripcion2 = String.valueOf(userInput.getText());
                                        realm.beginTransaction();
                                        people.get(post).setDescripcion(descripcion2);
                                        realm.commitTransaction();

                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
                return true;
            }
            case R.id.item_color:
                post = info.position;
                LayoutInflater Co = LayoutInflater.from(context);
                View DialogColorView = Co.inflate(R.layout.dialog_color, null);

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                        context);

                alertDialogBuilder.setView(DialogColorView).setNegativeButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });;
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;

            case R.id.item_posicion:
                post = info.position;
                LayoutInflater po = LayoutInflater.from(context);
                View DialogPostView = po.inflate(R.layout.dialog_posicion, null);
                android.app.AlertDialog.Builder alertDialogBuilderPost = new android.app.AlertDialog.Builder(context);
                alertDialogBuilderPost.setView(DialogPostView);
                final EditText userInput = (EditText) DialogPostView.findViewById(R.id.editText);
                alertDialogBuilderPost.setCancelable(false).setPositiveButton("Mover",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        nueva = Integer.valueOf(userInput.getText().toString());
                                         int tamaño = people.size();
                                        if(nueva > tamaño){
                                            Toast.makeText(MainActivity.this, "Accion incorrecta", Toast.LENGTH_SHORT).show();
                                        }else{
                                            if (nueva >= 1) {


                                                realm.beginTransaction();
                                                colorposicion = people.get(nueva-1).getColor();
                                                textoposicion = people.get(nueva-1).getDescripcion();
                                                people.get(nueva-1).setColor(people.get(post).getColor());
                                                people.get(nueva-1).setDescripcion(people.get(post).getDescripcion());
                                                people.get(post).setColor(colorposicion);
                                                people.get(post).setDescripcion(textoposicion);
                                                realm.commitTransaction();
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Error de posición", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                android.app.AlertDialog alertDialogPost = alertDialogBuilderPost.create();
                alertDialogPost.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_add:
                addPeople();
                return true;
            case R.id.item_delete:
                removeAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private RealmResults<Note> getAllPeople() {
        return realm.where(Note.class).findAll();
    }

    private void removeAll() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_celeste:
                realm.beginTransaction();
                people.get(post).setColor(1);
                realm.commitTransaction();
                break;
            case R.id.button_verde:
                realm.beginTransaction();
                people.get(post).setColor(2);
                realm.commitTransaction();
                break;
            case R.id.button_azul:
                realm.beginTransaction();
                people.get(post).setColor(3);
                realm.commitTransaction();
                break;
            case R.id.button_rojo:
                realm.beginTransaction();
                people.get(post).setColor(4);
                realm.commitTransaction();
                break;

        }
    }




    private void addPeople() {
        LayoutInflater li = LayoutInflater.from(context);
        View mView = li.inflate(R.layout.dialog,null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setView(mView);
        final EditText Descripcion = (EditText) mView.findViewById(R.id.descripcion);

        mBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Descripcion.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Nota agregada",Toast.LENGTH_SHORT).show();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(final Realm realm) {
                            Note n1 = new Note(Descripcion.getText().toString(),0);
                            realm.copyToRealmOrUpdate(n1);
                            people = getAllPeople();
                        }
                    });
                    dialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this, R.string.error_login_msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




}
