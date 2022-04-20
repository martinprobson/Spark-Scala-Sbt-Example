package net.martinprobson.spark

import org.apache.spark.sql.SaveMode

object Main extends App with Logging with SparkEnv {

  val titlesDF = spark.read.json(getClass.getResource("/data/titles.json").getFile)
  val employeesDF = spark.read.json(getClass.getResource("/data/employees.json").getFile)
  val empTitlesDF = employeesDF.join(titlesDF, Seq("emp_no"), "inner")
  logger.info(s"empTitlesDF count = ${empTitlesDF.count()}")
  versionInfo.foreach(v => logger.info(v))
  employeesDF.write
    .mode(SaveMode.Overwrite)
    .format("orc")
    .save(getClass.getResource("/data").getFile + "/employees.orc")
  spark.stop()
}
