package code {
package snippet {

import net.liftweb.mapper.{OrderBy, Descending, By}
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import code.lib._
import Helpers._

import java.text.{DateFormat}

import code.model._

class Lists {
  private lazy val suits = User.findAll(By(User.kind, UserType.Business), OrderBy(User.createdAt, Descending))
  private lazy val developers = User.findAll(By(User.kind, UserType.Developer), OrderBy(User.createdAt, Descending))
  private lazy val designers = User.findAll(By(User.kind, UserType.Designer), OrderBy(User.createdAt, Descending))

  private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

  def snippet(maxLen: Int)(s: String) : String = {
    if (s == null || s.length < maxLen) s
    else s.substring(0, maxLen) + "..."
  }

  def coloringClass(index: Int, c: String) =
    AttrBindParam("class",
    if (index % 2 == 0) "hl" else "", "class")

  private def profiles(list: List[User], xhtml: NodeSeq): NodeSeq = {
    var i = 0
    list.flatMap(item =>
      bind("i", xhtml,
        "title" -> item.shortName,
        "createdAt" -> dateFormat.format(item.createdAt.is),
        "location" -> <lift:children>{item.city}, {item.country}</lift:children>,
        "snippet" -> snippet(255)(item.statement.asText),
        AttrBindParam("href", "/u/" + item.id.is, "href"),
        { i += 1; coloringClass(i, "hl") }
      )
    )
  }

  def suits(xhtml: NodeSeq): NodeSeq = profiles(suits, xhtml)

  def geeks(xhtml: NodeSeq): NodeSeq = profiles(developers, xhtml)

  def designers(xhtml: NodeSeq) : NodeSeq = profiles(designers, xhtml)
}

}
}
