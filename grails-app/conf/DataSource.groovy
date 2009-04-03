dataSource {
	pooled = true
	driverClassName = "oracle.jdbc.driver.OracleDriver" 
	dialect = org.hibernate.dialect.OracleDialect.class
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			url = "jdbc:oracle:thin:@10.211.55.4:1521:XE"
			username = "common"
			password = "common"
			logSql = true
			/*
			url = "jdbc:hsqldb:file:prodDb;shutdown=true"
			driverClassName = "org.hsqldb.jdbcDriver"
			username = "sa"
			password = ""
			*/
		}
	}
	test {
		dataSource {
			url = "jdbc:oracle:thin:@10.211.55.4:1521:XE"
			username = "common"
			password = "common"
			logSql = true
			/*
			url = "jdbc:oracle:thin:@141.161.25.36:1521:XE"
			username = "web"
			password = "readonly"
			logSql = true
			*/
		}
	}
	production {
		dataSource {
			url = "jdbc:hsqldb:file:prodDb;shutdown=true"
		}
	}
}