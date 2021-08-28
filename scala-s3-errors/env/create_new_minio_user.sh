#!/bin/bash
user="customuser"
pw="customuser123"

echo "Using policy defined in custom.json file"
docker cp policy/custom.json mc:/tmp
echo "user=$user, Creating user with custom policy"
docker exec mc bash -c "mc admin user add local $user $pw;
mc admin policy add local custom_policy /tmp/custom.json;
mc admin policy set local custom_policy user=$user;
mc admin user list local;
mc policy get-json local/secret-bucket"
echo "user=$user, Created user"
