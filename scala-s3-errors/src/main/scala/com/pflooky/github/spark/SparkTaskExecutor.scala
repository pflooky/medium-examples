package com.pflooky.github.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.LoggerFactory

object SparkTaskExecutor extends App {

  private val LOGGER = LoggerFactory.getLogger(getClass)
  private val USER = "customuser"

  def apply(): Unit = {
    val sparkSession = createSparkSession(createSparkConf())
    val readFilePath = "s3a://secret-bucket/README.md"
    val writeFilePath = "s3a://secret-bucket/data/hello_world.csv"

    LOGGER.info(s"user=$USER, path=$readFilePath, msg=Attempting to get object with user")
    try {
      sparkSession.read.text(readFilePath).show()
    } catch {
      case ex: Exception => LOGGER.error(s"user=$USER, path=$readFilePath, msg=Failed to get file from S3, ex-msg=${ex.getMessage}")
    }

    LOGGER.info(s"user=$USER, path=$writeFilePath, msg=Attempting to push object with user")
    try {
      import sparkSession.implicits._
      Seq("hello", "world").toDF()
        .write
        .mode(SaveMode.Overwrite)
        .csv(writeFilePath)
      sparkSession.read.csv(writeFilePath).show()
    } catch {
      case ex: Exception => LOGGER.error(s"user=$USER, path=$writeFilePath, msg=Failed to write data to S3, ex-msg=${ex.getMessage}")
    }
  }

  def createSparkSession(sparkConf: SparkConf): SparkSession = {
    SparkSession.builder()
      .appName("spark-s3-test-job")
      .master("local[1]")
      .config(sparkConf)
      .getOrCreate()
  }

  def createSparkConf(): SparkConf = {
    val conf = new SparkConf()
    conf.set("fs.s3a.endpoint", "http://localhost:9000")
    conf.set("fs.s3a.path.style.access", "true")
    conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
    useCustomerUser(conf)
    conf
  }

  private def useCustomerUser(conf: SparkConf) = {
    conf.set("fs.s3a.access.key", USER)
    conf.set("fs.s3a.secret.key", "customuser123")
  }

  apply()
}
