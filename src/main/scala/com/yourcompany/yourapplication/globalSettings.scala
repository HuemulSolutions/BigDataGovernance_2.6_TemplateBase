package com.yourcompany.yourapplication

import com.huemulsolutions.bigdata.common._
import scala.collection.mutable.ArrayBuffer 
import scala.io.Source
import java.io.{FileNotFoundException, IOException}

/**
 * Configuración del ambiente
 */
object globalSettings {
   val Global: huemul_GlobalPath  = new huemul_GlobalPath()
   Global.GlobalEnvironments = "production, experimental"
   
   //HDFS base path
   val baseDir = "/user/data"
   
   //Local directory to open configuration files
   val localPath: String = System.getProperty("user.dir").concat("/")
   println(s"path: $localPath")
   
   /**
   * Get encrypted key from file, and return decrypted key. 
   */
  def getKeyFromFile(fileName: String): String = {
    var key: String = null
    
    try {
      val openFile = Source.fromFile(fileName)
      key = openFile.getLines.mkString
      openFile.close()
    } catch {
        case _: FileNotFoundException => println(s"Couldn't find that file: $fileName")
        case e: IOException => println(s"($fileName). Got an IOException! ${e.getLocalizedMessage}")
        case _: Exception => println(s"exception opening $fileName")
    }
    
    key
  }
   

   
   Global.HIVE_HourToUpdateMetadata =50
   Global.CONTROL_Setting.append(new huemul_KeyValuePath("production",getKeyFromFile(s"${localPath}prod-setting-control-connection.set")))
   Global.CONTROL_Setting.append(new huemul_KeyValuePath("experimental",getKeyFromFile(s"${localPath}exp-setting-control-connection.set")))
   
   Global.ImpalaEnabled = false
   //Global.IMPALA_Setting.append(new huemul_KeyValuePath("production",getKeyFromFile(s"${localPath}prod-setting-impala-connection.set")))
   //Global.IMPALA_Setting.append(new huemul_KeyValuePath("experimental",getKeyFromFile(s"${localPath}exp-setting-impala-connection.set")))

   //NEW FROM 2.6 --> add globalSettings Light validation, to use on notebooks
   //Global.setValidationLight()

   /**
    * NEW FROM 2.5
    */
   
   //Agrega configuración para uso de Hortonworks Hive Connector
   //Global.externalBBDD_conf.Using_HWC.setActive(true)
   
   
   /**
    *NEW FROM 2.3 
    */
   
   //Agregar variable con opciones de conexió JDBC para HIVE
   val HIVE_Setting = new ArrayBuffer[huemul_KeyValuePath]()
   HIVE_Setting.append(new huemul_KeyValuePath("production",s"jdbc:hive2://{{server}}:10000/default;user={{user}};password={{pass}}"))
   HIVE_Setting.append(new huemul_KeyValuePath("experimental",s"jdbc:hive2://{{server}}:10000/default;user={{user}};password={{pass}}"))

   //Para indicar el uso adicional de HIVE mediante JDBC para crear la metadata.
   Global.externalBBDD_conf.Using_HIVE.setActive(true).setActiveForHBASE(true).setConnectionStrings(HIVE_Setting)
   
   /**
    *NEW FROM 2.1 
    */
   
   Global.HIVE_HourToUpdateMetadata = 2 //Numero de horas para guardar cache de metadata de hive, disminuye tiempo de respuesta en 1 minuto aproximadamente, dependiendo del tamaño del cluster.
   
   /**
    *NEW FROM 2.0 
    */
   
   //BACKUP
   Global.MDM_SaveBackup = true
   Global.MDM_Backup_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/backup/"))
   Global.MDM_Backup_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/backup/"))
      
   //DATA QUALITY PATH & DATABASE
   Global.DQ_SaveErrorDetails = true
   Global.DQError_DataBase.append(new huemul_KeyValuePath("production","production_master"))   
   Global.DQError_DataBase.append(new huemul_KeyValuePath("experimental","experimental_master"))
   
   Global.DQError_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/dq/"))
   Global.DQError_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/dq/"))

   //MDM OLD VALUE TRACE PATH & DATABASE
   Global.MDM_SaveOldValueTrace = true
   Global.MDM_OldValueTrace_DataBase.append(new huemul_KeyValuePath("production","production_oldvalue"))   
   Global.MDM_OldValueTrace_DataBase.append(new huemul_KeyValuePath("experimental","experimental_oldvalue"))
   
   Global.MDM_OldValueTrace_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/oldvalue/"))
   Global.MDM_OldValueTrace_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/oldvalue/"))
   
   /**
    *FROM 1.0 
    */
   //TEMPORAL SETTING
   Global.TEMPORAL_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/temp/"))
   Global.TEMPORAL_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/temp/"))
     
   //RAW SETTING
   Global.RAW_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/raw/"))
   Global.RAW_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/raw/"))
   
   Global.RAW_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/raw/"))
   Global.RAW_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/raw/"))
   
   
   
   //MASTER SETTING
   Global.MASTER_DataBase.append(new huemul_KeyValuePath("production","production_master"))   
   Global.MASTER_DataBase.append(new huemul_KeyValuePath("experimental","experimental_master"))

   Global.MASTER_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/master/"))
   Global.MASTER_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/master/"))
   
   Global.MASTER_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/master/"))
   Global.MASTER_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/master/"))

   //DIM SETTING
   Global.DIM_DataBase.append(new huemul_KeyValuePath("production","production_dim"))   
   Global.DIM_DataBase.append(new huemul_KeyValuePath("experimental","experimental_dim"))

   Global.DIM_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/dim/"))
   Global.DIM_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/dim/"))
   
   Global.DIM_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/dim/"))
   Global.DIM_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/dim/"))

   //ANALYTICS SETTING
   Global.ANALYTICS_DataBase.append(new huemul_KeyValuePath("production","production_analytics"))   
   Global.ANALYTICS_DataBase.append(new huemul_KeyValuePath("experimental","experimental_analytics"))
   
   Global.ANALYTICS_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/analytics/"))
   Global.ANALYTICS_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/analytics/"))
   
   Global.ANALYTICS_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/analytics/"))
   Global.ANALYTICS_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/analytics/"))

   //REPORTING SETTING
   Global.REPORTING_DataBase.append(new huemul_KeyValuePath("production","production_reporting"))
   Global.REPORTING_DataBase.append(new huemul_KeyValuePath("experimental","experimental_reporting"))

   Global.REPORTING_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/reporting/"))
   Global.REPORTING_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/reporting/"))
   
   Global.REPORTING_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/reporting/"))
   Global.REPORTING_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/reporting/"))

   //SANDBOX SETTING
   Global.SANDBOX_DataBase.append(new huemul_KeyValuePath("production","production_sandbox"))
   Global.SANDBOX_DataBase.append(new huemul_KeyValuePath("experimental","experimental_sandbox"))
   
   Global.SANDBOX_SmallFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/sandbox/"))
   Global.SANDBOX_SmallFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/sandbox/"))
   
   Global.SANDBOX_BigFiles_Path.append(new huemul_KeyValuePath("production",s"$baseDir/production/sandbox/"))
   Global.SANDBOX_BigFiles_Path.append(new huemul_KeyValuePath("experimental",s"$baseDir/experimental/sandbox/"))

   
}

