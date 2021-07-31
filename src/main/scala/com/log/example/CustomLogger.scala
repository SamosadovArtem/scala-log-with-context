package com.log.example

import cats.Monad
import cats.syntax.all._
import com.evolutiongaming.catshelper.{Log, LogOf}
import com.log.example.CustomLogger.{ContextLogOf, LogContext}
import tofu.WithContext

object CustomLogger {
  case class LogContext(requestId: String)
  type ContextLogOf[F[_]] = WithContext[F, LogContext]
}

trait LogWithCtxOf[F[_]] {
  def apply(source: Class[_]): F[LogWithCtx[F]]
}

class LogWithCtxOfImpl[F[_]: Monad](logOf: LogOf[F]) extends LogWithCtxOf[F] {

  override def apply(source: Class[_]): F[LogWithCtx[F]] = logOf(source).map { log =>
    new LogWithCtxImpl(log)
  }
}

object LogWithCtxOf {

  def summon[F[_]](implicit F: LogWithCtxOf[F]): LogWithCtxOf[F] = F

  def apply[F[_]: LogOf: Monad](): LogWithCtxOf[F] = new LogWithCtxOfImpl[F](LogOf[F])

}

trait LogWithCtx[F[_]] {
  def debug(msg: String)(implicit ev: ContextLogOf[F]): F[Unit]
}

class LogWithCtxImpl[F[_]: Monad](log: Log[F]) extends LogWithCtx[F] {
  def debug(msg: String)(implicit ev: ContextLogOf[F]) =
    WithContext[F, LogContext].askF { ctx =>
      log.debug(s"requestId = ${ctx.requestId}, $msg") *> Monad[F].pure(println(s"requestId = ${ctx.requestId}, $msg"))
    }
}
