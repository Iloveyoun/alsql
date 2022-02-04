# 开发记录-待做

> 未完成

```bash
# POJO生成器
把POJO生成器改成工具类方式，参照MyBatis-plus

#适配SpringBoot框架

#加一个注解，用注解去判断实体类应该跟数据库的对应

```

> 以完成

```bash
# SQL构建工具类 √
SQL构建时，可以指定一个参数，true/false,此参数表示是否把此参数加入构建

# 事务隔离级别 √
默认在配置文件里面配置吧，引用到Config里面去，也可单独为某个连接设置隔离级别，
完善事务隔离级别文档
    # 不支持事务
    TRANSACTION_NONE = 0;
    # 读未提交
    TRANSACTION_READ_UNCOMMITTED = 1;
    # 读提交
    TRANSACTION_READ_COMMITTED   = 2;
    # 可重复读
    TRANSACTION_REPEATABLE_READ  = 4;
    # 序列化
    TRANSACTION_SERIALIZABLE = 8;
    
#分页查询等，应该放在AfSqlConnection里面去，而不是在AfSimpleDB里面 √
因为现在这样的话，就不能在AfSqlConnection中使用分页方法了

#查询单条记录时，可以优化一下 √
类似改成Limit 0,1 ; 而非现在这样，返回值在拿来处理，返回第一条，数据多的时候，传输会耗性能。

#切换分页方式 √
不需要切换分页方式，根据配置文件中driverClassName(数据库驱动)信息去自动识别应该用哪个数据库的分页方式，如果没有匹配到，报错提示，或者默认用MySQL的。
先调用手动实现的分页方法，如果未指定方法（就是在AfSqlObjects类中没有指定分页实现类），启动自动识别
```







# 开始

## 介绍

## 下载

## 示例

> - 本篇将带你从零开始搭建一个环境，从而让你快速熟悉alsql的使用姿势
> - 数据库以MySQL为例



### 普通java版?



### Maven版

> 1、创建测试数据库

- 在数据库创建测试数据库afsql-dome，并在其中运行以下SQL语句，创建表及导入数据。

```sql
-- 创建student表
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL COMMENT '学号',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别',
  `cellphone` varchar(16) DEFAULT '13800000000' COMMENT '手机号',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `recCreateTime` datetime DEFAULT NULL COMMENT '记录创建时间',
  `recReviseTime` datetime DEFAULT NULL COMMENT '记录修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 导入数据
insert  into 
`student`(`id`,`name`,`sex`,`cellphone`,`birthday`,`recCreateTime`,`recReviseTime`) 
values 
(20190001,'盖聂',1,'14099000892','1999-02-19','2022-01-13 13:06:05',NULL)
,(20190002,'卫庄',1,'12823999991','1999-02-03','2022-01-13 13:06:05',NULL)
,(20190003,'张良',1,'13691243355','1999-02-13','2022-01-13 13:06:05',NULL)
,(20190004,'伏念',1,'13234344352','1999-02-16','2022-01-13 13:06:05',NULL)
,(20190005,'颜路',1,'13699292899','1999-01-21','2022-01-13 13:06:05',NULL)
,(20190006,'赤练',0,'13819289890','1999-02-28','2022-01-13 13:06:05',NULL)
,(20190007,'端木蓉',0,'13800000000','1999-02-03','2022-01-13 13:06:05',NULL)
,(20190008,'盗跖',1,'13410012908','1999-02-19','2022-01-13 13:06:05',NULL)
,(20190009,'白凤',1,'13509890090','1999-02-03','2022-01-13 13:06:05',NULL)
,(20190010,'天明',1,'18799891829','1999-02-03','2022-01-13 13:06:05',NULL)
,(20190011,'月儿',0,'13882938990','1999-01-21','2022-01-13 13:06:05',NULL)
,(20190012,'小瑞瑞',0,'13132454356','1999-02-16','2022-01-13 13:06:05',NULL);
```



> 2、创建项目

- 在IDE中新建一个普通maven项目



