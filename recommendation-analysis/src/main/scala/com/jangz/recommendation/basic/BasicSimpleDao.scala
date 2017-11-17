package com.jangz.recommendation.basic

import com.jangz.recommendation.util.db.DBHelper
import java.sql.Connection
import scala.collection.mutable.ListBuffer

object BasicSimpleDao {
  
  def getSqlObject(sql: String, entity: Object): Any = {
    val conn: Connection = DBHelper.getConnection
    try {
      BasicDao.getSqlObject(sql, entity, conn)
    } finally {
      DBHelper.close(conn)
    }
  }
  
  def getSqlList(sql: String, entity: Object): ListBuffer[Any] = {
    val conn: Connection = DBHelper.getConnection
    try {
      BasicDao.getSqlList(sql, entity, conn)
    } finally {
      DBHelper.close(conn)
    }
  }
  
/**
	 * @param sql
	 * @param cls
	 * @return
	 */
	def getSqlList(sql: String, cls: Class[_]): ListBuffer[Any] = {
		 val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.getSqlList(sql, cls, conn)
     } finally {
       DBHelper.close(conn)
     }
	}
  
  /**
	 * @param sql
	 * @param entity
	 */
	def saveObject(sql: String, entity: Object): Int = {
	   val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.saveObject(sql, entity, conn)
     } finally {
       DBHelper.close(conn)
     }
	}
	
	/**
	 * @param sql
	 * @param entities
	 */
	def saveList(sql: String, entities: List[Object]): Int = {
	   val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.saveList(sql, entities, conn)
     } finally {
       DBHelper.close(conn)
     }
	}
	
	/**
	 * @param sql
	 * @param entities
	 */
  def saveListBatch(sql: String, entities: List[Object]): Int = {
     val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.saveListBatch(sql, entities, conn)
     } finally {
       DBHelper.close(conn)
     }
  }
  
  /**
	 * @param sql
	 * @param entity
	 */
	def deleteObject(sql: String, entity: Object): Int = {
	   val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.deleteObject(sql, entity, conn)
     } finally {
       DBHelper.close(conn)
     }
	}
	
	/**
	 * @param sql
	 * @param entity
	 */
	def updateObject(sql: String, entity: Object): Int = {
	   val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.updateObject(sql, entity, conn)
     } finally {
       DBHelper.close(conn)
     }
	}
	
	/**
	 * @param sql
	 * @param entities
	 */
	def updateList(sql: String, entities: List[Object]): Int = {
	   val conn: Connection = DBHelper.getConnection()
     try {
       BasicDao.updateList(sql, entities, conn)
     } finally {
       DBHelper.close(conn)
     }
	}  
}