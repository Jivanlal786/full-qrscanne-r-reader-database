package qr.scanner.generator.room;

import android.graphics.Bitmap;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "aadhar_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String gender;
    private String uid;
    private String birth;
    private Bitmap image;

    public User() {
    }

//    public User(int id, String name, String gender, String uid, String birth, Bitmap image) {
//        this.id = id;
//        this.name = name;
//        this.gender = gender;
//        this.uid = uid;
//        this.birth = birth;
//        this.image = image;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