> 3、导入依赖

- 在pom.xml文件中导入Maven依赖

```xml
<!-- MySQL驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
    <scope>runtime</scope>
</dependency>

<!-- afsql依赖 -->
<dependency>
    <groupId>cn.dev33</groupId>
    <artifactId>xxx</artifactId>
    <version>1.0.0</version>
</dependency>
```



> 4、配置文件

在`src/main/resources/`目录下创建`afsql.properties`文件，并输入以下内容

```properties
# 连接信息
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/afsql-dome?useUnicode=true&characterEncoding=utf-8
username=root
password=rootqazwsx
# 是否启用内置连接池
ispool=true
# 是否在控制台打印sql执行日志
printSql=true
# 是否在初始化时打印版本号
isV=true
```

- 注意要把数据库的用户名、密码改成你自己的。



> 5、创建主类

在项目中新建包 `com.example` ，在此包内新建主类 `App.java`，并输入以下代码：

```java
public class App {
    public static void main(String[] args) throws Exception {
        String sql = "select * from student";
        List<Map> query = AfSimpleDB.query(sql, 0);
        for (Map map : query) {
            System.out.println(map.toString());
        }
    }
}
```



> 6、运行

- 运行之后控制台输出如下信息，就代表框架已经搭建成功了。

```text
****[AfSQL]select * from student
{birthday=1999-02-19, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=盖聂, cellphone=13155555555, id=20190001}
{birthday=1999-02-03, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=卫庄, cellphone=12823999991, id=20190002}
{birthday=1999-02-13, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=张良, cellphone=13691243355, id=20190003}
{birthday=1999-02-16, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=伏念, cellphone=13234344352, id=20190004}
{birthday=1999-01-21, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=颜路, cellphone=13699292899, id=20190005}
{birthday=1999-02-28, recReviseTime=null, sex=0, recCreateTime=2022-01-13 13:06:05.0, name=赤练, cellphone=13819289890, id=20190006}
{birthday=1999-02-03, recReviseTime=null, sex=0, recCreateTime=2022-01-13 13:06:05.0, name=端木蓉, cellphone=13800000000, id=20190007}
{birthday=1999-02-19, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=盗跖, cellphone=13410012908, id=20190008}
{birthday=1999-02-03, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=白凤, cellphone=13509890090, id=20190009}
{birthday=1999-02-03, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=天明, cellphone=18799891829, id=20190010}
{birthday=1999-01-21, recReviseTime=null, sex=0, recCreateTime=2022-01-13 13:06:05.0, name=月儿, cellphone=13882938990, id=20190011}
{birthday=1999-02-16, recReviseTime=null, sex=0, recCreateTime=2022-01-13 13:06:05.0, name=小瑞瑞, cellphone=13132454356, id=20190012}
{birthday=1999-02-19, recReviseTime=null, sex=1, recCreateTime=2022-01-13 13:06:05.0, name=张三, cellphone=14099000892, id=20190013}
```













### SpringBoot集成版?



### 详细了解

通过这个示例，你已经对afsql有了初步的了解，那么现在开始详细了解一下它都有哪些能力吧





# 使用

- 80%的操作都可以使用`AfSimpleDB`,除了少数的外，下面详细介绍。



## SQL语句构建器

- Java 程序员面对的最痛苦的事情之一就是在 Java 代码中嵌入 SQL 语句。这通常是因为需要动态生成 SQL 语句，不然我们可以将它们放到外部文件或者存储过程中。Afsql有另外一个特性可以帮到你，让你从处理典型问题中解放出来，比如加号、引号、换行、格式化问题、嵌入条件的逗号管理及 AND 连接。确实，在 Java 代码中动态生成 SQL 代码真的就是一场噩梦。例如：

