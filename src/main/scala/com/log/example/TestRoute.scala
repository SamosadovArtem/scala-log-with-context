package com.log.example

import cats.Monad
import cats.implicits._
import com.log.example.ContextLogger.{WithLogContext, LogContext}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.typelevel.ci._
import tofu.WithContext

final class TestRoute[F[_]: Monad](service: TestService[F]) extends Http4sDsl[F] {

  val routes: HttpRoutes[F] = HttpRoutes.of { case req @ GET -> Root / "run" =>
    val ctx = LogContext(
      req.headers.get(ci"X-Request-ID").fold("null")(_.head.value)
    )
    implicit val ev: WithLogContext[F] = WithContext.make(Monad[F].pure(ctx))

    service.logMessage() *> Ok()
  }
}
