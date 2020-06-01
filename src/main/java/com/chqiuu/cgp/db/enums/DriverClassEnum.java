package com.chqiuu.cgp.db.enums;

/**
 * 数据库驱动枚举
 * <p>
 * jdbc:odps	com.aliyun.odps.jdbc.OdpsDriver
 * ODPS是分布式的海量数据处理平台，提供了丰富的数据处理功能和灵活的编程框架，主要的功能组件有如下几个。
 * 1. Tunnel服务：数据进出ODPS的唯一通道，提供高并发、高吞吐量的数据上传和下载服务。
 * 2. SQL：基于SQL92并进行了本地化扩展，可用于构建大规模数据仓库和企业BI系统，是应用最为广泛的一类服务。
 * 3. DAG编程模型：类似Hadoop MapReduce，相对SQL更加灵活，但需要一定的开发工作量，适用于特定的业务场景或者自主开发新算法等。
 * 4. Graph编程模型：用于大数据量的图计算功能开发，如计算PageRank。
 * 5. XLIB：提供诸如SVD分解、逻辑回归、随机森林等分布式算法，可用于机器学习、数据挖掘等场景。
 * 6. 安全：管控ODPS中的所有数据对象，所有的访问都必须经过鉴权，提供了ACL、Policy等灵活强大的管理方式。
 * <p>
 * jdbc:derby	org.apache.derby.jdbc.EmbeddedDriver
 * Apache Derby是一个完全用java编写的数据库，Derby是一个Open source的产品，基于Apache License 2.0分发。
 * Apache Derby非常小巧，核心部分derby.jar只有2M，所以既可以做为单独的数据库服务器使用，也可以内嵌在应用程序中使用。
 * Cognos 8 BI的Content Store默认就是使用的Derby数据库，可以在Cognos8的安装目录下看到一个叫derby10.1.2.1的目录，就是内嵌的10.1.2.1 版本的derby。
 * <p>
 * jdbc:mysql	com.mysql.jdbc.Driver
 * jdbc:oracle	oracle.jdbc.driver.OracleDriver
 * jdbc:microsoft	com.microsoft.jdbc.sqlserver.SQLServerDriver
 * jdbc:sybase:Tds	com.sybase.jdbc2.jdbc.SybDriver
 * jdbc:jtds	net.sourceforge.jtds.jdbc.Driver
 * jdbc:postgresql	org.postgresql.Driver
 * jdbc:fake	com.alibaba.druid.mock.MockDriver
 * jdbc:mock	com.alibaba.druid.mock.MockDriver
 * jdbc:hsqldb	org.hsqldb.jdbcDriver
 * jdbc:db2	COM.ibm.db2.jdbc.app.DB2Driver
 * <p>
 * jdbc:sqlite	org.sqlite.JDBC
 * <p>
 * SQLite，是一款轻型的数据库，是遵守ACID的关系型数据库管理系统，它包含在一个相对小的C库中。它是D.RichardHipp建立的公有领域项目。
 * 它的设计目标是嵌入式的，而且目前已经在很多嵌入式产品中使用了它，它占用资源非常的低，在嵌入式设备中，可能只需要几百K的内存就够了。
 * 它能够支持Windows/Linux/Unix等等主流的操作系统，同时能够跟很多程序语言相结合，比如 Tcl、C#、PHP、Java等，还有ODBC接口，
 * 同样比起Mysql、PostgreSQL这两款开源的世界著名数据库管理系统来讲，它的处理速度比他们都快。SQLite第一个Alpha版本诞生于2000年5月。
 * 至2015年已经有15个年头，SQLite也迎来了一个版本 SQLite 3已经发布。
 * 在确定是否在应用程序中使用 SQLite 之前，应该考虑以下几种情况：
 * 1. 有没有可用于 SQLite 的网络服务器。从应用程序运行位于其他计算机上的 SQLite 的惟一方法是从网络共享运行。这样会导致一些问题，像 UNIX® 和 Windows® 网络共享都存在文件锁定问题。还有由于与访问网络共享相关的延迟而带来的性能下降问题。
 * 2. SQLite 只提供数据库级的锁定。虽然有一些增加并发的技巧，但是，如果应用程序需要的是表级别或行级别的锁定，那么 DBMS 能够更好地满足您的需求。
 * 3. 正如前面提到的，SQLite 可以支持每天大约 100,00 次点击率的 Web 站点 —— 并且，在某些情况下，可以处理 10 倍于此的通信量。对于具有高通信量或需要支持庞大浏览人数的 Web 站点来说，应该考虑使用 DBMS。
 * 4. SQLite 没有用户帐户概念，而是根据文件系统确定所有数据库的权限。这会使强制执行存储配额发生困难,强制执行用户许可变得不可能。
 * 5. SQLite 支持多数（但不是全部）的 SQL92 标准。不受支持的一些功能包括完全触发器支持和可写视图。
 * <p>
 * jdbc:ingres	com.ingres.jdbc.IngresDriver
 * Ingres 是比较早的数据库系统，开始于加利福尼亚大学柏克莱分校的一个研究项目，该项目开始于 70 年代早期，在 80 年代早期结束。
 * 像柏克莱大学的其他研究项目一样，它的代码使用BSD许可证。从 80 年代中期，在Ingres 基础上产生了很多商业数据库软件，
 * 包括 Sybase、Microsoft SQL Server、NonStop SQL、Informix 和许多其他的系统。
 * 在 80 年代中期启动的后继项目 Postgres，产生了 PostgreSQL、Illutypea，无论从任何意义上来说，Ingres 都是历史上最有影响的计算机研究项目之一。
 * <p>
 * jdbc:h2	org.h2.Driver
 * H2的优势：
 * 1、h2采用纯Java编写，因此不受平台的限制。
 * 2、h2只有一个jar文件，十分适合作为嵌入式数据库试用。
 * 3、性能和功能的优势
 * 4、支持Embedded，server和in-memory模式以及内存模式。
 * <p>
 * jdbc:mckoi	com.mckoi.JDBCDriver
 * MckoiDDB 全称为Mckoi Ditypeibuted Database，它是基于Java 1.6开发的一种分布式数据库系统，采用100%纯Java编写，可以运行在任何操作系统上。
 * MckoiDDB对小数据集与大数据集都有很好的支持。
 * <p>
 * jdbc:cloudscape	COM.cloudscape.core.JDBCDriver
 * Cloudscape 是一个以 Java 类库形式提供的、轻量级的、可嵌入的关系引擎。它的本机接口是带有 Java 关系扩展的 Java Database Connectivity（JDBC）。它实现了 SQL92E 标准和许多 SQL 99 扩展。该引擎提供了事务和崩溃恢复，支持多个连接，而且支持使用一个连接的多个线程。因为 Cloudscape 是一个 Java 类库，您可以很容易地将它嵌入到任何 Java 应用程序或服务器架构中，同时还不会损害该应用程序的 Java 特性。当需要扩充数据库时，Cloudscape 支持复杂 SQL 事务和 JDBC，该特性允许它将应用程序迁移到其他 SQL 数据库，例如 IBM DB2® Universal Database™（UDB）。
 * <p>
 * jdbc:informix-sqli	com.informix.jdbc.IfxDriver
 * Informix是IBM公司出品的关系数据库管理系统（RDBMS）家族。作为一个集成解决方案，它被定位为作为IBM在线事务处理（OLTP）旗舰级数据服务系统。 IBM对Informix和DB2都有长远的规划，两个数据库产品互相吸取对方的技术优势。在2005年早些时候，IBM推出了Informix Dynamic Server（IDS）第10版。目前最新版本的是IDS11（v11.50，代码名为“Cheetah 2”），在2008年5月6日全球同步上市，
 * <p>
 * jdbc:timesten	com.timesten.jdbc.TimesTenDriver
 * 内存数据库（timesten），顾名思义就是将数据放在内存中直接操作的数据库。
 * <p>
 * jdbc:as400	com.ibm.as400.access.AS400JDBCDriver
 * <p>
 * jdbc:sapdb	com.sap.dbtech.jdbc.DriverSapDB
 * <p>
 * jdbc:JSQLConnect	com.jnetdirect.jsql.JSQLDriver
 * <p>
 * jdbc:JTurbo	com.newatlanta.jturbo.driver.Driver
 * <p>
 * jdbc:firebirdsql	org.firebirdsql.jdbc.FBDriver
 * <p>
 * jdbc:interbase	interbase.interclient.Driver
 * <p>
 * jdbc:pointbase	com.pointbase.jdbc.jdbcUniversalDriver
 * <p>
 * jdbc:edbc	ca.edbc.jdbc.EdbcDriver
 * <p>
 * jdbc:mimer:multi1	com.mimer.jdbc.Driver
 * <p>
 * <p>
 *
 * @author CHQIU
 * @date 2017-08-18
 */
