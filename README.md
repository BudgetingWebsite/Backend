# Budgeting Website Backend
This is the backend for the budgeting website. It is written in Java 17 and uses Dropwizard as the framework.

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
---

### `AUTH:Basic`&emsp;`DELETE`&emsp;/account/{username}
Delete an existing account based on the provided username. Will return 401 if the provided credentials does not correspond to an account with the admin role or is not the same account that is being deleted.

---
### `AUTH:Basic`&emsp;`PUT`&emsp;/account/password
Update the password of authenticated account. 

#### Consumes JSON
```
{
	password: String
}
```
---

### `AUTH:Basic`&emsp;`PUT`&emsp;/account/{username}/roles
Update the roles of the account of the provided username. The roles should be comma-delimited.

#### Consumes JSON
```
{
	roles: String
}
```
