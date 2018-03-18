package de.sciss.catmullrom

import de.sciss.catmullrom.CatmullRomSpline.Type

import scala.collection.immutable.{IndexedSeq => Vec}

final case class CatmullRomSpline(mType: Type, p1: Point2D, p2: Point2D, p3: Point2D, p4: Point2D) {

  /** Interpolates with `p` running from 0.0 to 1.0.
    * Position zero corresponds with `p2`, position one corresponds with `p3`.
    */
  def calc(p: Double): Point2D = {
    val t0 = 0.0
    val t1 = calcKnotFor(t0, p1, p2)
    val t2 = calcKnotFor(t1, p2, p3)
    val t3 = calcKnotFor(t2, p3, p4)
    val dt  = t2 - t1
    val t   = t1 + (p * dt)
    interpolate(t0, t1, t2, t3, t)
  }

  /** Interpolates `n` points with relative position running from `0.0` to `(n-1)/n)`. */
  def calcN(n: Int): Vec[Point2D] = {
    val t0  = 0.0
    val t1  = calcKnotFor(t0, p1, p2)
    val t2  = calcKnotFor(t1, p2, p3)
    val t3  = calcKnotFor(t2, p3, p4)
    val dt  = t2 - t1
    val f   = dt / n

    Vector.tabulate(n) { i =>
      val t = t1 + (i * f)
      interpolate(t0, t1, t2, t3, t)
    }
  }

  private def interpolate(t0: Double, t1: Double, t2: Double, t3: Double, t: Double): Point2D = {
    val a1 = p1 * ((t1 - t) / (t1 - t0)) + p2 * ((t - t0) / (t1 - t0))
    val a2 = p2 * ((t2 - t) / (t2 - t1)) + p3 * ((t - t1) / (t2 - t1))
    val a3 = p3 * ((t3 - t) / (t3 - t2)) + p4 * ((t - t2) / (t3 - t2))

    val b1 = a1 * ((t2 - t) / (t2 - t0)) + a2 * ((t - t0) / (t2 - t0))
    val b2 = a2 * ((t3 - t) / (t3 - t1)) + a3 * ((t - t1) / (t3 - t1))

    val c  = b1 * ((t2 - t) / (t2 - t1)) + b2 * ((t - t1) / (t2 - t1))

    c
  }

  private def calcKnotFor(prevKnot: Double, vecA: Point2D, vecB: Point2D): Double =
    math.pow(math.sqrt(math.pow(vecB.x - vecA.x, 2.0) + math.pow(vecB.y - vecA.y, 2.0)), mType.alpha) + prevKnot
}

object CatmullRomSpline {
  final implicit class Type(val alpha: Double) extends AnyVal

  val Uniform     = new Type(0.0)
  val Chordal     = new Type(1.0)
  val Centripetal = new Type(0.5)
}