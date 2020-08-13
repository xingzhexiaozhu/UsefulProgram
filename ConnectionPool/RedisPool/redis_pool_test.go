package RedisPool

import (
	"github.com/garyburd/redigo/redis"
	"testing"
)

func TestRedisPool(t *testing.T) {
	pool := initRedisPool()

	c := pool.Get() //从连接池，取一个链接
	defer c.Close() //函数运行结束 ，把连接放回连接池

	_, err := c.Do("Set", "abc", 200)
	if err != nil {
		t.Error(err)
	}

	r, err := redis.Int(c.Do("Get", "abc"))
	if err != nil {
		t.Error(err)
	}
	t.Log(r)
}