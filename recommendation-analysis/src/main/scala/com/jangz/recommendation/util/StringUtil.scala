package com.jangz.recommendation.util

object StringUtil {
  def limitString(str: String, len: Int, encoding: String): String = {
    var s = str
    var yy: Array[Byte] = s.getBytes(encoding)
    while (s != null && yy.length > len) {
      var sublen: Int = getEncodingLen(yy.length, s.length(), len)
      if (sublen < 0) {
        sublen = s.length() / 2
      }
      s = s.substring(0, sublen)
      yy = s.getBytes(encoding)
    }
    s
  }

  def getEncodingLen(bytelen: Int, strlen: Int, maxlen: Int): Int = {
    strlen - (bytelen - maxlen)
  }

  def clearTitleAll(title: String): String = {
    var result: String = clearTitle(title, "-")
    result = clearTitle(result, "_")
    result
  }

  def clearTitle(title: String, symbol: String): String = {
    if (title == null) {
      return ""
    }
    val index: Int = title.lastIndexOf(symbol)
    if (index < 5) {
      title
    } else {
      clearTitle(title.substring(0, index), symbol)
    }
  }
}