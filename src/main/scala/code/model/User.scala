package code {
package model {

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

import code.lib.UserType

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
  locale, timezone, password, statement)

  // comment this line out to require email validations
  override def skipEmailValidation = true
}

class User extends MegaProtoUser[User] with CreatedUpdated {
  def getSingleton = User // what's the "meta" server

  // define an additional field for a personal essay
  object displayName extends MappedString(this, 255) {
    override def displayName = "Display Name"
  }

  object location extends MappedString(this, 255) {
    override def displayName = "Location"
  }

  object country extends MappedCountry(this) {
    override def displayName = "Country"
  }

  object website extends MappedString(this, 255) {
    override def displayName = "Website"
  }

  object statement extends MappedTextarea(this, 2048) {
    override def displayName = "Personal Statement"
  }

  object kind extends MappedEnumList(this, UserType) {
    override def displayName = "Type"
  }
}

}
}