```java
String sql = "SELECT P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME, "
"P.LAST_NAME,P.CREATED_ON, P.UPDATED_ON " +
"FROM PERSON P, ACCOUNT A " +
"INNER JOIN DEPARTMENT D on D.ID = P.DEPARTMENT_ID " +
"INNER JOIN COMPANY C on D.COMPANY_ID = C.ID " +
"WHERE (P.ID = A.ID AND P.FIRST_NAME like ?) " +
"OR (P.LAST_NAME like ?) " +
"GROUP BY P.ID " +
"HAVING (P.LAST_NAME like ?) " +
"OR (P.FIRST_NAME like ?) " +
"ORDER BY P.ID, P.FULL_NAME";
```



### 生成insert语句

> 生成insert语句

- java代码如下

```java
public static void createSql() throws Exception {
    DateFormat df = AfSql.dateTimeFormat();  // 时间转换类
    // 生成SQL
    AfSqlInsert insert = new AfSqlInsert("student"); // 数据库表名
    insert.add2("name", "王五"); 	// 字段跟值，一一对应
    insert.add2("sex", true);
    insert.add2("cellphone", "1323321332");
    insert.add2("birthday", "1992-01-17");
    insert.add2("recCreateTime", df.format(new Date()));

    System.out.println("SQL语句为：" + insert.toString());

    // 插入数据
    AfSimpleDB.execute(insert.toString());
}
```

- 生成的SQL语句如下

```sql
INSERT INTO `student`(`name`,`sex`,`cellphone`,`birthday`,`recCreateTime`) 
	VALUES ('王五','1','1323321332','1992-01-17','2022-01-20 22:59:27')
```



> 也可以不写列名，直接写值

- java代码如下

```java
public static void createSql3() throws Exception {
    DateFormat df = AfSql.dateTimeFormat();  // 时间转换类
    // 生成SQL
    AfSqlInsert insert = new AfSqlInsert("student");
    insert.add("202100");
    insert.add("李四");
    insert.add("1");
    insert.add("13233432234");
    insert.add(df.format(new Date()));

    System.out.println("SQL语句为：" + insert.toString());
}
```

- 生成的SQL如下

```sql
INSERT INTO `student` VALUES ('202100','李四','1','13233432234','2022-01-20 23:33:47')
```



### 生成where条件

> 生成where条件

- 代码如下

```java
public static void createSql1() throws Exception {
    String sql = "SELECT * FROM student";
    // 生成SQL的Where条件，条件之间默认用AND连接
    AfSqlWhere where = new AfSqlWhere();
    // id>=2021001
    where.add("id>=2021001");
    // name='李四'
    where.add2("name", "李四");
    // sex!='1'
    where.add("sex", "!=", true);
    // 传入List，生成 id IN ('2021001', '2021002', '2021003')
    where.addIn("id", Arrays.asList("2021001","2021002","2021003"));
    // 传入String[],生成 name IN ('盖聂','卫庄','小瑞瑞')
    where.addIn("name", new String[]{"盖聂", "卫庄", "小瑞瑞"});
    // name LIKE ‘%卫%’
    where.addLike("name", "%卫%");

    System.out.println("生成的SQL为: " + sql + where.toString());
}
```

- 生成的SQL如下

```sql
SELECT * FROM student 
WHERE (id>=2021001) 
	AND (`name`='李四') 
	AND (`sex`!='true') 
	AND (`id` IN ('2021001','2021002','2021003')) 
	AND (`name`IN ('盖聂','卫庄','小瑞瑞')) 
	AND (`name` LIKE '%卫%')
```



### 生成update语句

> 生成update语句

- java代码如下

```java
public static void createSql2() throws Exception {
    // 生成update语句
    AfSqlUpdate update = new AfSqlUpdate("student"); // 数据库表名
    update.add("name=王五");  // SET name=王五
    update.add2("id", 20220101);  // SET id='20220101'
    update.add2("cellphone", "13233333333"); // SET cellphone='13233333333'
    // 生成where语句
    AfSqlWhere where = new AfSqlWhere();
    where.add("id=2021001");    // 可以以字符串形式设定单个条件

    System.out.println("生成的SQL为: " + update + where);
}
```

- 生成的SQL如下

