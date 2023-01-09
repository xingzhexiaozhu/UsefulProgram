package main

import (
	"fmt"
	"runtime"
	"sync"
	"time"
)

// Pool 协程池定义
type Pool struct {
	queue chan int
	wg    *sync.WaitGroup
}

// New 创建协程池
func New(size int) *Pool {
	if size < 1 {
		size = 1
	}
	return &Pool{
		queue: make(chan int, size),
		wg:    &sync.WaitGroup{},
	}
}

// Add 往协程池加任务
func (p *Pool) Add(delta int) {
	for i := 0; i < delta; i++ {
		p.queue <- 1
	}
	p.wg.Add(delta)
}

// Wait 等待任务完成
func (p *Pool) Wait() {
	p.wg.Wait()
}

// Done 任务执行完成减一
func (p *Pool) Done() {
	<-p.queue
	p.wg.Done()
}

func main() {
	pool := New(5)
	fmt.Println("the NumGoroutine begin is:", runtime.NumGoroutine())
	for i := 0; i < 20; i++ {
		pool.Add(1)
		go func(i int) {
			time.Sleep(time.Second)
			fmt.Printf("idx:%d, the NumGoroutine continue is:%d\n", i, runtime.NumGoroutine())
			pool.Done()
		}(i)
	}
	pool.Wait()
	fmt.Println("the NumGoroutine done is:", runtime.NumGoroutine())
}
