package fathurrahman.projeku.a7langit.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fathurrahman.projeku.a7langit.db.table.FavoriteTable

@Dao
interface daoFavorite {
    @Query("SELECT * FROM FavoriteTable WHERE status=:status")
    fun getAll(status: String) : LiveData<List<FavoriteTable>>

    @Query("SELECT * FROM FavoriteTable WHERE name=:name")
    fun getByName(name: String): LiveData<FavoriteTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: FavoriteTable)

    @Query("UPDATE FavoriteTable SET status=:status WHERE name=:name")
    fun update(name: String, status: String)

    @Query("DELETE FROM FavoriteTable")
    fun deleteAll()
}