```sql
UPDATE `student` 
SET name=王五,`id`='20220101',`cellphone`='13233333333'  
WHERE (id=2021001)
```



### 动态SQL

- 很多时候，我们对于SQL的生成，希望他是动态的，比如在查询条件中，只有当字段内容不为空，才参与SQL的构建，Afsql也提供相应的方法来满足此需求，例子如下。

```java
public static void main(String[] args) throws Exception {
    createSql4("20220112", null);
}

public static void createSql4(String id, String name) throws Exception {
    // 生成where语句
    String sql = "SELECT * FROM student";
    AfSqlWhere where = new AfSqlWhere();
    // 当id的长度大于0才参与SQL构建
    where.add("id", ">", id, id.length() > 0);
    // 当name不为null才参与SQL构建
    where.add2("name", name, name != null);
    // 不参与构建
    where.add("cellphone=13233232214", false);

    System.out.println("生成的SQL为: " + sql + where);
}
```

- 生成的SQL如下

```sql
SELECT * FROM student WHERE (`id`>'20220112')
```



> 在Afsql的SQL语句构建类中（AfSqlInsert、AfSqlWhere、AfSqlUpdate），所有add/add2方法都重载第三个参数，传入true/false来指定当前参数是否参与最终SQL的构建，可在实际项目中灵活的使用。



## 执行删改

- 方法`AfSimpleDB.execute(String sql)`会执行sql并返回受影响的行数。
- 因此**增、删、改**的sql都可以交给`AfSimpleDB.execute`方法来处理，同时你也可以用此方法执行任何sql语句。



### 执行修改

```java
public static void update() throws Exception {
    String sql = "UPDATE student SET cellphone='13177777' WHERE id=20190001";
    int execute = AfSimpleDB.execute(sql);
    System.out.println("共" + execute + "条数据受到影响");
}
```



### 执行删除

```java
public static void delete() throws Exception {
    String sql = "delete from student WHERE id=20134";
    int execute = AfSimpleDB.execute(sql);
    System.out.println("共" + execute + "条数据受到影响");
}
```



## 执行新增

### 普通sql新增

```java
public static void insert() throws Exception {
    String sql = "insert into " 
		+ "`student`(`id`,`name`,`sex`,`cellphone`,`birthday`,`recCreateTime`,`recReviseTime`) "
		+ "values " 
		+ "(20190013,'张三',1,'14099000892','1999-02-19','2022-01-13 13:06:05',NULL)";
    int execute = AfSimpleDB.execute(sql);
    System.out.println("共" + execute + "条数据受到影响");
}
```



### 实体类新增

- **实体类新增**

首先我们创建一个`student`表的实体类，如下，并为此实体类加上`@AFTABLE`注解。此注解描述数据库表名信息。此类可以用代码生成器自动生成，详情**请戳**。

```java
@AFTABLE(name="student") 	// 描述数据库的表名
public class Student {
	public Integer id ; 
	public String name ; 
	public Boolean sex ; 
	public String cellphone ; 
	public Date birthday ; 
	public Date recCreateTime ; 
	public Date recReviseTime ; 
	
    // 此处省略get/set方法
} 
```

再执行如下代码

```java
public static void insert2() throws Exception {
    DateFormat df = AfSql.dateFormat(); // 时间转换类
    // 创建一个实体类
    Student stu = new Student();
    stu.setId(202101);
    stu.setName("李四");
    stu.setSex(true);
    stu.setCellphone("13233223321");
    stu.setBirthday(df.parse("1998-07-05"));
    stu.setRecCreateTime(new Date());

    AfSimpleDB.insert(stu);
    System.out.println("插入成功");
}
```



- **实体类新增，并返回数据库自增主键**

要返回自增主键，需要在实体类上添加一个`@AFCOLUMNS`注解，此注解描述数据库的自增主键信息，如下

```java
@AFTABLE(name="student") 	// 描述数据库的表名
@AFCOLUMNS(generated="id")	// 描述数据库的自增id字段
public class Student {
	public Integer id ; 
	public String name ; 
	public Boolean sex ; 
	public String cellphone ; 
	public Date birthday ; 
	public Date recCreateTime ; 
	public Date recReviseTime ; 
	
    // 此处省略get/set方法
} 
```

