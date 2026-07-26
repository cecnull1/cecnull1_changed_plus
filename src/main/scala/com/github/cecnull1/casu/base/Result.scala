package com.github.cecnull1.casu.base

case class Result[+T] private (private val value: Any) extends AnyVal with IterableOnce[T] with Product with Serializable:
  def isSuccess: Boolean = !isFailure
  def isFailure: Boolean = value.isInstanceOf[Result.Failure]

  def getOrNull: T | Null =
    if isSuccess then value.asInstanceOf[T] else null

  def exceptionOrNull: Throwable | Null =
    value match
      case f: Result.Failure => f.exception
      case _ => null

  override def iterator: Iterator[T] =
    if isSuccess then
      Iterator.single(value.asInstanceOf[T])
    else
      Iterator.empty

  def map[U](f: T => U): Result[U] =
    if isSuccess then
      Result.success(f(value.asInstanceOf[T]))
    else
      Result(value.asInstanceOf[Result.Failure])

  def flatMap[U](f: T => Result[U]): Result[U] =
    if isSuccess then
      f(value.asInstanceOf[T])
    else
      Result(value.asInstanceOf[Result.Failure])

  def fold[U](onSuccess: T => U, onFailure: Throwable => U): U =
    if isSuccess then
      onSuccess(value.asInstanceOf[T])
    else
      value.asInstanceOf[Result.Failure].exception
      onFailure(value.asInstanceOf[Result.Failure].exception)

  def getOrElse[U >: T](default: => U): U =
    if isSuccess then value.asInstanceOf[T]
    else default

  def orElse[U >: T](alternative: => Result[U]): Result[U] =
    if isSuccess then this
    else alternative

  def toOption: Option[T] = if isSuccess then Some(value.asInstanceOf[T]) else None
end Result

object Result:

  private class Failure(val exception: Throwable) extends Serializable

  def success[T](v: T): Result[T] = new Result(v)

  def failure[T](e: Throwable): Result[T] = new Result(new Failure(e))
end Result
