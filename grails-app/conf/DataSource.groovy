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
			url = "jdbc:oracle:thin:@localhost:1521:orcl"
			username = "web"
			password = "readonly"
			logSql = true
			/*
			url = "jdbc:oracle:thin:@141.161.25.36:1521:XE"
			username = "web"
			password = "readonly"
			logSql = true
			*/
		}
	}
	test {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:orcl"
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