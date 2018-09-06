package uzquiano.wagner.notasdepapel.models;

import android.graphics.Color;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import uzquiano.wagner.notasdepapel.application.MyApplication;

public class Note  extends RealmObject{
    @PrimaryKey
    private int Id;
    private String Descripcion;
    private int Color;

    public Note(){}

    public Note(String descripcion,int color ){
        Id = MyApplication.NoteId.incrementAndGet();
        Descripcion = descripcion;
        Color = color;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
