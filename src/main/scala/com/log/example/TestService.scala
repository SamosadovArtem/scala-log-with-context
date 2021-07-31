package com.log.example

import cats.Functor
import cats.syntax.all._
import com.log.example.CustomLogger.ContextLogOf

trait TestService[F[_]] {
  def logMessage()(implicit ev: ContextLogOf[F]): F[Unit]
}

object TestService {
  def of[F[_]: LogWithCtxOf: Functor]: F[TestService[F]] = {
    LogWithCtxOf.summon[F].apply(getClass).map { logWithCtx =>
      new TestService[F] {
        override def logMessage()(implicit ev: ContextLogOf[F]): F[Unit] = logWithCtx.debug("Message")
      }
    }
  }
}
