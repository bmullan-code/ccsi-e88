# CSCI-E88 Assignment 1

## create an ssh key pair

```
cd tf
ssh-keygen -t rsa -C "your_email@example.com" -f ./tf-cloud-init
cd ..
```
- Add the public key (tf-cloud-init.pub) to tf/scripts/add-ssh-web-app.yaml
- this will mean the public key is added to the AWS instance, and the private key can be used to ssh to the instance.

## Provision the AWS instance
```
cd assignment1
terraform -chdir=tf init
terraform -chdir=tf apply -auto-approve

# confirm the instance is created and running
terraform -chdir=tf state list
terraform -chdir=tf state show aws_instance.bm-assignment1
```


## SSH into the instance

```
# ssh using terraform variable for public_ip
export INSTANCE_IP=$(terraform -chdir=tf output -raw public_ip)

ssh terraform@$INSTANCE_IP -i ./tf/tf-cloud-init

exit
```

## Build the java application

```
cd hw1-java
./gradlew build
cd ..
```

## Copy application jar to instance home directory
```
scp -i ./tf/tf-cloud-init hw1-java/build/libs/cscie88_java_2022-1.0-SNAPSHOT.jar terraform@$INSTANCE_IP:~ 
```

## Run the application in the instance

```
ssh -i ./tf/tf-cloud-init terraform@$INSTANCE_IP "cd ~; java -jar cscie88_java_2022-1.0-SNAPSHOT.jar 5 5;ls *.txt"
```

## tear down the infrastructure
```
terraform -chdir=tf destroy -auto-approve
```
