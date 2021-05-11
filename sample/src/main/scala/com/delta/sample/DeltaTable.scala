package com.delta.sample

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.current_timestamp

object DeltaTable {

  //val logger = Logger("TableJoin")

  def getSparkSession (): SparkSession = {
    val spark = SparkSession
      .builder()
      .appName("DeltaTable Simulation")
      //.master("local[1]")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    spark
  }

  def csvReadWrite(inputPath: String, outputPath: String) = {

    val spark = getSparkSession()

    val original_df = spark
      .read
      .format("csv")
      .option("header", "true")
      .load(inputPath)

    val newDF = original_df.select("hack_license").withColumnRenamed("hack_license", "hack_license1")
      .withColumn("time_stamp", current_timestamp())
    val finalDf = newDF.join(original_df, newDF("hack_license1") === original_df("hack_license"), "left")

    finalDf.write.partitionBy("vendor_id").format("delta").mode("overwrite").save(outputPath)

  }

  def show(deltaTablePath: String) = {

    val spark = getSparkSession()
    val deltaDF = spark.read.format("delta").load(deltaTablePath);

    deltaDF.show()
  }

}