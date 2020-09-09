package main

import (
	"io"
	"io/ioutil"
	"log"
	"net"
	"net/http"
)

/*
 * 每个连接一个goroutine的模式(goroutine-per-conn)
 */

func handleConn(conn net.Conn) {
	io.Copy(ioutil.Discard, conn)
}

func main() {
	listen, err := net.Listen("tcp", ":8972")
	if err != nil {
		panic(err)
	}

	go func() {
		if err := http.ListenAndServe(":6060", nil); err != nil {
			log.Fatalf("pprof failed: %v", err)
		}
	}()

	var connections []net.Conn
	defer func() {
		for _, conn := range connections {
			conn.Close()
		}
	}()

	for {
		conn, err := listen.Accept()
		if err != nil {
			if ne, ok := err.(net.Error); ok && ne.Temporary() {
				log.Printf("accept temp err: %v", ne)
				continue
			}

			log.Printf("accept err: %v", err)
			return
		}

		go handleConn(conn)
		connections = append(connections, conn)
		if len(connections) % 100 == 0 {
			log.Printf("total number of connections: %v", len(connections))
		}
	}
}
