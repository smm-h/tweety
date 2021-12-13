# Contracts

[Back to TOC](toc.md)

## Length Limit

| Datapoint               | Min length   | Max length     |
| ----------------------- | ------------ | -------------- |
| The username of a user  | 3 characters | 32 characters  |
| The name of a user      | none         | 64 characters  |
| The bio of a user       | none         | 256 characters |
| The contents of a tweet | 1 character  | 256 characters |

## Date and Time

Date and Time must be parsed and formatted by the following format: `yyyyMMddhhmmss`

## Sessions Limit

A user cannot have more than 10 sessions. By signing in successfully, the oldest session is terminated silently.

## Username Persistence

The username of a user cannot be changed after signing up. Therefore, it is safe to use the username as identifiers in databases.

## Password Strength

A secure password must contain at least one lowercase letter, one uppercase letter, one digit, and a character not from any of these. It must also be at least eight characters.

## Token Diversity

A generated token must be random each time, be comprised entirely of lowercase hex characters, be at least 16 characters long, and contain at least 12 different characters.

## Default Max Count

The default value for the `max_count` parameter in pagination requests is always 30. It is a number that is divisible by 2, 3 and 5. It is not too small to illicit too frequent calls, and not too big to take too long to download. It is also more than enough for a preview, useful for search engines.

If this paramter is absent or zero, the default value be assumed silently.

Addendum: the maximum value for the parameter is 1000.
