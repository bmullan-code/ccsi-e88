
### Setup redis on an aws instance

https://shawn-shi.medium.com/how-to-install-redis-on-ec2-server-for-fast-in-memory-database-f30c3ef8c35e

```
sudo yum -y install gcc make # install GCC compiler
cd /usr/local/src 
sudo wget http://download.redis.io/redis-stable.tar.gz
sudo tar xvzf redis-stable.tar.gz
sudo rm -f redis-stable.tar.gz
cd redis-stable
sudo yum groupinstall "Development Tools"
sudo make distclean
sudo make
sudo yum install -y tcl
```

#### Setup the instance

```
sudo make test
sudo cp src/redis-server /usr/local/bin/
sudo cp src/redis-cli /usr/local/bin/
```

#### Quick test using cli
```
redis-cli
keys *
set session_id_1 'Hey World'
keys *
get session_id_1
```