再执行如下代码

```java
public static void insert2() throws Exception {
    DateFormat df = AfSql.dateFormat(); // 时间转换类
    // 创建一个实体类
    Student stu = new Student();
    // 这里不用给ID字段赋值，因为是数据库自增的，如果赋值也可以，后面返回的主键就是这里赋的值
    stu.setName("李四");
    stu.setSex(true);
    stu.setCellphone("13233223321");
    stu.setBirthday(df.parse("1998-07-05"));
    stu.setRecCreateTime(new Date());

    AfSimpleDB.insert(stu);
    System.out.println("新增成功，主键ID=" + stu.getId());
}
```



## 执行查询

### 查询单行记录

查询单条记录，使用`AfSimpleDB.getOne`方法。

当使用单行查询时，只会返回一条记录，如果有多条记录返回时，会把第一条记录当做返回值



- **查询单条记录，返回`List<String[]>`**

```java
public static void query3() throws Exception {
    String sql = "SELECT * FROM student WHERE id=20190012";
    String[] stu = AfSimpleDB.getOne(sql);
    System.out.println(Arrays.toString(stu));
}
```



- **查询单行记录，返回`List<Map>`**

```java
public static void query3() throws Exception {
    String sql = "SELECT * FROM student WHERE id=20190012";
    Map stu = AfSimpleDB.getOne(sql, 0);
    System.out.println(stu);
}
```



- **查询多行记录，返回`List<Model>`**

```java
public static void query3() throws Exception {
    String sql = "SELECT * FROM student WHERE id=20190012";
    Student stu = (Student) AfSimpleDB.getOne(sql, Student.class);
    System.out.println(stu);
}
```



### 查询多行记录

查询多条记录，使用`AfSimpleDB.query`方法



- **查询多行记录，返回`List<String[]>`**

```java
public static void query2() throws Exception {
    String sql = "SELECT * FROM student";
    List<String[]> rows = AfSimpleDB.query(sql);
    for (String[] row : rows) {
        System.out.println(Arrays.toString(row));
    }
}
```



- **查询多行记录，返回`List<Map>`**

```java
public static void query2() throws Exception {
    String sql = "SELECT * FROM student";
    List<Map> rows = AfSimpleDB.query(sql, 0);
    for (Map row : rows) {
        System.out.println(row.toString());
    }
}
```



- **查询多行记录，返回`List<Model>`**

```java
public static void query2() throws Exception {
    String sql = "SELECT * FROM student";
    List<Student> rows = AfSimpleDB.query(sql, Student.class);
    for (Student row : rows) {
        System.out.println(row.toString());
    }
}
```



### 分页查询?

- AfSql内置三种数据库的分页查询，分别是：`MySql`、`Sql Server`、`Oracle`，默认分页方式为 `MySql`，当然你也可以**切换分页方式**，以及**自定义分页方式**



#### 获取AfPage实例

AfPage是实现分页查询的重要工具类，描述了分页信息。

要想实现分页查询，需要先获取分页工具类`AfPage`,此类有一系列方法辅助你进行分页查询。

```java
public static void queryPage() throws Exception {
    // 方式1：直接new,表示第1页，每页10条数据。
    // Limit 0, 10
    AfPage<Object> page1 = new AfPage<>(1, 10);

    // 方式2：通过静态方法获取，表示第1页，每页10条数据。
    // Limit 0, 10
    AfPage<Object> page2 = AfPage.getPage(1, 10);

    // 方式3：通过set来构建,直接设置开始位置
    // Limit 5, 10
    AfPage<Object> page3 = new AfPage();
    page3.setStart(5); 
    page3.setSize(10);
}
```

- 对于大多数数据库的分页都是，通过 `start` 和 `Size` 两个字段来控制分页的
- 方式1和方式2会通过你传入的当前页和页大小自动计算 `start`
- 方式3会直接使用你设置的 `start` 值



