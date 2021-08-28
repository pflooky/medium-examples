#!/bin/bash

echo "Attempting to startup minio"
cd env && docker-compose up -d
echo "Try login via browser: localhost:9000 with user: minio, pw: minio123"
