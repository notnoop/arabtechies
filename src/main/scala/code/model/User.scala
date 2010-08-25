package code {
package model {

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.http.S
import code.lib.UserType
import _root_.net.liftweb.sitemap.Loc._

/**
 * The singleton that has methods for accessing the database
 */
object User extends User with MetaMegaProtoUser[User] {
  override def dbTableName = "users" // define the DB table name
  override def screenWrap = Full(<lift:surround with="default" at="content">
			       <lift:bind /></lift:surround>)

  override def signupFields = List(firstName, lastName, email,
    password, statement, website, city, country, kind)

  // define the order fields will appear in forms and output
  override def fieldOrder = List(id, firstName, lastName, email,
  locale, timezone, password, statement)

  // comment this line out to require email validations
  override def skipEmailValidation = true

  override def createUserMenuLocParams = Hidden :: super.createUserMenuLocParams
  override def loginMenuLocParams = Hidden :: super.loginMenuLocParams
  override def lostPasswordMenuLocParams = Hidden :: super.lostPasswordMenuLocParams
  override def changePasswordMenuLocParams = Hidden :: super.changePasswordMenuLocParams
  override def editUserMenuLocParams = Hidden :: super.editUserMenuLocParams
  override def logoutMenuLocParams = Hidden :: super.logoutMenuLocParams

  override def signupXhtml(user: User) = {
    (<form method="post" class="profile_form" action={S.uri}>
          {localForm(user, false)}
          <user:submit/>
        </form>)
  }

  override def editXhtml(user: User) = {
    (<form method="post" class="profile_form" action={S.uri}>
          {localForm(user, false)}
          <user:submit/>
        </form>)
  }

  override protected def localForm(user: User, ignorePassword: Boolean): scala.xml.NodeSeq = {
    <fieldset>
      <legend>About You</legend>

      <div class="required">
        <label>Name </label>
        { user.displayName.toForm open_! }
        <p class="explanation">Company name or the name you work under</p>
      </div>

      { if (!ignorePassword)
      <div class="required">
        <label>Password</label>
        { user.password.toForm open_! }
      </div>
      }

      <div class="required">
        <label>Email</label>
        { user.email.toForm open_! }
        <p class="explanation">Your email be concealed using the <a
        href="http://mailhide.recaptcha.net/">MailHide</a> service to help
        prevent Spam</p>
      </div>

      <div class="required">
        <label>Type</label>
        { user.kind.toForm open_! }
      </div>

      <div>
        <label>Website</label>
        { user.website.toForm open_! }
      </div>

      <div class="required">
        <label>City</label>
        { user.city.toForm open_! }
      </div>

      <div class="required">
        <label>Country</label>
        { user.country.toForm open_! }
      </div>

      <div class="required">
        <label>Statement markdown</label>
        { user.statement.toForm open_! }
        <p class="explanation">Anything you'd like to say about yourself, your
        skills and experience. You can use <a
        href="http://daringfireball.net/projects/markdown/basics">Markdown</a>
        but no html.</p>
      </div>
    </fieldset>
  }
}

class User extends MegaProtoUser[User] with CreatedUpdated {
  def getSingleton = User // what's the "meta" server

  def detailFields = List(city, country, website, email)

  // define an additional field for a personal essay
  object displayName extends MappedString(this, 255) {
    override def displayName = "Display Name"
    override def required_? = true
    override def validations = valMinLen(1, "Name is required") _ :: Nil
  }

  object city extends MappedString(this, 255) {
    override def displayName = "City"
    override def required_? = true
    override def validations = valMinLen(1, "City is required") _ :: Nil
  }

  object country extends MappedCountry(this) {
    override def displayName = "Country"
    override def required_? = true
  }

  object website extends MappedString(this, 255) {
    override def displayName = "Website"
  }

  object statement extends MappedTextarea(this, 2048) {
    override def displayName = "Personal Statement"
    override val textareaCols = 40
    override val textareaRows = 10

    override def asHtml : scala.xml.Node = {
      import com.tristanhunt.knockoff.DefaultDiscounter._
      val text : String = is
      toXHTML(knockoff(text.replace("\r\n", "\n")))
    }
    def asText: String = {
      import com.tristanhunt.knockoff.DefaultDiscounter._
      val text : String = is
      toText(knockoff(text.replace("\r\n", "\n")))
    }
  }

  object kind extends MappedEnum(this, UserType) {
    override def displayName = "Type"
  }
}

}
}
