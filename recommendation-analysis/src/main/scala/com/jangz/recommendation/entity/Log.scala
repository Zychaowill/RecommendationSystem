package com.jangz.recommendation.entity



import scala.beans.BeanProperty
import com.jangz.recommendation.util.StringUtil

class Log extends Serializable {
  @BeanProperty var Ts: Long = 0L
  @BeanProperty var Ip: String = ""
  @BeanProperty var Uuid: String = ""
  
  @BeanProperty var SearchEngine: String = ""
  @BeanProperty var Country: String = ""
  @BeanProperty var Area: String = ""
  
  @BeanProperty var ContentId: Long = 0L
  @BeanProperty var Url: String = ""
  @BeanProperty var Title: String = ""
  
  @BeanProperty var wd: Wd = null
  
  def getPageType(): Char = {
    if (wd != null && wd.getT() != null) {
      val wdt: String = wd.getT()
      if (wdt.length() == 3) {
        val pt: Char = wdt.charAt(2)
        if (pt >= '0' && pt <= '5') {
          return pt
        }
      }
    }
    '6'
  }
  
  def getClearTitle(): String = {
    var title = ""
    if (title != null) {
      title = StringUtil.clearTitleAll(title)
    }
    title
  }
  
  def isLegal(): Boolean = {
    if (Ip == null || Ip.equals("") || Uuid == null || Uuid.equals("")) {
      return false
    }
    true
  }
  
  class Wd extends Serializable {
    @BeanProperty var t: String = ""
  }
}