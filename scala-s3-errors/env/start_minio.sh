#!/bin/bash

user=minio
pw=minio123
port=9000

echo "Attempting to startup minio with user=$user, pw=$pw on port $port"

docker run -d --name minio -p $port:$port  \
-e MINIO_ROOT_USER=$user -e MINIO_ROOT_PASSWORD=$pw \
-v /mnt/data:/data --entrypoint bash minio/minio -c "mkdir -p /data/my-bucket && /usr/bin/minio server /data"

echo "Try login via browser localhost:9000"

---
docker-compose create
docker-compose up -d

mc admin policy add local putonly putonly.json
mc admin user add local uploaduser uploaduser123
mc admin policy set local putonly user=uploaduser
mc admin user list local
