

package bootstrap.liftweb

import _root_.net.liftweb.http.{LiftRules, NotFoundAsTemplate, ParsePath}
import _root_.net.liftweb.sitemap.{SiteMap, Menu, Loc}
import _root_.net.liftweb.util.{ NamedPF }
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.mapper.{Schemifier, DB, StandardDBVendor, DefaultConnectionIdentifier}
import _root_.net.liftweb.util.{Props}
import _root_.net.liftweb.common.{Full}
import _root_.net.liftweb.http.{S}
import charz.model._


/*

CREATE DATABASE foo CHARACTER SET utf8 COLLATE utf8_bin;
grant all privileges on foo.* to 'foo'@'localhost' identified by 'foo';
flush privileges;
  
 */

class Boot {
	
	
	val db = "foo"
  val db_user = Full("foo")
  val db_password = Full("foo")
	
  def boot {
  
  	
		val vendor = new StandardDBVendor("com.mysql.jdbc.Driver", 
				"jdbc:mysql://localhost:3306/%s?useUnicode=true&characterEncoding=UTF-8".format(db), 
				db_user, db_password )
	  
		LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)
	  DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
		

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, StringTable)

  
    // where to search snippet
    LiftRules.addToPackages("charz")

    // build sitemap
    val entries = List(Menu("Home") / "index") :::
                  List(Menu(Loc("Static", Link(List("static"), true, "/static/index"), 
                       "Static Content"))) :::
                  Nil
    
    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
       
      // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
    
  }
}