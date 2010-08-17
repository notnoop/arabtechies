package code {
package model {

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

object Gig extends Gig with LongKeyedMetaMapper[Gig]
with CRUDify[Long, Gig] {
  override def dbTableName = "gigs"

  override def fieldOrder = List(title, description, location,
    country, website, contact, notes)
  override def showAllMenuLoc = Empty
}

class Gig extends LongKeyedMapper[Gig] with IdPK with CreatedUpdated {
  def getSingleton = Gig

  object title extends MappedString(this, 255) {
    override def displayName = "Name"
    override def validations = valMinLen(1, "Required Field") _ :: Nil
  }

  object description extends MappedTextarea(this, 2048) {
    override def displayName = "Description"
    override def required_? = true
  }

  object country extends MappedCountry(this) {
    override def displayName = "Country"
  }

  object location extends MappedString(this, 255) {
    override def required_? = true
    override def displayName = "Location (City)"
  }

  object website extends MappedString(this, 255) {
    override def displayName = "Website"
  }

  // Contact
  object contact extends MappedEmail(this, 255) {
    override def displayName = "Contact Email"
  }

  object notes extends MappedTextarea(this, 2048) {
    override def displayName = "Notes"
  }

}

}
}

