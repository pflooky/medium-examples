package com.pflooky.github.s3

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.{GetObjectRequest, PutObjectRequest}

import java.io.File

object AwsS3SdkExecutor extends App {

  def apply(): Unit = {
    val region = "ap-east-1"
    val basicAWSCredentials = new BasicAWSCredentials("minio", "minio123")
    val clientConfig = new ClientConfiguration()
    clientConfig.setSignerOverride("AWSS3V4SignerType")

    val s3Client = AmazonS3ClientBuilder
      .standard()
      .withClientConfiguration(clientConfig)
      .withPathStyleAccessEnabled(true)
      .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
      .withEndpointConfiguration(new EndpointConfiguration("http://localhost:9000", region))
      .build()

    println("Attempting to get file from S3 and print out metadata")
    s3Client
      .getObject(new GetObjectRequest("secret-bucket", "data/hello_world.csv/_SUCCESS"))
      .getObjectMetadata
      .getRawMetadata
      .entrySet()
      .forEach(println)

    println("Attempting to push file into S3")
    s3Client.putObject(
      new PutObjectRequest("secret-bucket", "README.md", new File("README.md"))
    )
    println("Done!")
  }

  apply()
}
