package taxipark

import kotlin.math.ceil
import kotlin.math.floor

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers
        .filter { driver -> trips.find { trip -> trip.driver == driver } == null }
        .toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    allPassengers
        .filter { passenger -> trips.filter { trip -> trip.passengers.contains(passenger) }.size >= minTrips }
        .toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    allPassengers
        .filter { passenger ->
            trips.filter { trip -> trip.passengers.contains(passenger) && trip.driver == driver }.count() > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers
        .filter { passenger ->
            trips
                .filter { trip -> trip.passengers.contains(passenger) }
                .map { trip -> if (trip.discount != null) 1 else - 1 }
                .sum() > 0
        }.toSet()

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? =
    trips
        .groupBy { trip -> (floor(trip.duration / 10.0).toInt() * 10) until (floor(trip.duration / 10.0).toInt() * 10) + 10 }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
        .firstOrNull()?.first6

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) {
        return false
    }
    val top20Income = allDrivers
        .map { driver -> driver to trips.filter { trip -> trip.driver == driver }.map { trip -> trip.cost }.sum() }
        .sortedByDescending { it.second }.slice(0 until floor(allDrivers.size / 5.0).toInt())
        .map { it.second }
        .sum()
    val totalIncome = trips.map { it.cost }.sum()
    return top20Income / totalIncome >= 0.8
}