# Connect to Redis Command Line Interface.
redis-cli

# Read config parameters CONFIG GET ${parameter_name}. * - all parameters.
CONFIG GET *
CONFIG GET loglevel

# Set config parameter CONFIG SET ${parameter_name} ${parameter_value}
CONFIG SET loglevel "notice"

############################################
## Data types and abstractions
############################################

# Altering and quering the key space
EXISTS  key [key...]    # Check if key exists
DEL     key [key...]    # Delete.
TYPE    key             # Returns the type of the value: {string, list, set, zset, hash, stream}


# Redis expires: keys with limited time to live
EXPIRE  key seconds     # set ttl to the key.
TTL key                 # read remaining ttl for the key. {-2 the key doesn't exists, -1 the key exists but has no associated expire}
SET key value EX 10     # write the key with TTL = 10 sec.
SET key value PX 1000   # write the key with TTL = 1000 milliseconds.


# String - up to 512 mb in one string.
GET     key             # read the key
GETSET  key             # read - rewrite
MGET    key [key...]    # read multiply keys
SET     key value       # write
SET     key value   NX  # write only if not exists
SET     key value   XX  # rewrite only if exists
MSET    key value [key value...] # write multiply keys


# Atomic increment/decrement
INCR    key
INCRBY  key increment
DECR    key
DECRBY  key decrement


# Lists
LPUSH   key value [value...]    # add new element into the list on the head.
RPUSH   key value [value...]    # add new element into the list on the tail.

LRANGE  key start stop          # read range of the list. ( 0, -1 from start to the end)
LPOP    key                     # read and remove element from the head of the list.
RPOP    key                     # read and remove element from the tail of the list.