package de.sciss.catmullrom

case class Point2D(x: Double, y: Double) {
  def +(vec   : Point2D ) = Point2D(x + vec.x , y + vec.y )
  def *(scalar: Double  ) = Point2D(x * scalar, y * scalar)

  def distSq(that: Point2D): Double = {
    val dx = that.x - this.x
    val dy = that.y - this.y
    dx * dx + dy * dy
  }

  def dist(that: Point2D): Double = math.sqrt(distSq(that))

  def isNaN: Boolean = x.isNaN || y.isNaN
}