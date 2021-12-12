# Contracts

[Back to TOC](toc.md)

## Length Limit

| Datapoint               | Max length     |
| ----------------------- | -------------- |
| The username of a user  | 32 characters  |
| The name of a user      | 64 characters  |
| The bio of a user       | 256 characters |
| The contents of a tweet | 256 characters |

## Date and Time

Date and Time must be parsed and formatted by the following format: `yyyy-MM-dd hh:mm:ss`

## Sessions Limit

A user cannot have more than 10 sessions. By signing in successfully, the oldest session is terminated silently.

## Username Persistence

The username of a user cannot be changed after signing up. Therefore, it is safe to use the username as identifiers in databases.

## Password Strength

A secure password must contain at least one lowercase letter, one uppercase letter, one digit, and a character not from any of these. It must also be at least eight characters.

## Token Diversity

A generated token must be random each time, be comprised entirely of lowercase hex characters, be at least 16 characters long, and contain at least 12 different characters.
