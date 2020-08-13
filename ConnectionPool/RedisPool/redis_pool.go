package RedisPool

import (
	"fmt"
	"github.com/garyburd/redigo/redis"
	"time"
)

func initRedisPool() *redis.Pool {
	return &redis.Pool{ //实例化一个连接池
		MaxIdle:     2,   //最初的连接数量
		MaxActive:   10,  //最大连接数量，连接池最大连接数量、不确定可以用0（0表示自动定义）表示按需分配
		IdleTimeout: 300, //连接关闭时间 300秒 （300秒不使用自动关闭）
		Dial: func() (redis.Conn, error) { //要连接的redis数据库
			return redis.Dial("tcp", "localhost:6379")
		},
		TestOnBorrow: func(c redis.Conn, t time.Time) error {
			_, err := c.Do("PING")
			if err != nil {
				return fmt.Errorf("ping redis error: %s", err)
			}
			return nil
		},
	}
}

