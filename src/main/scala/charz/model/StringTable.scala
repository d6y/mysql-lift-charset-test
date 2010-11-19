package charz.model

import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._

class StringTable extends LongKeyedMapper[StringTable] with IdPK {

  def getSingleton = StringTable

  object content extends MappedString(this, 512)

}

object StringTable extends StringTable with LongKeyedMetaMapper[StringTable]