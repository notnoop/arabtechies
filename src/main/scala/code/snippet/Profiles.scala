package code {
package snippet {

import net.liftweb.mapper.{OrderBy, Descending}
import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http.S
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.java.util.Date
import _root_.net.liftweb.mapper.MappedField
import code.lib._
import Helpers._

import java.text.{DateFormat}

import code.model.User

class Profiles {
  private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

  var id = S.param("id") openOr ""

  var item = try {
    User.findByKey(id.toLong)
  } catch {
    case e:NumberFormatException => Empty
  }

  def view(xhtml: NodeSeq): NodeSeq = {
      val m = item openOr new User
      val fields = List(m.city, m.country, m.website)

      def ifField(f: MappedField[_, _])(in: NodeSeq): NodeSeq =
        if (f.is == null || f.is == "") NodeSeq.Empty else bind("u", in, "value" -> f.asHtml)

      item map ({u =>
        bind("u", xhtml,
          "title" -> u.displayName,
          "createdAt" -> dateFormat.format(u.createdAt.is),
          "updatedAt" -> dateFormat.format(u.updatedAt.is),
          "description" -> u.statement.asHtml,
          "location" -> ifField(u.city) _,
          "country" -> ifField(u.country) _,
          "website" -> ifField(u.website) _,
          "twitter" -> ifField(u.twitter) _,
          "linkedin" -> ifField(u.linkedin) _,
          "name" -> u.shortName,
          AttrBindParam("contactURL", "/u/" + id + "/contact", "href")
        )
      }) openOr <p>Test</p>
  }

  def edit (html: NodeSeq): NodeSeq ={
    item map ({ i =>
        i.toForm(Full("save"), "/u/" + i.id)
    }) openOr Text("Invalid Item")
  }


  def create (xhtml: NodeSeq): NodeSeq = {
    val user = new User
    user.toForm(Full("save"), "/u/" + user.id)
  }
}

}
}

