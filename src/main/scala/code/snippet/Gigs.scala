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

import code.model.Gig

class Gigs {
  private def toShow = Gig.findAll(OrderBy(Gig.createdAt, Descending))
  private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

  def recentGigs(xhtml: NodeSeq): NodeSeq = {
    toShow.flatMap(gig =>
      bind("g", xhtml,
        "title" -> gig.title,
        "createdAt" -> gig.createdAt,
        "location" -> gig.location,
        "description" -> gig.description
      )
    )
  }
}

}
}

