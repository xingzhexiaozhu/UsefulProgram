package main

import (
	"errors"
	"fmt"
	"io"
	"sync"
)

// 一个安全的资源池，被管理的资源必须都实现io.Close接口
type Pool struct {
	m       sync.Mutex                // 互斥锁，保证多goroutine访问资源时池内值是安全的
	res     chan io.Closer            // 有缓冲的通道，保存共享资源
	factory func() (io.Closer, error) // 函数类型，当需要一个新资源时可通过这个函数创建（灵活）
	closed  bool                      // 表示资源池是否关闭，如果关闭则不可再访问
}

var ErrPoolClosed = errors.New("资源池已关闭")

// 创建资源池，接收两个参数，返回资源池的指针
//  fn：创建新资源的函数；
//  size：指定资源池大小；
func New(fn func() (io.Closer, error), size uint) (*Pool, error) {
	if size < 0 {
		return nil, errors.New("size invalid")
	}
	return &Pool{
		res:     make(chan io.Closer, size),
		factory: fn,
	}, nil
}

// 从资源池获取一个资源
func (p *Pool) Acquire() (io.Closer, error) {
	select {
	case r, ok := <-p.res:
		fmt.Println("Acquire:共享资源")
		if !ok {
			return nil, ErrPoolClosed
		}
		return r, nil
	default:
		fmt.Println("Acquire:新生成资源")
		return p.factory()
	}
}

// 释放资源归还给资源池，以便复用
func (p *Pool) Release(r io.Closer) {
	p.m.Lock()
	defer p.m.Unlock()

	// 如果资源池已关闭，则只有当前一个资源、直接释放即可
	if p.closed {
		r.Close()
		return
	}

	select {
	case p.res <- r:
		fmt.Println("资源归还资源池")
	default:
		fmt.Println("资源池已满，释放当前资源")
		r.Close()
	}
}

// 关闭资源池，即释放所有资源
func (p *Pool) Close() {
	p.m.Lock()
	defer p.m.Unlock()

	if p.closed {
		return
	}
	p.closed = true

	// 关闭通道
	close(p.res)

	// 关闭通道的资源
	for r := range p.res {
		r.Close()
	}
}

