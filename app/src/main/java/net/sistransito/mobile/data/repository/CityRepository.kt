package net.sistransito.mobile.data.repository

import kotlinx.coroutines.flow.Flow
import net.sistransito.mobile.data.local.dao.CityDao
import net.sistransito.mobile.data.local.entity.CityEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityRepository @Inject constructor(
    private val cityDao: CityDao
) {
    fun getCitiesByState(state: String): Flow<List<CityEntity>> =
        cityDao.getCitiesByState(state)

    fun getAllCities(): Flow<List<CityEntity>> =
        cityDao.getAllCities()

    suspend fun insertCities(cities: List<CityEntity>) =
        cityDao.insertCities(cities)

    fun searchCities(query: String): Flow<List<CityEntity>> =
        cityDao.searchCities(query)
} 