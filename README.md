# rest-json-quarkus project - active record pattern
El objetivo de este proyecto era conectar la api rest-json en quarkus con una base de datos utilizando el active-record-pattern.

## Usando hibernate:

Hacemos un Docker con la imagen de la base de datos:

   docker run -it --rm --name maria_fresh -e MYSQL_ROOT_PASSWORD=developer -e MYSQL_USR=developer -e MYSQL_PASSWORD=developer -e MYSQL_DATABASE=fruit -p 3306:3306 -d         mariadb:latest
   
Instalamos distintos paquetes:

  mvnw quarkus:add-extension -Dextensions="quarkus-jbdc-mariadb, quarkus-resteasy-jsonb, quarkus-hibernate-orm"
  <dependency>
          <groupId>io.quarkus</groupId>
          <artifactId>quarkus-jdbc-mariadb</artifactId>
        </dependency>

  Instalar agroal:
    ./mvnw quarkus:add-extension -Dextensions="agroal"
    
  quarkus.datasource.db-kind=mariadb
	quarkus.datasource.username = root
	quarkus.datasource.password = developer
	quarkus.datasource.jdbc.url = jdbc:mariadb://localhost:3306/fruit
	# quarkus.datasource.driver=org.mariadb.jdbc.Driver
	

	# drop and create the database at startup (use `update` to only update the schema)
	# quarkus.hibernate-orm.database.generation = drop-and-create
	quarkus.hibernate-orm.database.generation = update



## PANACHE:

Instalamos panache:
  
  ./mvnw quarkus:add-extension -Dextensions=”quarkus-hibernate-orm-panache”

Cambios en el Código:

La clase que conecta con la base de datos ha de extender de panache:
  
@Entity
@Table(name="Fruit")
@JsonPropertyOrder({"name", "decription"})
public class Fruit extends PanacheEntity {
Nada del id
En el resource (donde las peticiones http) en el método post, vamos al método de la clase service y añadimos un .persist():
Resource: @POST
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// curl -d '{"name":"Banana", "description":"Brings a Gorilla too"}'
// -H "Content-Type: application/json" -X POST http://localhost:8080/fruits
public Set<Fruit> add(@Valid Fruit fruit) {
    service.add(fruit);
    return this.list();
}
  
Service:
  
public void add(Fruit fruit) {
    fruit.persist();
}
  
con el método delete:
Resource:
  
@DELETE
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
// curl -d '{"name":"Banana", "description":"Brings a Gorilla too"}'
// -H "Content-Type: application/json" -X DELETE http://localhost:8080/fruits   
public Set<Fruit> delete(@Valid Fruit fruit) {
    service.remove(fruit.getName());
    return list();
}

Service:
public void remove(String name) {
    Fruit fruit = Fruit.find("name", name).firstResult();
    fruit.delete();
}
