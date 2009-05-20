dataSource {
	pooled = true
	driverClassName = "oracle.jdbc.OracleDriver" 
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
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
			/*
			url = "jdbc:oracle:thin:@141.161.25.36:1521:XE"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
			*/
		}
	}
	demo {
		dataSource {
			url = "jdbc:oracle:thin:@141.161.54.205:1521:orcl"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
	}
	test {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:orcl"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
			/*
			url = "jdbc:oracle:thin:@141.161.25.36:1521:XE"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
			dialect = ""
			driverClassName = "org.hsqldb.jdbcDriver"
			dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:mem:devDB"
			*/
		}
	}
	production {
		dataSource {
			url = "jdbc:oracle:thin:@141.161.25.36:1521:XE"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
	}
}