package net.sistransito.mobile.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.sistransito.mobile.data.local.entity.CityEntity

@Dao
interface CityDao {
    @Query("SELECT * FROM cities WHERE state = :state")
    fun getCitiesByState(state: String): Flow<List<CityEntity>>

    @Query("SELECT * FROM cities")
    fun getAllCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityEntity>)

    @Query("SELECT * FROM cities WHERE name LIKE '%' || :query || '%'")
    fun searchCities(query: String): Flow<List<CityEntity>>
} 