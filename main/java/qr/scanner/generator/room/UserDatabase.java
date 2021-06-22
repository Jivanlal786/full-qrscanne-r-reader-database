package qr.scanner.generator.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities={User.class},version = 1)
@TypeConverters({ImageBitmapString.class}) // This will convert Bitmap to String and vice-versa;
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
