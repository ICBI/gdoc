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
	
	sandbox {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc_sb"
			username = "mcgdoc"
			password = "change_me"
			logSql = false
		}
	}
	
	development {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = false
		}
	}
	
/*	development {
		dataSource {
			url = "jdbc:oracle:thin:@172.16.200.128:1521:XE"
			username = "mcgdoc"
			password = "cure4cancer"
			logSql = false
		}
	}*/
	devserver {
		dataSource {
			url = "jdbc:oracle:thin:@10.10.50.3:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
	}
	demo {
		dataSource {
			url = "jdbc:oracle:thin:@10.10.50.3:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = false
		}
	}
	test {
		dataSource {
			url = "jdbc:oracle:thin:@10.10.50.3:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
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