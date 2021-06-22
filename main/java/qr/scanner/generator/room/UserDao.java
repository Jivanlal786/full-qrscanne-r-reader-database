package qr.scanner.generator.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long addData(User user);

    @Query("SELECT * FROM aadhar_table")
    public List<User> getFavoriteData();

    @Query("SELECT * FROM aadhar_table WHERE uid like :uid")
    public boolean isFavorite(String uid);
}
