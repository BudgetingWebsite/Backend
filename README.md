# Budgeting Website Backend
This is the backend for the budgeting website. It should provide for the majority of processing and logic for the website. Users should be able to create secure accounts, store income and expense records, categorize those records into bucket, and budget their finances. Statistics should also be provided, such as net income over time.

# Technology
- Java 17
- Dropwizard
- Jooq
- SQLite JDBC
- Bouncycastle
- JUnit
- Jackson
- Mybatis

# Configuration
The project-specific configurations you can set in the YAML file you'll provide to the program.

- `database-url <String>` - _the url to the database._
- `admin-username <String>` - _the username of the admin account to be autogenerated._
- `admin-password <String>` - _the password of the admin account to be autogenerated._
- `max-username-length <Integer>` - _the maximum length to allow for usernames._

# Setup
The schema for initializing the database can be found at src/main/resources/schema.sql.

# Endpoints

## Account

### `POST`&emsp;/account/{uuid}
Creates a new account with the provided username and password.

#### Consumes JSON
```
{
	username: String,
	password: String
}
```

### `AUTH:Basic`&emsp;`DELETE`&emsp;/account/{username}
Delete an existing account based on the provided username. Will return 401 if the provided credentials does not correspond to an account with the admin role or is not the same account that is being deleted.

### `AUTH:Basic`&emsp;`PUT`&emsp;/account/password
Update the password of authenticated account. 

#### Consumes JSON
```
{
	password: String
}
```

### `AUTH:Basic`&emsp;`Permited:ADMIN`&emsp;`PUT`&emsp;/account/{username}/roles
Update the roles of the account of the provided username. The roles should be comma-delimited. They are case-sensitive. The allowed roles are USER and ADMIN.

#### Consumes JSON
```
{
	roles: String
}
```

## Account

### `AUTH:Basic`&emsp;`POST`&emsp;/bucket
Create a new bucket.

#### Consumes JSON
```
{
	name: String,
	share: Double
}
```

### `AUTH:Basic`&emsp;`DELETE`&emsp;/bucket/{uuid}
Delete the bucket associated with the provided UUID.

### `AUTH:Basic`&emsp;`PUT`&emsp;/bucket/{uuid}
Update the bucket associated with the provided UUID.

#### Consumes JSON
```
{
	name: String,
	share: Double
}
```

### `AUTH:Basic`&emsp;`GET`&emsp;/bucket
Get the authenicated user's buckets.

#### Produces JSON
```
{
	[
		{
			uuid: String,
			created: LocalDateTime,
			updated: LocalDateTime,
			owner: String,
			name: String,
			share: Double,
			amount: Long
		},
		...
	]
}
```
