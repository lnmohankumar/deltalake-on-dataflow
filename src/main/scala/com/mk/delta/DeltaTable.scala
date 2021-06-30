package com.mk.delta

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.current_timestamp

object DeltaTable {

  def getSparkSession(): SparkSession = {
    val spark = SparkSession
      .builder()
      .appName("DeltaTable Simulation")
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.delta.catalog.DeltaCatalog")
      .config("spark.hadoop.fs.AbstractFileSystem.oci.impl", "com.oracle.bmc.hdfs.Bmc")
//     .master("local[1]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    spark
  }

  def csvToDelta(inputPath: String, outputPath: String) = {

    val spark = getSparkSession()

    val original_df = spark
      .read
      .format("csv")
      .option("header", "true")
      .load(inputPath)

      //val newDF = original_df.select("hack_license").withColumnRenamed("hack_license", "hack_license1")
      .withColumn("time_stamp", current_timestamp())

    original_df.write.partitionBy("vendor_id").format("delta").mode("overwrite").save(outputPath)

  }

  def csvToParquet(inputPath: String, outputPath: String) = {

    val spark = getSparkSession()

    val original_df = spark
      .read
      .format("csv")
      .option("header", "true")
      .load(inputPath)

      .withColumn("time_stamp", current_timestamp())

      original_df.write.partitionBy("vendor_id").mode("overwrite").parquet(outputPath)

  }

  def show(deltaTablePath: String) = {

    val spark = getSparkSession()
    val deltaDF = spark.read.format("delta").load(deltaTablePath);

    deltaDF.show()
  }

  def runVacuum (deltaTablePath: String) = {

    val spark = getSparkSession()
    val deltaTable = io.delta.tables.DeltaTable.forPath(spark, deltaTablePath)

    deltaTable.vacuum()
  }


  def runHistory(deltaTablePath: String) = {
    val spark = getSparkSession()

    val deltaTable = io.delta.tables.DeltaTable.forPath(spark, deltaTablePath)

    val fullHistoryDF = deltaTable.history()
    print("History for " + deltaTablePath)
    fullHistoryDF.show()
  }

  def runInSQL(parquetTablePath: String) = {
    val spark = getSparkSession()

    spark.sqlContext.sql("CONVERT TO DELTA parquet.`" + parquetTablePath + "` PARTITIONED BY (vendor_id string)");
  }

}
