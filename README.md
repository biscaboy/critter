# Critter Chronologer Project
Udacity Java Developer Nanodegree Project 3

## Purpose
Critter Chronologer is a Software as a Service application that provides a scheduling interface for a small business that takes care of animals. This Spring Boot project will allow users to create pets, owners, and employees, and then schedule events for employees to provide services for pets.

This project required utilizing data persistence concepts including:

  *  Structuring Data in a Multitier Architeture
  *  Using the Java Persistence API (JPA)
  *  Connecting to multiple data sources
  *  Persistence using SQL and Data Access Objects
  *  Solve persistence performance issues

### [Project Starter Code and Instructions](https://github.com/udacity/nd035-c3-data-stores-and-persistence-project-starter)

### Dependencies

* [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download) (or Ultimate) recommended 
* [Java SE Development Kit 8+](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/download.cgi)
* [MySQL Server 8](https://dev.mysql.com/downloads/mysql/) (or another standalone SQL instance)
* [Postman](https://www.getpostman.com/downloads/)

### Resources

#### Spring.io
* [Spring Data JPA - Reference Documentation 2.4.2](https://docs.spring.io/spring-data/jpa/docs/current/reference/html)
* [Spring Data JPA - Reference Documentation 2.1.21](https://docs.spring.io/spring-data/jpa/docs/2.1.x/reference/html/)


#### Baeldung.com

* [Hibernate Many to Many Annotation Tutorial](https://www.baeldung.com/hibernate-many-to-many)
* [Jackson – Bidirectional Relationships](https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion)
* [The difference between map() and flatMap()](https://www.baeldung.com/java-difference-map-and-flatmap)
* [Iterate over a Map in Java](https://www.baeldung.com/java-iterate-map)
* [Use Criteria Queries in a Spring Data Application](https://www.baeldung.com/spring-data-criteria-queries)
* [Guide to the Hibernate EntityManager](https://www.baeldung.com/hibernate-entitymanager)
* [Guide to the Java 8 forEach](https://www.baeldung.com/foreach-java)

#### StackOverflow.com
* [Pascal Thivent - Querying Relationships Using Joins](https://stackoverflow.com/questions/3424696/jpa-criteria-api-how-to-add-join-clause-as-general-sentence-as-possible)
* [Konstantin Yovkov - What is @StaticMetamodel and SingularAttribute<Obj,Obj>?](https://stackoverflow.com/questions/19704407/what-is-staticmetamodel-and-singularattributeobj-obj)
* [Roland - Hibernate CriteriaBuilder to join multiple tables](https://stackoverflow.com/questions/41982998/hibernate-criteriabuilder-to-join-multiple-tables)
* [Fabian - Hibernate OneToMany java.lang.StackOverflowError](https://cni.castingnetworks.com/talent/confirmation/default.aspx?cid=2989879)
* [Bozho - MultipleBagFetchExceptions and @LazyCollection()](https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl)
* [Ketrox - JPA many-to-many relationship causing infinite recursion and stack overflow error](https://stackoverflow.com/questions/43481353/jpa-many-to-many-relationship-causing-infinite-recursion-and-stack-overflow-erro)
* [jbrookover - Hibernate criteria query for Collection Table?](https://stackoverflow.com/questions/7687409/hibernate-criteria-query-for-collection-table)
* [axtavt - How do I update an entity using spring-data-jpa?](https://stackoverflow.com/questions/11881479/how-do-i-update-an-entity-using-spring-data-jpa)
* [Taryn - SQL Inner-join with 3 tables?](https://stackoverflow.com/questions/10195451/sql-inner-join-with-3-tables)
* [Vlad Dobrydin - org.hibernate.hql.internal.ast.QuerySyntaxException: table is not mapped](https://stackoverflow.com/questions/23018836/org-hibernate-hql-internal-ast-querysyntaxexception-table-is-not-mapped)
* [Oliver Drotbohm - Spring CrudRepository ... equivalent to IN clause](https://stackoverflow.com/questions/18987292/spring-crudrepository-findbyinventoryidslistlong-inventoryidlist-equivalen)
* [Fuury - Spring Data JPA findBy a collection [duplicate]](https://stackoverflow.com/questions/53619322/spring-data-jpa-findby-a-collection)


#### Oracle
* [The Java EE 6 Tutorial - Criteria Queries](https://docs.oracle.com/javaee/6/tutorial/doc/gjivm.html)
* [Querying JPA Entities wiht JPQL and Native SQL](https://www.oracle.com/technical-resources/articles/vasiliev-jpql.html)
* [Creating Queries Using the Criteria API](https://docs.oracle.com/cd/E19226-01/820-7627/gjitv/index.html)

#### Various Sources
* [Pivotal Engineering Journal - Must-Know Spring Boot Annotations: Controllers](https://engineering.pivotal.io/post/must-know-spring-boot-annotations-controllers/)
* [Callicoder - JPA Many to Many Mappings](https://www.callicoder.com/hibernate-spring-boot-jpa-many-to-many-mapping-example/)
* [ConcretePage.com - JPA EntityManager](https://www.concretepage.com/java/jpa/jpa-entitymanager-and-entitymanagerfactory-example-using-hibernate-with-persist-find-contains-detach-merge-and-remove)
* [Codata.com - How to use CriteriaBuilder](https://www.codota.com/code/java/classes/javax.persistence.criteria.CriteriaBuilder)
* [JavaCodeExamples - Get Elements by Index from HashSet in Java](https://www.javacodeexamples.com/get-elements-by-index-from-hashset-in-java-example/2772)
* [Hibernate Reference Documentation](https://docs.jboss.org/hibernate/orm/3.3/reference/en/html/index.html)
* [Buggy Bread - Spring Framework List of Exceptions](https://www.buggybread.com/2015/03/spring-framework-list-of-exceptions.html)

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - Framework providing dependency injection, web framework, data binding, resource management, transaction management, and more.
* [Google Guava](https://github.com/google/guava) - A set of core libraries used in this project for their collections utilities.
* [H2 Database Engine](https://www.h2database.com/html/main.html) - An in-memory database used in this project to run unit tests.
* [MySQL Connector/J](https://www.mysql.com/products/connector/) - JDBC Drivers to allow Java to connect to MySQL Server

## License

This project is licensed under the MIT License - see the [LICENSE.md]()
