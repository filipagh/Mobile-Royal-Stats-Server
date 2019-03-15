package utils

import java.io.FileInputStream
import java.io.InputStream
import java.util.*



class PropertiesManager
{



    fun initProperties()
    {

        var prop = Properties()
        val impStream: InputStream?

        //TODO cesta k conf
        impStream = FileInputStream("conf/conf.properties")
        prop.load(impStream)

        impStream.close()
        prop.getProperty("aa")
    }

    fun getDbName()
    {


    }

}
