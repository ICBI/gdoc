dataSource {
	pooled = true
}
hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'
    validator.apply_to_ddl = false
    validator.autoregister_listeners = false
}
// environment specific settings
environments {

	sandbox {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc_sb"
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
			username = "mcgdoc"
			password = "change_me"
			logSql = true
		}
	}

	development {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc"
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
			username = "guidoc"
			password = "cure4cancer"
			logSql = true
		}
		dataSource_lookup {
			pooled = true
            driverClassName = "com.mysql.jdbc.Driver"
            dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
            url = "jdbc:mysql://pacsdb.c8grftijnwnl.us-east-1.rds.amazonaws.com/pacsdb"
            logSql = true
            username = "root"
            password = "isis6095"
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
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
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
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
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
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
			username = "mcgdoc"
			password = "m#gd0c!"
			logSql = true
		}
	}

	dev_load {
		dataSource {
			url = "jdbc:oracle:thin:@localhost:1521:gdoc"
			driverClassName = "oracle.jdbc.OracleDriver"
			dialect = org.hibernate.dialect.OracleDialect.class
			username = "mcgdoc"
			password = "change_me"
			logSql = true
		}
	}
}
