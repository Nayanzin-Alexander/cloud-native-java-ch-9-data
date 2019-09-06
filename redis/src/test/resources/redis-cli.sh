############################################
## Data types and abstractions
############################################

# Altering and querying the key space
EXISTS  key [key...]            # Check if key exists
DEL     key [key...]            # Delete.
TYPE    key                     # Returns the type of the value: {string, list, set, zset, hash, stream}
KEYS    pattern                 # Returns exist keys matched to the pattern

# Redis expires: keys with limited time to live
EXPIRE  key seconds             # set ttl to the key.
TTL key                         # read remaining ttl for the key. {-2 the key doesn't exists, -1 the key exists but has no associated expire}
SET key value EX 10             # write the key with TTL = 10 sec.
SET key value PX 1000           # write the key with TTL = 1000 milliseconds.

# String - up to 512 mb in one string.
GET     key                     # read the key
GETSET  key                     # read - rewrite
MGET    key [key...]            # read multiply keys
SET     key value               # write
SET     key value   NX          # write only if not exists
SET     key value   XX          # rewrite only if exists
MSET    key value [key value..] # write multiply keys

# Atomic increment/decrement
INCR    key
INCRBY  key increment
DECR    key
DECRBY  key decrement

# Lists
LPUSH   key value [value...]    # add new element into the list on the head.
RPUSH   key value [value...]    # add new element into the list on the tail.
LLEN    key                     # returns the length of the list.
LRANGE  key start stop          # read range of the list. ( 0, -1 from start to the end)
LPOP    key                     # read and remove element from the head of the list.
RPOP    key                     # read and remove element from the tail of the list.
LTRIM   key start stop          # removes all except specified range elements from the list.
BLPOP   key [key...]    timeout # waits timeout seconds on empty list till any element will be added into it, then lpop operation performs.
BRPOP   key [key...]    timeout # waits timeout seconds on empty list till any element will be added into it, then rpop operation performs.

# Hashes
HMSET   key field value [field value...]    # add new field/value pairs to the hashtable.
HMGET   key field [field...]    # get specified fields values from the hashtable.
HSET    key field value         # write field/value to the hashtable.
HGET    key field               # get specified field's value from the hashtable.
HGETALL key                     # get all field/value pairs.
HINCRBY key field increment     # increments field's value in the hashtable.
HKEYS   key                     # get all keys
HVALUES key                     # get all values

# Sets
SADD    key member [member...]  # add members
SMEMBERS    key                 # get all members
SISMEMBER   key member          # check if member exists in set
SINTER key [key...]             # get sets intersection
SPOP    key [count]             # get and remove count random elements
SUNIONSTORE destination key [key...] # save multiply sets union to destination set
SCARD   key                     # get number of elements

# Sorted sets
ZADD key score member [score member...] # add or rewrite score
ZRANGE key start stop [WITHSORES] # get members with index in range INC
ZREVRANGE key start stop        # get members with index in range DECR
ZRANGEBYSCORE   key min max [WITHSCORES] [LIMIT offset limit]
ZREMRANGEMYSCORE key min max    # removes members having score in specified range

# Bitmaps - 512 MB, ~ 4 billion of bits
SETBIT  key offset  value       # set bit
GETBIT  key offset              # get bit
BITCOUNT key [start end]        # get bit population
BITPOS  key bit [start] [end]   # get the offset for the specified occurrence of the specified bit

# HyperLogLogs
PFADD key element [element...]  # add elements to the counter
PFCOUNT key [key...]            # count unique elements across keys

# Iterating over keyspace of a large collection
# An iteration starts when the cursor is set to 0, and terminates when the cursor
# returned by the server is 0.
# An elements can be returned more than once. So the operation performed must be safe when re-applied.
SCAN cursor [MATCH pattern] [COUNT count]
SSCAN key cursor [MATCH pattern] [COUNT count]
HSCAN key cursor [MATCH pattern] [COUNT count]
ZSCAN key cursor [MATCH pattern] [COUNT count]
