/**
 * Gant script to use Hibernate's SchemaExport tool to generate DDL or export the schema.
 * <p/>
 * Usage: grails [environment] schema-export [generate | export] [stdout] [filename]
 * The action (generate or export), stdout option, and the filename are optional; if ommitted
 * the action is generate, the filename defaults to ddl.sql in the project base dir, and ddl
 * is only sent to stdout if the stdout option is present.
 * <p/>
 * Example 1: 'grails schema-export' generates ddl.sql for the default configuration
 * Example 2: 'grails prod schema-export stdout' generates ddl.sql for the 'prod' configuration, sending
 *            ddl to stdout in addition to the default file.
 * Example 3: 'grails dev schema-export c:/foo/bar/grails-app.sql' generates the file 'c:/foo/bar/grails-app.sql'
 *            for the 'dev' configuration.
 * Example 4: 'grails prod schema-export export' exports the schema (drop and create) using the 'prod' configuration.
 * Example 5: 'grails prod schema-export export stdout' exports the schema using the 'prod' configuration, echoing the ddl
 *            to stdout.
 * If you haven't specified the Dialect class in DataSource.groovy and are relying on
 * Grails to auto-detect the Dialect for you, you should consider explicitly setting
 * the value. This script uses the same approach but must connect to your database. Another
 * reason is that there are often version-specific Dialects; for example the default Dialect
 * for MySQL is MySQLDialect but if you're using MySQL v5 you should use MySQL5Dialect and
 * if you want to use transactional InnoDB tables, you should use MySQLInnoDBDialect.
 *
 * @author Burt Beckwith
 */
import org.hibernate.dialect.Dialect
import org.hibernate.dialect.DialectFactory
import org.hibernate.tool.hbm2ddl.SchemaExport
import org.codehaus.groovy.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.support.JdbcUtils

grailsHome = Ant.project.properties.'environment.GRAILS_HOME'
includeTargets << new File("${grailsHome}/scripts/Bootstrap.groovy")

Properties props = new Properties()
String filename = "${basedir}/ddl.sql"
boolean export = false
boolean stdout = false

def configClasspath = {

	getClass().classLoader.rootLoader?.addURL(new File(classesDirPath).toURL())

	Ant.copy(todir: classesDirPath, file: "${basedir}/application.properties")
	Ant.copy(todir: classesDirPath, failonerror: false) {
		fileset(dir: "${basedir}/grails-app/conf", excludes: '*.groovy, log4j*, hibernate, spring')
		fileset(dir: "${basedir}/grails-app/conf/hibernate")
		fileset(dir: "${basedir}/src/java", excludes: '**/*.java')
	}
}

def configureFromArgs = {

	args = args ?: ''
	args.split('\n').each { arg ->
		arg = arg.trim()
		if (arg.length() > 0) {
			if (arg == 'export') {
				export = true
			}
			else if (arg == 'generate') {
				export = false
			}
			else if (arg == 'stdout') {
				stdout = true
			}
			else {
				// assume filename override
				filename = arg
			}
		}
	}
}

def populateProperties = {

	File dsFile = new File("${basedir}/grails-app/conf/DataSource.groovy")
	def dsConfig = null
	if (dsFile.exists()) {
		dsConfig = new ConfigSlurper(grailsEnv).parse(dsFile.text)
	}

	props.'hibernate.connection.username' = dsConfig?.dataSource?.username ?: 'sa'
	props.'hibernate.connection.password' = dsConfig?.dataSource?.password ?: ''
	props.'hibernate.connection.url' = dsConfig?.dataSource?.url ?: 'jdbc:hsqldb:mem:testDB'
	props.'hibernate.connection.driver_class' =
		dsConfig?.dataSource?.driverClassName ?: 'org.hsqldb.jdbcDriver'
	if (dsConfig?.dataSource?.dialect) {
		def dialect = dsConfig.dataSource.dialect
		if (dialect instanceof Class) {
			dialect = dialect.name
		}
		props.'hibernate.dialect' = dialect
	}
	else {
		println("WARNING: Autodetecting the Hibernate Dialect; "
		+ "consider specifying the class name in DataSource.groovy")
		try {
			def ds = new DriverManagerDataSource(
					props.'hibernate.connection.driver_class',
					props.'hibernate.connection.url',
					props.'hibernate.connection.username',
					props.'hibernate.connection.password')
			String dbName = JdbcUtils.extractDatabaseMetaData(ds, 'getDatabaseProductName')
			int majorVersion = JdbcUtils.extractDatabaseMetaData(ds, 'getDatabaseMajorVersion')
			props.'hibernate.dialect' =
				DialectFactory.determineDialect(dbName, majorVersion).class.name
		}
		catch (Exception e) {
			println "ERROR: Problem autodetecting the Hibernate Dialect: ${e.message}"
			throw e
		}
	}
}

target('default': 'Run Hibernate SchemaExport') {
	depends(classpath, checkVersion, configureProxy, packageApp)

	configureFromArgs()

	File file = new File(filename)
	Ant.mkdir(dir: file.parentFile)

	configClasspath()
	loadApp()

	populateProperties()

	def configuration = new DefaultGrailsDomainConfiguration(
			grailsApplication: grailsApp,
			properties: props)

	def schemaExport = new SchemaExport(configuration)
		.setHaltOnError(true)
		.setOutputFile(file.path)
		.setDelimiter(';')

	String action = export ? "Exporting" : "Generating script to ${file.path}"
	println "${action} in environment '${grailsEnv}' using properties ${props}"

	if (export) {
		// 1st drop, warning exceptions
		schemaExport.execute(stdout, true, true, false)
		schemaExport.exceptions.clear()
		// then create
		schemaExport.execute(stdout, true, false, true)
	}
	else {
		// generate
		schemaExport.execute(stdout, false, false, false)
	}

	if (!schemaExport.exceptions.empty) {
		((Exception)schemaExport.exceptions[0]).printStackTrace()
	}
}
