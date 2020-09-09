package main

import (
	"fmt"
	"io"
	"sync"
	"sync/atomic"
)

/*
 * 使用资源池，模拟数据库连接
 */

const (
	maxGoroutine = 5 // goroutine数量
	poolSize     = 2 // 资源池大小
)

// 数据库连接
type dbConnection struct {
	ID int32 // 连接标志
}
// 实现 io.Closer 接口
func (db *dbConnection) Close() error {
	fmt.Println("关闭连接", db.ID)
	return nil
}

// 生成数据库连接
var idCounter int32
func createConn()(io.Closer, error) {
	// 并发安全给数据库连接生成唯一标识
	id := atomic.AddInt32(&idCounter, 1)
	return &dbConnection{id}, nil
}

// 模拟查询
func dbQuery(query int, pool *Pool) {
	conn, err := pool.Acquire()
	if err != nil {
		fmt.Println(err)
		return
	}

	defer pool.Release(conn)

	// 模拟查询
	fmt.Printf("第%d个查询，使用的ID为%d的数据库连接", query, conn.(*dbConnection).ID)
}

func main() {
	wg := sync.WaitGroup{}
	wg.Add(maxGoroutine)

	pool, err := New(createConn, poolSize)
	if err != nil {
		fmt.Println(err)
		return
	}

	// 模拟并发使用资源池查询数据
	for query := 0; query < maxGoroutine; query++ {
		go func(q int) {
			dbQuery(q, pool)
			wg.Done()
		}(query)
	}

	wg.Wait()
	fmt.Println("关闭连接池")
	pool.Close()
}