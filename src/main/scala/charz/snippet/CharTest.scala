package charz.snippet

import charz.model._
import scala.xml._

// IMPORTANT: The character encoding of this file *must* be UTF-8
// (or escape the test_data with native2ascii)

class CharTest {

	val test_data = "Mads ♥mann (@Mads_Hartmann)"
	
	def go(in: NodeSeq) : NodeSeq = try {
			val row = StringTable.create.content(test_data).saveMe
			val row_recovered = StringTable.findByKey(row.id) map {_.content.is} openOr("FAIL!")
			Text("Tried to store:") ++ Text(test_data) ++ Text(". Result: ") ++ Text(row_recovered)
		} catch {
			case e => Text(e.getMessage)
		}
		
		

	
}