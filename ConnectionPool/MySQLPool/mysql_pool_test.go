package MySQLPool

import (
	"log"
	"testing"
)

func TestMySqlPool(t *testing.T) {
	conn, err := initMySQLPool()

	_, err = conn.Exec("CREATE TABLE IF NOT EXISTS test.hello(world varchar(50))")
	if err != nil{
		log.Fatalln(err)
	}

	rows, err := conn.Query("SELECT world FROM test.hello")
	if err != nil{
		log.Fatalln(err)
	}

	for rows.Next(){
		var s string
		err = rows.Scan(&s)
		if err !=nil{
			log.Fatalln(err)
		}
		log.Printf("found row containing %q", s)
	}
	rows.Close()
}
