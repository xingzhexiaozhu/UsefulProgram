package HttpPool

import (
	. "github.com/smartystreets/goconvey/convey"
	"testing"
)

func TestHttpPool(t *testing.T) {
	Convey("get a http pool", t, func() {
		client := initHttpPool()
		So(client, ShouldNotBeNil)
	})
}

func TestHttpGet(t *testing.T) {
	client := initHttpPool()
	resp, err := client.Get("https://www.baidu.com")
	Convey("test http get", t, func() {
		if err != nil {
			So(resp, ShouldBeNil)
		}
	})
}