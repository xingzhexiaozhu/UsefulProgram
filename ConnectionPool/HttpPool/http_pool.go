package HttpPool

import (
	"net/http"
	"time"
)

func initHttpPool() *http.Client {
	trans := &http.Transport{
		MaxIdleConns:       3,
		IdleConnTimeout:    time.Duration(2000) * time.Second,
		DisableCompression: true,
	}

	return &http.Client{
		Transport:     trans,
		Timeout:       time.Duration(5) * time.Second,
	}
}
