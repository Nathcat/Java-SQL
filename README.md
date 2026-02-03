# Java-SQL

This is a relatively simple library to give developers like me a slightly easier
/ simpler time when trying to enforce type safety in interactions between Java
programs, and SQL databases.

## Connecting to a database

One can connect to a database with a combination of the `Database`, and
`DBConfig` types.

```java
DBConfig conf = new DBConfig("JDBC driver URL", "username", "password");
Database db = new Database(conf);
```

I have provided the `DBConfig` class with the intention of allowing you to
specify the configuration in a safe way, especially when said
configuration is read from a local file. I personally use this
an approach where the entire local configuration for my applications
is stored in a single JSON file, and then converted into a type safe
object using [gson](https://github.com/google/gson). For example:

```java
Gson gson = new Gson();
DBConfig conf = gson.fromJson(new InputStreamReader(new FileInputStream("config.json")));
Database db = new Database(conf);
```

## Querying a database

Personally I quite enjoy being able to chain calls when building some kind of
complex object or expression, and this is not possible with the
[PreparedStatement](https://docs.oracle.com/javase/8/docs/api/java/sql/PreparedStatement.html)
class provided in `java.sql`. So, I have wrapped this class with the
`Query` class, which allows for type safe chaining of parameter sets. Note
that not all parameter types are implemented, I have only implemented `String`,
`Integer`, and `Boolean` for now!

```java
// Query db for all users who:
// Have id = 1
// and username like "Nathcat%"
// and verified = true
Query q = db.newQuery("SELECT * FROM Users WHERE id = ? AND username like ? AND verified = ?")
  .set(1, Integer.class, 1)
  .set(2, String.class, "Nathc%")
  .set(3, Boolean.class, true);

ResultSet rs = db.execute(q);
```

## Type safe conversion to DBType

One can also take a `ResultSet`, and convert it into a list of `DBType`s! This allows for a more
verbose and type safe approach to using data which you have pulled from a database.
Continuing with the example from the previous section:

We define a `DBType` for our user query,

```java
public class User implements DBType {
  public int id;
  public String username;
  public boolean verified;
}
```

... and then use `Utils.extractResults` to get a list of `User`.

```java
User[] users = Utils.extractResults(rs, User.class);
```

## Using this library

To use this library, one may either build a `jar` distribution from the source with `./gradlew build`,
or one may add this repository as a submodule of their project, and declare it as a child project in gradle:

Add the submodule:

```
git submodule add https://github.com/Nathcat/Java-SQL Java-SQL
```

Declare the child project in your top-level gradle file:

```kotlin
// settings.gradle.kts

...
include(/* Your projects go here */, "Java-SQL")
project(":Java-SQL").projectDir = file("Java-SQL/lib")
```

Add the child project as a dependency:

```kotlin
// build.gradle.kts

dependencies {
  /* Your dependencies here */

  implementation(project(":Java-SQL"))
}
```
