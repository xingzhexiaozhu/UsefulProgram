package MySQLPool

import (
	"database/sql"
	"fmt"
	_ "github.com/go-sql-driver/mysql"
)

func initMySQLPool() (*sql.DB, error){
	connUrl := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?parseTime=true&loc=Local&charset=%s&timeout=%dms&readTimeout=%dms&writeTimeout=%dms",
		"root", "zl19921013", "127.0.0.1", "3306", "test", "utf-8", 5000, 5000, 5000)
	dbConn, err := sql.Open("mysql", connUrl)
	dbConn.SetMaxOpenConns(2000)
	dbConn.SetMaxIdleConns(20)
	if err != nil {
		return nil, fmt.Errorf("init db failed %s", err.Error())
	}

	return dbConn, err
}
