package me.archdev

import akka.http.scaladsl.testkit.ScalatestRouteTest
import me.archdev.restapi.http.HttpService

trait BaseServiceTest extends BaseDbTest with ScalatestRouteTest with HttpService {
}
