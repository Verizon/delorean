# Delorean

![image](docs/img/logo.png)

[![Build Status](https://travis-ci.org/Verizon/delorean.svg?branch=master)](https://travis-ci.org/Verizon/delorean)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.verizon.delorean/core_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.verizon.delorean/core_2.11)

Trivial library for correct and stable conversions between `Task` and `Future`.  To use:

For `scalaz-7.1.x`:
```sbt
libraryDependencies += "io.verizon.delorean" %% "core" % "1.2.40-scalaz-7.1"
```

For `scalaz-7.2.x`:
```sbt
libraryDependencies += "io.verizon.delorean" %% "core" % "1.2.40-scalaz-7.2"
```

Cross builds are available for 2.10.x, 2.11.x and for 2.12.x

## Usage

Keep it simple:

```scala
import delorean._       // conversions are in the package object

val f: Future[Foo] = ...
val t: Task[Foo] = f.toTask
val f2: Future[Foo] = t.unsafeToFuture
```

As a general rule of thumb:

```scala
def toTask[A](f: Future[A]) = Task { Await.result(f, Duration.Inf) }
def unsafeToFuture[A](t: Task[A]) = Future { t.run }
```

Obviously, the above is misleading since the definitions of these functions are non-blocking, but this should give you an idea of their rough semantics.

There are a few subtle things that are hidden here by the pretty syntax.

- `toTask` is lazy in the `Future` parameter, meaning that evaluation of the method receiver will be deferred.  This is significant as `Future` is eagerly evaluated and caching.  It is ideal to ensure that you do not accidentally eagerly evaluate `Future` values before they are fed into `toTask` (note: we break this rule in the above example, since `f` is a `val`)
- The `Task` resulting from `toTask` will have sane semantics (i.e. it will behave like a normal `Task`) with respect to reevaluation and laziness, provided that you are disciplined and ensure that the `Future` value that is the dispatch receiver is not eagerly evaluated elsewhere (e.g. in a `val`).  This means that you can reason about the results of `toTask` in a referentially transparent fashion, as if the constituent `Future` had been constructed as a `Task` all along.
- `toTask` takes an implicit `ExecutionContext` *and* an implicit `Strategy`.  You should make sure that the appropriate values of each are in scope.  If you are not overriding Scalaz's default implicit `Strategy` for the rest of your `Task` composition, then you can just rely on the default `Strategy`.  The point is just to make sure that the `Task` resulting from `toTask` is context-shifted into the same thread pool you're using for "normal" `Task`(s).
- `unsafeToFuture` is exactly as unsafe as it sounds, since the `Future` you get back will be running *eagerly*.  Do *not* make use of this function unless you are absolutely sure of what you're doing!  It is very dangerous, because it defeats the expected laziness of your `Task` computation.  This function is meant primarily for interop with legacy libraries that require values of type `scala.concurrent.Future`.
- `toTask` and `unsafeToFuture` are not strict inverses.  They add overhead and defeat fairness algorithms in *both* scala.concurrent and scalaz/scalaz-stream.  So… uh… don't repeatedly convert back and forth, k?

### Pitfalls

There are two major pitfalls here.  First, the `toTask` conversion can only give you a referentially transparent `Task` if you religiously avoid eagerly caching its dispatch receiver.  As a general rule, this isn't a bad idiom:

```scala
def f: Future[A] = ???

val t: Task[A] = f.toTask
```

In other words, `f` is a `def` and not a `val`.  With this sort of machinery, `toTask` will give you a reasonable output.  If you eagerly cache its input `Future` though, the results are on your own head.

Second, `unsafeToFuture` *immediately* runs the input `Task` (as mentioned above).  There really isn't anything else that could be done here, since `Future` is eager, but it's worth mentioning.  Additionally, `unsafeToFuture` makes no attempt to thread-shift the input `Task`, since in general this is not possible (or necessarily desirable).  The resulting `Future` will be on the appropriate thread pool, and it is certainly safe to `flatMap` on said `Future` and treat it normally, but the computation itself will be run on whatever thread scheduler the `Task` was composed against.