#### 分页查询

- **分页查询，映射为List<String[]>**

```java
public static void queryPage() throws Exception {
    AfPage page = new AfPage<>(1, 10);
    
    String sql = "select * from student";
    AfIPage<String[]> pageRows = AfSimpleDB.query(sql, page);
    
    System.out.println("数据总数为" + pageRows.getTotal() + "条！");
    System.out.println("查询返回" + pageRows.getRecords().size() + "条数据，分别为：");
    for (String[] row : pageRows.getRecords()) {
        System.out.println(Arrays.toString(row));
    }
}
```



- **分页查询，映射为`List<Map>`**

```java
public static void queryPage() throws Exception {
    AfPage page = new AfPage<>(1, 10);

    String sql = "select * from student";
    AfIPage<Map> pageRows = AfSimpleDB.query(sql, 0, page);

    System.out.println("数据总数为" + pageRows.getTotal() + "条！");
    System.out.println("查询返回" + pageRows.getRecords().size() + "条数据，分别为：");
    for (Map row : pageRows.getRecords()) {
        System.out.println(row.toString());
    }
}
```



- **分页查询，映射为`List<Model>`**

```java
public static void queryPage() throws Exception {
    AfPage page = new AfPage<>(1, 10);

    String sql = "select * from student";
    AfIPage<Student> pageRows = AfSimpleDB.query(sql, Student.class, page);

    System.out.println("数据总数为" + pageRows.getTotal() + "条！");
    System.out.println("查询返回" + pageRows.getRecords().size() + "条数据，分别为：");
    for (Student row : pageRows.getRecords()) {
        System.out.println(row.toString());
    }
}
```



#### 自定义分页方式

AfSql只实现了常用的几个数据库的分页方式，分别是`MySQL`、`Oracle`、`SqlServer`、`DB2`，如果你使用的数据库不是这几个，或者你要实现其他的分页方式，那么就需要用到自定义分页了。使用方法如下

1、创建一个类，实现`AfPaging`接口，并实现`getPagingSql`方法

```java
// 自定义分页实现类
public class AfSqlPagingTest implements AfPaging {
    @Override
    public String getPagingSql(String sql, AfPage page) {
        // 根据原SQL，返回分页形式的SQL，后续执行分页的时候，会调用此SQL执行
        String pageSql = sql + " LIMIT " + page.getStart() + "," + page.getSize();
        return pageSql;
    }
}
```

2、更改`AfSqlObjects.paging`的实现类。

```java
// 切换为自定义的分页方式
AfSqlObjects.setPaging(new AfSqlPagingTest());
```



## 事务

- 为了能够让`Connection`能够被及时回收，AfSql采用 `用完即释` 的策略，即：在每一次执行sql后将立刻释放 `Connection`
- 在开启事务后，`用完即释`模式将被关闭，直至你提交或者回滚事务后，`Connection`才会被释放
- 有些时候，我们希望sql根据业务进行提交或回滚，也需要用到事务



### 开启事务

```java
public static void affairs() throws Exception {
    AfSqlConnection conn = AfSimpleDB.getConnection();
    conn.beginTransaction();    // 开启事务

    try {
        // 创建一个第一个实体类
        DateFormat df = AfSql.dateFormat(); // 时间转换类
        Student stu1 = new Student();
        stu1.setId(333);
        stu1.setName("李四");
        stu1.setSex(true);
        stu1.setCellphone("13233223321");
        stu1.setBirthday(df.parse("1998-07-05"));
        stu1.setRecCreateTime(new Date());
        // 执行第一条sql
        conn.insert(stu1);

        // 创建一个第二个实体类
        Student stu2 = new Student();
        stu2.setId(333);
        stu2.setName("李四");
        stu2.setSex(true);
        stu2.setCellphone("13233223321");
        stu2.setBirthday(df.parse("1998-07-05"));
        stu2.setRecCreateTime(new Date());
        // 执行第二条sql,由于ID是主键，不可重复，所以这一条SQL会执行失败，事务会回滚，都不会成功
        conn.insert(stu2);

        conn.commit(); // 提交

    } catch (Exception e) {
        conn.rollback(); // 回滚
        conn.close();
        throw e;
    }
}
```



