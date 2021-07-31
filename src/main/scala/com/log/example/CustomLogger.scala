package com.log.example

import cats.Monad
import cats.syntax.all._
import com.evolutiongaming.catshelper.Log
import com.log.example.ContextLogger.{ContextLogOf, LogContext}
import tofu.WithContext

trait ContextLogger[F[_], A] {
  def debugWitchCtx(msg: String)(implicit ev: ContextLogOf[F]): F[Unit]
}

object ContextLogger {
  case class LogContext(requestId: String)
  type ContextLogOf[F[_]] = WithContext[F, LogContext]
}

object ContextLoggerInstances {
  implicit def contextLoggerStd[F[_]: Monad](log: Log[F]): ContextLogger[F, Log[F]] = new ContextLogger[F, Log[F]] {
    override def debugWitchCtx(msg: String)(implicit ev: ContextLogOf[F]): F[Unit] =
      WithContext[F, LogContext].askF { ctx =>
        log.debug(s"requestId = ${ctx.requestId}, $msg") *> Monad[F].pure(println(s"requestId = ${ctx.requestId}, $msg"))
      }
  }
}
