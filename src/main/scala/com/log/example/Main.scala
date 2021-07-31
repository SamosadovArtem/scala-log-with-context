package com.log.example

import cats.Monad
import cats.effect.{Resource, _}
import com.evolutiongaming.catshelper.LogOf
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.RequestId

import scala.concurrent.ExecutionContext

object Main extends IOApp {
  implicit val ec: ExecutionContext  = ExecutionContext.global
  implicit val emptyLogOf: LogOf[IO] = LogOf.empty[IO]

  override def run(args: List[String]): IO[ExitCode] = {

    val dsl = for {
      service <- Resource.eval(TestService.of[IO])
      route <- Resource.pure(
        RequestId.httpRoutes(new TestRoute[IO](service).routes)
      )
      _ <- BlazeServerBuilder[IO](ec)
        .bindHttp(8080, "127.0.0.1")
        .withHttpApp(route.orNotFound)
        .resource
    } yield ()

    dsl.use(_ => IO.never).as(ExitCode.Success)
  }
}