public enum DriverClassEnum {
    /*
     * jdbc:odps	com.aliyun.odps.jdbc.OdpsDriver
     * ODPS是分布式的海量数据处理平台，提供了丰富的数据处理功能和灵活的编程框架，主要的功能组件有如下几个。
     * 1. Tunnel服务：数据进出ODPS的唯一通道，提供高并发、高吞吐量的数据上传和下载服务。
     * 2. SQL：基于SQL92并进行了本地化扩展，可用于构建大规模数据仓库和企业BI系统，是应用最为广泛的一类服务。
     * 3. DAG编程模型：类似Hadoop MapReduce，相对SQL更加灵活，但需要一定的开发工作量，适用于特定的业务场景或者自主开发新算法等。
     * 4. Graph编程模型：用于大数据量的图计算功能开发，如计算PageRank。
     * 5. XLIB：提供诸如SVD分解、逻辑回归、随机森林等分布式算法，可用于机器学习、数据挖掘等场景。
     * 6. 安全：管控ODPS中的所有数据对象，所有的访问都必须经过鉴权，提供了ACL、Policy等灵活强大的管理方式。
     */
    ODPS("odps", "com.aliyun.odps.jdbc.OdpsDriver", "url", null),
    /*
     * jdbc:derby	org.apache.derby.jdbc.EmbeddedDriver
     * Apache Derby是一个完全用java编写的数据库，Derby是一个Open source的产品，基于Apache License 2.0分发。
     * Apache Derby非常小巧，核心部分derby.jar只有2M，所以既可以做为单独的数据库服务器使用，也可以内嵌在应用程序中使用。
     * Cognos 8 BI的Content Store默认就是使用的Derby数据库，可以在Cognos8的安装目录下看到一个叫derby10.1.2.1的目录，就是内嵌的10.1.2.1 版本的derby。
     */
    DERBY("derby", "org.apache.derby.jdbc.EmbeddedDriver", "url", "values 1"),
    /*
     * jdbc:mysql	com.mysql.jdbc.Driver
     */
    MYSQL("MySQL", "com.mysql.jdbc.Driver", "jdbc:mysql://[server]:[port]/[database]?allowMultiQueries=true&serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false", "select 1"),
    ORACLE("Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@[server]:[port]:[database]", "select 1 from dual"),
    SQLSERVER("SQL Server", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:sqlserver://[server]:[port];DatabaseName=[database];integratedSecurity=false", "select 1"),
    SYBASE("sybase", "com.sybase.jdbc2.jdbc.SybDriver", "jdbc:sybase:Tds:[server]:[port]/[database]", ""),
    JTDS("jtds", "net.sourceforge.jtds.jdbc.Driver", "url", null),
    POSTGRESQL("postgresql", "org.postgresql.Driver", "url", "select version()"),
    FAKE("fake", "com.alibaba.druid.mock.MockDriver", "url", null),
    MOCK("mock", "com.alibaba.druid.mock.MockDriver", "url", null),
    HSQLDB("hsqldb", "org.hsqldb.jdbcDriver", "url", "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS"),
    DB2("db2", "COM.ibm.db2.jdbc.app.DB2Driver", "jdbc:db2://[server]:[port]/[database]", "select 1 from sysibm.sysdummy1"),
    /**
     * SQLite 数据库
     * jdbc:sqlite	org.sqlite.JDBC
     * jdbc:sqlite:D:\\xxxdatabase.db
     * <p>
     * SQLite，是一款轻型的数据库，是遵守ACID的关系型数据库管理系统，它包含在一个相对小的C库中。它是D.RichardHipp建立的公有领域项目。
     * 它的设计目标是嵌入式的，而且目前已经在很多嵌入式产品中使用了它，它占用资源非常的低，在嵌入式设备中，可能只需要几百K的内存就够了。
     * 它能够支持Windows/Linux/Unix等等主流的操作系统，同时能够跟很多程序语言相结合，比如 Tcl、C#、PHP、Java等，还有ODBC接口，
     * 同样比起Mysql、PostgreSQL这两款开源的世界著名数据库管理系统来讲，它的处理速度比他们都快。SQLite第一个Alpha版本诞生于2000年5月。
     * 至2015年已经有15个年头，SQLite也迎来了一个版本 SQLite 3已经发布。
     * 在确定是否在应用程序中使用 SQLite 之前，应该考虑以下几种情况：
     * 1. 有没有可用于 SQLite 的网络服务器。从应用程序运行位于其他计算机上的 SQLite 的惟一方法是从网络共享运行。这样会导致一些问题，像 UNIX® 和 Windows® 网络共享都存在文件锁定问题。还有由于与访问网络共享相关的延迟而带来的性能下降问题。
     * 2. SQLite 只提供数据库级的锁定。虽然有一些增加并发的技巧，但是，如果应用程序需要的是表级别或行级别的锁定，那么 DBMS 能够更好地满足您的需求。
     * 3. 正如前面提到的，SQLite 可以支持每天大约 100,00 次点击率的 Web 站点 —— 并且，在某些情况下，可以处理 10 倍于此的通信量。对于具有高通信量或需要支持庞大浏览人数的 Web 站点来说，应该考虑使用 DBMS。
     * 4. SQLite 没有用户帐户概念，而是根据文件系统确定所有数据库的权限。这会使强制执行存储配额发生困难,强制执行用户许可变得不可能。
     * 5. SQLite 支持多数（但不是全部）的 SQL92 标准。不受支持的一些功能包括完全触发器支持和可写视图。
     */
    SQLITE("SQLite", "org.sqlite.JDBC", "jdbc:sqlite:[database]", null),
    INGRES("ingres", "com.ingres.jdbc.IngresDriver", "url", "select 1"),
    /*
     * jdbc:h2	org.h2.Driver
     * H2的优势：
     * 1、h2采用纯Java编写，因此不受平台的限制。
     * 2、h2只有一个jar文件，十分适合作为嵌入式数据库试用。
     * 3、性能和功能的优势
     * 4、支持Embedded，server和in-memory模式以及内存模式。
     */
    H2("H2", "org.h2.Driver", "jdbc:h2:tcp://[server]/~/[database]", "select 1"),
    MCKOI("mckoi", "com.mckoi.JDBCDriver", "jdbc:mckoi://[server]/", null),
    CLOUDSCAPE("cloudscape", "COM.cloudscape.core.JDBCDriver", "url", null),
    INFORMIX("informix", "com.informix.jdbc.IfxDriver", "jdbc:informix-sqli://[server]:[port]/[database]:INFORMIXSERVER=myserver", null),
    TIMESTEN("timesten", "com.timesten.jdbc.TimesTenDriver", "url", null),
    AS400("as400", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://[server]", null),
    SAPDB("sapdb", "com.sap.dbtech.jdbc.DriverSapDB", "jdbc:sapdb://[server]:[port]/[database]", null),
    JSQL("jsql", "com.jnetdirect.jsql.JSQLDriver", "url", null),
    JTURBO("jturbo", "com.newatlanta.jturbo.driver.Driver", "url", null),
    FIREBIRDSQL("firebirdsql", "org.firebirdsql.jdbc.FBDriver", "url", null),
    INTERBASE("interbase", "interbase.interclient.Driver", "url", null),
    POINTBASE("pointbase", "com.pointbase.jdbc.jdbcUniversalDriver", "url", null),
    EDBC("edbc", "ca.edbc.jdbc.EdbcDriver", "url", null),
    MIMER("mimer", "com.mimer.jdbc.Driver", "jdbc:mimer:[database]", null);

    private final String dbType;
    private final String driverCLass;
    private final String url;
    private final String validationQuery;

    public String getDbType() {
        return dbType;
    }

    public String getDriverCLass() {
        return driverCLass;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl(String server, Integer port, String database) {
        String tempUrl = url;
        if (null != server) {
            tempUrl = tempUrl.replace("[server]", server);
        }
        if (null != port) {
            tempUrl = tempUrl.replace("[port]", port.toString());
        }
        if (null != database) {
            tempUrl = tempUrl.replace("[database]", database);
        }
        return tempUrl;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    DriverClassEnum(String dbType, String driverCLass, String url, String validationQuery) {
        this.driverCLass = driverCLass;
        this.dbType = dbType;
        this.url = url;
        this.validationQuery = validationQuery;
    }

    public static DriverClassEnum getByDbType(String dbType) {
        for (DriverClassEnum e : values()) {
            if (e.getDbType().equals(dbType)) {
                return e;
            }
        }
        return null;
    }

    public static DriverClassEnum getByDriverCLass(String driverCLass) {
        for (DriverClassEnum e : values()) {
            if (e.getDriverCLass().equals(driverCLass)) {
                return e;
            }
        }
        return null;
    }

}