### 设置事务回滚点

```java
public static void affairs1() throws Exception {
    AfSqlConnection conn = AfSimpleDB.getConnection();
    conn.beginTransaction();    // 开启事务
    Savepoint sp = null;    // 事务回滚点

    try {
        // 执行第一条sql
        String sql1 = "UPDATE student SET cellphone='13123456789' WHERE id=20190001";
        conn.execute(sql1);
        // 执行第二条sql
        String sql2 = "UPDATE student SET cellphone='13155555555' WHERE id=20190001";
        conn.execute(sql2);

        // 在这里设置一个事务回滚点
        sp = conn.setSavepoint();

        // 执行第三条sql，这条SQL有错误,故意写错
        String sql3 = "UPDATE student SET cellphone='13144444444' WHER id=20190001";
        conn.execute(sql3);

        conn.commit(); // 提交
    } catch (Exception e) {
        try {
            conn.rollback(sp);  // 回滚到该事务点，即该点之前的会正常执行（sql1,sql2）
            conn.commit();      //回滚了要记得提交,如果没有提交sql1,sql2将会自动回滚
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        conn.close();
        throw e;
    }
}
```



### 设置事务隔离级别

> 统一设置

- 要给所有连接统一设置隔离级别，可以直接在配置文件里面设置，如下，参照**框架配置**。

```properties
# 设置事务隔离级别，默认4
# 支持的隔离级别有4种
# 1:读未提交；2：读提交；4：可重复读；8：序列化
transactionIsolation=4
```



> 单独设置

- 如果想为某个连接单独设置隔离级别，如下

```java
public static void affairs() throws Exception {
    AfSqlConnection conn = AfSimpleDB.getConnection();
    // 设置事务隔离级别
    conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
    conn.beginTransaction();    // 开启事务
    try {
        // 创建一个第一个实体类
        DateFormat df = AfSql.dateFormat(); // 时间转换类
        Student stu1 = new Student();
        stu1.setId(333);
        stu1.setName("李四");
        stu1.setSex(true);
        stu1.setCellphone("13233223321");
        stu1.setBirthday(df.parse("1998-07-05"));
        stu1.setRecCreateTime(new Date());
        // 执行第一条sql
        conn.insert(stu1);

        // 创建一个第二个实体类
        Student stu2 = new Student();
        stu2.setId(333);
        stu2.setName("李四");
        stu2.setSex(true);
        stu2.setCellphone("13233223321");
        stu2.setBirthday(df.parse("1998-07-05"));
        stu2.setRecCreateTime(new Date());
        // 执行第二条sql,由于ID是主键，不可重复，所以这一条SQL会执行失败，事务会回滚，都不会成功
        conn.insert(stu2);

        conn.commit(); // 提交

    } catch (Exception e) {
        conn.rollback(); // 回滚
        conn.close();
        throw e;
    }
}
```



## 连接池

AfSql内部有一个简易的连接池实现，只需在配置文件配置 `ispool=true`即可(默认为false)，当然你也可以轻松的集成第三方连接池。



### 集成第三方连接池

- 这里以C3P0连接池为例，以当前示例Maven项目为例。



**1、在pom.xml文件中导入C3P0的Maven依赖**

```xml
<!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.2</version>
</dependency>
```



**2、在`src/main/resources/`目录下创建`c3p0-config.xml`文件，并输入以下内容**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<c3p0-config>
    <default-config>
        <!-- 更详细配置可以自行百度 -->
        <property name="driverClass">com.mysql.jdbc.Driver</property>
        <property name="jdbcUrl">jdbc:mysql://127.0.0.1/af_school?useUnicode=true&amp;characterEncoding=UTF-8</property>     
        <property name="user">root</property>
        <property name="password">LZM123456</property>

	    <property name="initialPoolSize">2</property>
	    <property name="minPoolSize">2</property>
	    <property name="maxPoolSize">10</property>
	    <property name="maxIdleTime">30</property>
	    <property name="idleConnectionTestPeriod">200</property>	    
    </default-config>
