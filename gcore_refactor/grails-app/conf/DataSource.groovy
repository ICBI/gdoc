dataSource {
	pooled = true
	driverClassName = "oracle.jdbc.OracleDriver" 
	dialect = org.hibernate.dialect.OracleDialect.class
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
	
	sandbox {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc_sb"
			username = "mcgdoc"
			password = "change_me"
			logSql = true
		}
	}
	
	development {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
	}
	
	devserver {
		dataSource {
			jndiName="java:gdoc"
			logSql = true
		}
	}
	demo {
		dataSource {
			jndiName="java:gdoc"
			logSql = false
		}
	}
	test {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc"
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
	}
	stage {
		dataSource {
			jndiName="java:gdoc"
			logSql = false
		}
	}
	stage_load {
		dataSource {
			url = "jdbc:oracle:thin:@//ohs-test-1.uis.georgetown.edu:1521/gdoct_portal.svcs.gu"
			username = "mcgdoc"
			password = "m#gd0c!"
			logSql = true
		}
	}
	production {
		dataSource {
			jndiName="java:gdoc"
			logSql = false
		}
	}
	prod_load {
		dataSource {
			url = "jdbc:oracle:thin:@//ohs-prod-1.uis.georgetown.edu:1521/gdocp_portal.svcs.gu"
			username = "mcgdoc"
			password = "m#gd0c!"
			logSql = true
		}
	}
}