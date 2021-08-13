//package com.brtrip.trip.domain
//
//import com.brtrip.TestDataLoader
//import com.brtrip.path.controller.request.PathRequest
//import com.brtrip.path.domain.PathFinder
//import com.brtrip.path.domain.PathPlaceFinder
//import com.brtrip.place.Place
//import com.brtrip.place.PlaceRequest
//import com.brtrip.trip.controller.request.TripRequest
//import io.kotlintest.shouldBe
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.jdbc.Sql
//import org.springframework.transaction.annotation.Transactional
//
//@SpringBootTest
//@Transactional
//@Sql("/truncate.sql")
//internal class TripUpdaterTest {
//
//    @Autowired
//    private lateinit var sut: TripUpdater
//
//    @Autowired
//    private lateinit var tripFinder: TripFinder
//
//    @Autowired
//    private lateinit var pathFinder: PathFinder
//
//    @Autowired
//    private lateinit var tripPathFinder: TripPathFinder
//
//    @Autowired
//    private lateinit var pathPlaceFinder: PathPlaceFinder
//
//    @Autowired
//    private lateinit var testDataLoader: TestDataLoader
//
//    @Test
//    fun `여행 일정 수정 - 바뀐 Path가 DB에 존재하는 경우`() {
//        // given
//        val place1 = Place(
//            lat = "123",
//            lng = "456",
//            name = "central park"
//        )
//        val place2 = Place(
//            lat = "789",
//            lng = "101",
//            name = "grand canyon"
//        )
//
//        val trip = testDataLoader.sample_trip_first(1L)
//
//        // path 2개 저장
//        var path1 = testDataLoader.sample_path_first(1L)
//        var path2 = testDataLoader.sample_path_first(2L)
//
//        // place 3개 저장
//        testDataLoader.sample_place_first(place1)
//        testDataLoader.sample_place_first(place2)
//
//        // 기존 trip은 path1을 가짐
//        testDataLoader.sample_trip_path_first(trip, path1)
//
//        // path1(as-is)에 대한 pathPlace 지정
//        testDataLoader.sample_path_place_first(path1, place1, 1)
//        testDataLoader.sample_path_place_first(path1, place2, 2)
//
//        // path2(to-be)에 대한 pathPlace 지정
//        testDataLoader.sample_path_place_first(path2, place2, 1)
//        testDataLoader.sample_path_place_first(path2, place1, 2)
//
//        // 1. 변경된 path가 DB 존재하는 경우 (path2: place2 -> place1)
//        val requestPathExist = TripRequest(
//            title = "new trip",
//            startDate = "2021-08-05",
//            endDate = "2021-08-08",
//            paths = listOf(
//                PathRequest(
//                    id = 1,
//                    places = listOf(
//                        PlaceRequest(
//                            lat = "789",
//                            lng = "101",
//                            name = "grand canyon"
//                        ),
//                        PlaceRequest(
//                            lat = "123",
//                            lng = "456",
//                            name = "central park"
//                        )
//                    )
//                )
//            )
//        )
//
//        // when
//        sut.update(trip.id!!, requestPathExist)
//
//        // then
//        val updatedTrip = tripFinder.findById(trip.id!!)
//        val updatedTripPaths = tripPathFinder.findBy(updatedTrip)
//        val updatedPathPlaces = pathPlaceFinder.findBy(updatedTripPaths[0].path)
//
//        // 1. trip
//        updatedTrip.title shouldBe "new trip"
//        updatedTrip.memo shouldBe null
//
//        // 2. tripPath
//        updatedTripPaths[0].trip.id shouldBe trip.id
//        updatedTripPaths[0].trip.title shouldBe "new trip"
//        updatedTripPaths[0].path.id shouldBe path2.id
//
//        // 3. pathPlace
//        updatedPathPlaces[0].sequence shouldBe 1
//        updatedPathPlaces[0].place.lat shouldBe place2.lat
//        updatedPathPlaces[0].place.lng shouldBe place2.lng
//        updatedPathPlaces[0].place.name shouldBe place2.name
//
//        updatedPathPlaces[1].sequence shouldBe 2
//        updatedPathPlaces[1].place.lat shouldBe place1.lat
//        updatedPathPlaces[1].place.lng shouldBe place1.lng
//        updatedPathPlaces[1].place.name shouldBe place1.name
//    }
//
//    @Test
//    fun `여행 일정 수정 - 바뀐 Path가 DB에 없는 경우`() {
//        // given
//        val place1 = Place(
//            lat = "123",
//            lng = "456",
//            name = "central park"
//        )
//        val place2 = Place(
//            lat = "789",
//            lng = "101",
//            name = "grand canyon"
//        )
//
//        val trip = testDataLoader.sample_trip_first(1L)
//
//        // path 하나만 저장 (1L)
//        var path1 = testDataLoader.sample_path_first(1L)
//
//        // place: 2개 저장
//        testDataLoader.sample_place_first(place1)
//        testDataLoader.sample_place_first(place2)
//
//        // tripPath: trip은 path1을 가짐
//        testDataLoader.sample_trip_path_first(trip, path1)
//
//        // pathPlace: path1에 대한 place 지정
//        testDataLoader.sample_path_place_first(path1, place1, 1)
//        testDataLoader.sample_path_place_first(path1, place2, 2)
//
//        val requestPathNotExist = TripRequest(
//            title = "new trip2",
//            startDate = "2021-08-05",
//            endDate = "2021-08-08",
//            paths = listOf(
//                PathRequest(
//                    id = 1,
//                    places = listOf(
//                        PlaceRequest(
//                            lat = "789",
//                            lng = "101",
//                            name = "grand canyon"
//                        ),
//                        PlaceRequest(
//                            lat = "123",
//                            lng = "456",
//                            name = "central park"
//                        )
//                    )
//                )
//            )
//        )
//
//        // when
//        sut.update(trip.id!!, requestPathNotExist)
//
//        // then
//        val updatedTrip = tripFinder.findById(trip.id!!)
//        val updatedTripPaths = tripPathFinder.findBy(updatedTrip)
//        val updatedPathPlaces = pathPlaceFinder.findBy(updatedTripPaths[0].path)
//
//        // 1. trip
//        updatedTrip.title shouldBe "new trip2"
//        updatedTrip.memo shouldBe null
//
//        // 2. tripPath
//        updatedTripPaths[0].trip.id shouldBe trip.id
//        updatedTripPaths[0].trip.title shouldBe "new trip2"
//
//        // 3. pathPlace
//        updatedPathPlaces[0].sequence shouldBe 1
//        updatedPathPlaces[0].place.lat shouldBe place2.lat
//        updatedPathPlaces[0].place.lng shouldBe place2.lng
//        updatedPathPlaces[0].place.name shouldBe place2.name
//
//        updatedPathPlaces[1].sequence shouldBe 2
//        updatedPathPlaces[1].place.lat shouldBe place1.lat
//        updatedPathPlaces[1].place.lng shouldBe place1.lng
//        updatedPathPlaces[1].place.name shouldBe place1.name
//    }
//}
