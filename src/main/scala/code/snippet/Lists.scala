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

import code.model.{Gig, User}

class Lists {
  private val gigs = Gig.findAll(OrderBy(Gig.createdAt, Descending))
  private val suites = User.findAll(OrderBy(User.createdAt, Descending))

  private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)

  def gigs(xhtml: NodeSeq): NodeSeq = {
    gigs.flatMap(item =>
      bind("i", xhtml,
        "title" -> item.title,
        "createdAt" -> item.createdAt,
        "location" -> item.location,
        "snippet" -> item.description
      )
    )
  }

  def allSuites(xhtml: NodeSeq): NodeSeq = {
    suites.flatMap(item =>
      bind("i", xhtml,
        "name" -> item.firstName,
        "createdAt" -> item.createdAt,
        "location" -> item.location,
        "snippet" -> item.statement
      )
    )
  }
}

}
}
