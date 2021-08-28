# S3 Access Testing
## Overview
Quick and easy way to test out whether your app/job has the correct permissions and configuration to access objects in S3.  
Can also be tested out with proxy being setup.

## How to run
### Start Minio
```shell
./env/start_minio.sh
```

### Create custom user
1. Alter [custom.json](env/policy/custom.json) to desired AWS policy
      1. [For complete list of AWS S3 actions, can view this page](https://docs.aws.amazon.com/AmazonS3/latest/API/API_Operations.html)
1. Run `./env/create_new_minio_user.sh`

### Upload sample file
1. Can do via browser
    1. Login to [localhost:9000](localhost:9000) with `minio:minio123`
    1. Upload [README.md](README.md) into `secret-bucket`
1. Via AWS CLI
```shell
export AWS_ACCESS_KEY_ID=minio
export AWS_SECRET_ACCESS_KEY=minio123
aws s3 ls --endpoint-url=http://localhost:9000
aws s3 cp README.md s3://secret-bucket/README.md --endpoint-url=http://localhost:9000
aws s3 ls s3://secret-bucket/ --endpoint-url=http://localhost:9000
```

### Run task
Can run with the newly created user to attempt to get the README from Minio  
[SparkTaskExecutor](src/main/scala/com/pflooky/github/spark/SparkTaskExecutor.scala)  
Can try to alter the S3 configurations inside the SparkTaskExecutor to desired configuration.  
[For reference, can look up all the various configuration options here](https://hadoop.apache.org/docs/stable/hadoop-aws/tools/hadoop-aws/index.html)
