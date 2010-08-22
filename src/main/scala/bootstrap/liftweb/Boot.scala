package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import sitemap._
import Loc._
import mapper._

import code.model._


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Business, Gig)

    // where to search snippet
    LiftRules.addToPackages("code")

    def isNumeric(s: String) =
      try {
        Integer.parseInt(s)
        true
      } catch {
        case _ => false
      }


    LiftRules.statelessRewrite.append {
      case RewriteRequest(
        ParsePath(List("u", id),_,_,_),_,_) if isNumeric(id) =>
      RewriteResponse("u" :: "view" :: Nil, Map("id" -> id))
      case RewriteRequest(
        ParsePath(List("u", id, "edit"),_,_,_),_,_) if isNumeric(id) =>
      RewriteResponse("u" :: "edit" :: Nil, Map("id" -> id))
      case RewriteRequest(
        ParsePath(List("login"),_,_,_),_,_) =>
      RewriteResponse("user_mgt" :: "login" :: Nil)
      case RewriteRequest(
        ParsePath(List("logout"),_,_,_),_,_) =>
      RewriteResponse("user_mgt" :: "logout" :: Nil)
      case RewriteRequest(
        ParsePath(List("signup"),_,_,_),_,_) =>
      RewriteResponse("user_mgt" :: "sign_up" :: Nil)
    }

    // Build SiteMap
    val entries = List(
      Menu.i("About Us") / "index", // the simple way to declare a menu
      Menu.i("Geeks") / "geeks",
      Menu.i("Suites") / "suites",

      // Viewing
      Menu.i("ViewProfile") / "u" / "view" >> Hidden,
      Menu.i("EditProfile") / "u" / "edit" >> Hidden

      ) ::: User.sitemap ::: Business.menus

    // the User management menu items
    // User.sitemap ::: Gig.menus

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
}
