package code {
package snippet {

import net.liftweb.mapper.{OrderBy, Descending}
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.lib._
import Helpers._

import java.text.{DateFormat}

import code.model._

class Lists {
  private lazy val gigs = Gig.findAll(OrderBy(Gig.createdAt, Descending))
  private lazy val businesses = Business.findAll(OrderBy(Business.createdAt, Descending))
  private lazy val suites = User.findAll(OrderBy(User.createdAt, Descending))

  private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

  def snippet(maxLen: Int)(s: String) : String = {
    if (s == null || s.length < maxLen) s
    else s.substring(0, maxLen) + "..."
  }

  def coloringClass(index: Int, c: String) =
    AttrBindParam("class",
    if (index % 2 == 0) "hl" else "", "class")

  def gigs(xhtml: NodeSeq): NodeSeq = {
    var i = 0
    gigs.flatMap(item =>
      bind("i", xhtml,
        "title" -> item.title,
        "createdAt" -> dateFormat.format(item.createdAt.is),
        "location" -> item.location,
        "snippet" -> snippet(255)(item.description.is),
        AttrBindParam("href", "/gigs/view/" + item.id.is, "href"),
        { i += 1; coloringClass(i, "hl") }
      )
    )
  }

  def startups(xhtml: NodeSeq): NodeSeq = {
    var i = 0
    businesses.flatMap(item =>
      bind("i", xhtml,
        "title" -> item.title,
        "createdAt" -> dateFormat.format(item.createdAt.is),
        "location" -> item.location,
        "snippet" -> snippet(255)(item.description.is),
        AttrBindParam("href", "/gigs/view/" + item.id.is, "href"),
        { i += 1; coloringClass(i, "hl") }
      )
    )
  }

  def suites(xhtml: NodeSeq): NodeSeq = {
    var i = 0
    suites.flatMap(item =>
      bind("i", xhtml,
        "title" -> item.shortName,
        "createdAt" -> dateFormat.format(item.createdAt.is),
        "location" -> item.location,
        "snippet" -> snippet(255)(item.statement.is),
        AttrBindParam("href", "/suite/view/" + item.id.is, "href"),
        { i += 1; coloringClass(i, "hl") }
      )
    )
  }

  def geeks(xhtml: NodeSeq) = suites(xhtml)
}

}
}
