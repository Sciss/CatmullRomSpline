package de.sciss.catmullrom

import java.awt.geom.{Ellipse2D, Path2D}
import java.awt.{BasicStroke, Color, RenderingHints}

import de.sciss.catmullrom.CatmullRomSpline.{Centripetal, Chordal, Uniform}

import scala.swing.{Component, Dimension, Frame, Graphics2D, MainFrame, SimpleSwingApplication}

object CatmullRomDemo extends SimpleSwingApplication {
  lazy val top: Frame = {
    val rnd     = new util.Random(6L)
    val N       = 12
    val width   = 800
    val height  = 800
    val pad     = 32
    val pad2    = pad + pad
    val wm      = width  - pad2
    val hm      = height - pad2

    val fix     = Vector.fill(N)(Point2D(rnd.nextInt(wm) + pad, rnd.nextInt(hm) + pad))
    val intp    = List(Uniform -> Color.red, Centripetal -> Color.blue, Chordal -> new Color(0, 0x80, 0)).map {
      case (tpe, colr) =>
        fix.sliding(4).flatMap { case Seq(f1, f2, f3, f4) =>
          CatmullRomSpline(tpe, f1, f2, f3, f4).calcN(32)
        }.toArray -> colr
    }

    val c       = new Component {
      preferredSize = new Dimension(width, height)

      private[this] val oval        = new Ellipse2D.Double
      private[this] val gp          = new Path2D.Double
      private[this] val strkDashed  = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10f,
        Array[Float](3f, 3f), 0f)

      override protected def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING  , RenderingHints.VALUE_ANTIALIAS_ON )
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE  )
        g.setColor(Color.white)
        g.fillRect(0, 0, width, height)

        val strkOrig = g.getStroke
        intp.foreach { case (arr, colr) =>
          g.setColor(colr)
          g.setStroke(strkDashed)
          gp.reset()
          arr.iterator.zipWithIndex.foreach { case (pt, pi) =>
            if (pi == 0) gp.moveTo(pt.x, pt.y)
            else gp.lineTo(pt.x, pt.y)
          }
          g.draw(gp)
          g.setStroke(strkOrig)
        }

        g.setColor(Color.black)
        fix.iterator.zipWithIndex.foreach { case (pt, pi) =>
          oval.setFrameFromCenter(pt.x, pt.y, pt.x - 4, pt.y - 4)
          g.fill(oval)
          g.drawString(pi.toString, pt.x.toInt + 6, pt.y.toInt)
        }
      }
    }

    new MainFrame {
      title     = "red = uniform, green = chordal, blue = centripetal"
      contents  = c
      resizable = false
      pack().centerOnScreen()
      open()
    }
  }
}