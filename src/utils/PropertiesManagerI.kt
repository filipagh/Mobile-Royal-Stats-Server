package utils

import javax.ejb.Local

/**
 * interface k PropertiesManager
 */
@Local
interface PropertiesManagerI {
   open fun getProp(key:String):String
}