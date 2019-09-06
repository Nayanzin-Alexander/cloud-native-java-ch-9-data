############################################
## Redis Cluster workaround
############################################

# Bootstrap the cluster nodes
start_cmd='redis-server
--port 6379
--cluster-enabled yes
--cluster-config-file nodes.conf
--cluster-node-timeout 5000
--appendonly yes'

redis_image='redis'
network_name='redis_cluster_net'

docker network create $network_name
echo $network_name created


# Create the cluster nodes
container-name=''
for port in `seq 6379 6384`; do \
 container_name="redis-"$port
 docker run -d --name $container_name  -p $port:6379 --net $network_name $redis_image $start_cmd;
 echo "created redis cluster node redis-"$port
done


# Discover cluster node hosts
cluster_hosts=''
for port in `seq 6379 6384`; do \
    hostip=`docker inspect -f '{{(index .NetworkSettings.Networks "redis_cluster_net").IPAddress}}' "redis-"$port`; \
    echo "IP for cluster node redis-"$port "is" $hostip; \
    cluster_hosts="$cluster_hosts$hostip:6379 ";
done
echo "cluster hosts "$cluster_hosts


# Start Redis Cluster
echo "creating cluster...."
docker run -i \
            --rm \
            --net $network_name \
            $redis_image \
            redis-cli \
                --cluster create $cluster_hosts \
                --cluster-replicas 1;