</c3p0-config>
```

- 注意把数据库及、用户名、密码等改成自己的



**3、使用**

- 一般我们配置了之后，就可以直接使用了，一般单独使用C3P0连接池方法如下

```java
public static void testQuery() throws SQLException {
    //实际项目中，这个池子不能关闭，应做成一个全局对象
    //当添加了c3po的支持后，表示池子里面有连接给我们用了
    //一个DataSource指代一个数据源，内部有一个连接池，自动读取c3po-config.xml的配置，这个名字不能改
    ComboPooledDataSource pool = new ComboPooledDataSource();
    // 连接数据库，从连接池中拿到连接
    Connection conn = pool.getConnection();
    // 查询操作
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM student");	//执行查询，返回结果集
    // 从结果集拿到数据
    while(rs.next()){
        int id = rs.getInt("id");
        String name = rs.getString("name");
        System.out.println("id" + id + "name" + name );
    }
    conn.close();	// 连接放回池子
    pool.close();	//关闭池子，（池子一般不关闭）
    System.out.println("关闭成功");
}
```

- 我们现在要把C3P0集成到Afsql中，代码如下

```java
public static void query4() throws Exception {
    // 创建C3P0连接池
    ComboPooledDataSource pool = new ComboPooledDataSource();
    // 将C3P0注入到Afsql,在项目启动时设置一次，就全局引用这个连接池了，不用重复设置
    AfSqlObjects.setDataSource(pool);

    // 自此，再执行SQL所获取的Connection，就是从C3P0连接池中获取的了
    System.out.println(AfSqlObjects.getDataSource());
    String sql = "SELECT * FROM student WHERE id=20190012";
    Student stu = (Student) AfSimpleDB.getOne(sql, Student.class);
    System.out.println(stu);
}
```

- 至此，集成C3P0完成



### 其它连接池

所谓集成第三方连接池，就是构建一个`DataSource`对象，然后通过`AfSqlObjects.setDataSource`方法注入到Afsql，明白这一点，就能够集成任意连接池了。



## 框架配置？

### 通过配置文件配置

- 在源代码根目录下（一般是src/，maven项目一般为src/main/resources/）创建配置文件：`afsql.properties`，并输入以下内容

```properties
# 数据库连接信息
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=utf-8
username=root
password=LZM123456
# 是否启用内置连接池，默认false
ispool=true
# 是否在控制台打印执行的sql语句,默认false
printSql=true
# 打印SQL时的前缀
sqlhh=****[AfSQL]
# 是否在初始化时打印版本号,默认true
isV=true

# 内置连接池相关配置 ******
# 初始连接数，默认10
init=10
# 最小连接数，默认5
min=5
# 最大连接数，默认20
max=20
# 获取连接最大的等待时间，默认3000，单位毫秒
poolTimeToWait=3000
# 分页查询，当Page==null时默认取出的数据量，相当于Limit 0, 1000
defaultLimit=1000
# 设置事务隔离级别，默认4
transactionIsolation=4
```



### 通过代码配置

- java代码如下

```java
public static void configDemo() {
    // 创建配置信息
    AfSqlConfig config = new AfSqlConfig();
    config.setDriverClassName("com.mysql.jdbc.Driver");
    config.setUrl("jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=utf-8");
    config.setUsername("root");
    config.setPassword("rootPassword");
    config.setIspool(true);
    // ... 此处省略 ...

    // 配置写好后，注入到框架中
    AfSqlObjects.setConfig(config);

    // 测试
    System.out.println(AfSqlObjects.getConfig());
}
```



- **注意**：如果同一参数，同时用两种配置方式配置了不同的数值，最后结果以**代码配置为准**。



### SpringBoot环境配置？



