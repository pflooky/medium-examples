package com.pflooky.github.spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory

object SparkTaskExecutor extends App {

  private val LOGGER = LoggerFactory.getLogger(getClass)
  private val USERS = List("upload", "uploadWithList", "uploadWithListAndDelete", "download", "downloadWithList")

  def apply(): Unit = {
    val sparkSession = createSparkSession(createSparkConf())
    val filePath = "s3a://secret-bucket/top-secret/Install-Linux-tar.txt"
    val testUploadFilePath = "/tmp/test.txt"

    USERS.foreach(user => {
      LOGGER.info(s"user=$user, path=$filePath, msg=Attempting to get object with upload user")
      try {
        val file = sparkSession.read.text(filePath)
        file.show()
      } catch {
        case ex: Exception => LOGGER.warn(s"user=$user, path=$filePath, msg=Failed to get file from S3, ex-msg=${ex.getMessage}")
      }
    })
  }

  def createSparkSession(sparkConf: SparkConf): SparkSession = {
    SparkSession.builder()
      .appName("spark-s3-test-job")
      .master("local[4]")
      .config(sparkConf)
      .getOrCreate()
  }

  def createSparkConf(): SparkConf = {
    val conf = new SparkConf()
    conf.set("fs.s3a.endpoint", "http://localhost:9000")
    conf.set("fs.s3a.path.style.access", "true")
    conf.set("fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
    useUploadUser(conf)
    conf
  }

  //Exception in thread "main" java.nio.file.AccessDeniedException: s3a://secret-bucket/top-secret/Install-Linux-tar.txt: getFileStatus on s3a://secret-bucket/top-secret/Install-Linux-tar.txt

  private def useUploadUser(conf: SparkConf) = {
    conf.set("fs.s3a.access.key", "uploaduser")
    conf.set("fs.s3a.secret.key", "uploaduser123")
  }

  apply()
}
