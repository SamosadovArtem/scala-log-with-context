package com.log.example

import cats.Monad
import cats.syntax.all._
import com.evolutiongaming.catshelper.LogOf
import com.log.example.ContextLogger._
import com.log.example.ContextLoggerInstances._

trait TestService[F[_]] {
  def logMessage()(implicit ev: ContextLogOf[F]): F[Unit]
}

object TestService {
  def of[F[_]: LogOf: Monad]: F[TestService[F]] = {
    LogOf[F].apply(getClass).map { logger =>
      new TestService[F] {
        override def logMessage()(implicit ev: ContextLogOf[F]): F[Unit] = {
          logger.debugWitchCtx("Message")
        }
      }
    }
  }
}
