# API Specification

[Back to TOC](toc.md)

## `username_exists`

| Input      | Type   | Description       |
| ---------- | ------ | ----------------- |
| `username` | String | An exact username |

| Output   | Type    | Description                                     |
| -------- | ------- | ----------------------------------------------- |
| `exists` | Boolean | Whether or not a user exists with that username |

## `search_username`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `username`      | String | The username of a user you want to search for                                   |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of usernames you want to be returned                         | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of usernames returned                                                   |
| `list`          | List of String | A list of possible usernames that you might have been searching for                       |

## `get_user_info`

| Input      | Type   | Description       |
| ---------- | ------ | ----------------- |
| `username` | String | An exact username |

| Output            | Type   | Description                                | Contract                                  |
| ----------------- | ------ | ------------------------------------------ | ----------------------------------------- |
| `name`            | String | Their name                                 | [Length Limit](contracts.md#length-limit) |
| `bio`             | String | Their bio                                  | [Length Limit](contracts.md#length-limit) |
| `tweet_count`     | Number | The number of their tweets                 |                                           |
| `followers_count` | Number | The number of their followers              |                                           |
| `following_count` | Number | The number of the users they are following |                                           |

## `get_tweet_info`

| Input      | Type   | Description                |
| ---------- | ------ | -------------------------- |
| `tweet_id` | String | The unique ID of the tweet |

| Output       | Type   | Description                                    | Contract                                    |
| ------------ | ------ | ---------------------------------------------- | ------------------------------------------- |
| `sender`     | String | The username of the user that sent that tweet  |                                             |
| `sent_on`    | String | The exact date and time that tweet was sent on | [Date and Time](contracts.md#date-and-time) |
| `contents`   | String | The contents of that tweet                     |                                             |
| `like_count` | Number | The number of users that have liked that tweet |                                             |

## `get_tweet_likes`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `tweet_id`      | String | The unique ID of the tweet                                                      |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of usernames you want to be returned                         | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of usernames returned                                                   |
| `list`          | List of String | A list of the usernames of the users that have liked that tweet                           |

## `get_tweets_of_user`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `username`      | String | An exact username                                                               |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of unique tweet IDs you want to be returned                  | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of unique tweet IDs returned                                            |
| `list`          | List of String | A list of unique IDs of the tweet that have been sent by that user                        |

## `get_followers_of_user`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `username`      | String | An exact username                                                               |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of usernames you want to be returned                         | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of usernames returned                                                   |
| `list`          | List of String | A list of the usernames of the users that are following that user                         |

## `get_following_of_user`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `username`      | String | An exact username                                                               |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of usernames you want to be returned                         | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of usernames returned                                                   |
| `list`          | List of String | A list of the usernames of the users that the user is following                           |

## `sign_up`

| Input      | Type   | Description       | Contract                                                  |
| ---------- | ------ | ----------------- | --------------------------------------------------------- |
| `username` | String | Your new username | [Username Persistence](contracts.md#username-persistence) |
| `password` | String | Your new password | [Password Strength](contracts.md#password-strength)       |

| Error                     | Description                               |
| ------------------------- | ----------------------------------------- |
| `USERNAME_EMPTY`          | You must provide a non-empty username     |
| `PASSWORD_EMPTY`          | You must provide a non-empty password     |
| `USERNAME_ALREADY_EXISTS` | The username you entered is already taken |
| `BAD_USERNAME`            | The username contains illegal characters  |
| `PASSWORD_TOO_WEAK`       | The password is not secure enough         |

## `sign_in`

| Input             | Type   | Description                                                                                           | Contract                                                                                       |
| ----------------- | ------ | ----------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| `username`        | String | The username you signed up with                                                                       | [Username Persistence](contracts.md#username-persistence)                                      |
| `password`        | String | Your password                                                                                         |                                                                                                |
| `generated_token` | String | A random token that you can later use as your authentication token if this call returns with no error | [Token Diversity](contracts.md#token-diversity), [Sessions Limit](contracts.md#sessions-limit) |
| `device_info`     | String | Arbitrary information about your device and client app                                                |                                                                                                |

| Error                     | Description                                                                      |
| ------------------------- | -------------------------------------------------------------------------------- |
| `USERNAME_EMPTY`          | You must provide a non-empty username                                            |
| `PASSWORD_EMPTY`          | You must provide a non-empty password                                            |
| `USERNAME_DOES_NOT_EXIST` | The username is mistyped or incorrect as it does not match any existing accounts |
| `BAD_USERNAME`            | The username is mistyped as it contains illegal characters                       |
| `INCORRECT_PASSWORD`      | The password you entered is incorrect                                            |
| `BAD_TOKEN`               | The token is too short or empty                                                  |

## `change_password`

| Input          | Type   | Description               | Contract                                            |
| -------------- | ------ | ------------------------- | --------------------------------------------------- |
| `my_username`  | String | Your username             |                                                     |
| `token`        | String | Your authentication token |                                                     |
| `old_password` | String | Your previous password    |                                                     |
| `new_password` | String | Your new password         | [Password Strength](contracts.md#password-strength) |

## `change_name`

| Input         | Type   | Description               | Contract                                  |
| ------------- | ------ | ------------------------- | ----------------------------------------- |
| `my_username` | String | Your username             |                                           |
| `token`       | String | Your authentication token |                                           |
| `new_name`    | String | Your new name             | [Length Limit](contracts.md#length-limit) |

## `change_bio`

| Input         | Type   | Description               | Contract                                  |
| ------------- | ------ | ------------------------- | ----------------------------------------- |
| `my_username` | String | Your username             |                                           |
| `token`       | String | Your authentication token |                                           |
| `new_bio`     | String | Your new bio              | [Length Limit](contracts.md#length-limit) |

## `get_sessions`

| Input         | Type   | Description               |
| ------------- | ------ | ------------------------- |
| `my_username` | String | Your username             |
| `token`       | String | Your authentication token |

| Output            | Type           | Description                                                            |
| ----------------- | -------------- | ---------------------------------------------------------------------- |
| `count`           | Number         | The number of your sessions                                            |
| `session_id_list` | List of String | A list of the unique IDs of the sessions associated with your username |

## `get_session_info`

| Input         | Type   | Description               |
| ------------- | ------ | ------------------------- |
| `my_username` | String | Your username             |
| `token`       | String | Your authentication token |
| `session_id`  | String | A unique session ID       |

| Output       | Type   | Description                                                                           | Contract                                    |
| ------------ | ------ | ------------------------------------------------------------------------------------- | ------------------------------------------- |
| `device_id`  | String | Some information about the device and the client on which this session was first used |                                             |
| `first_seen` | String | The exact date and time on which this session was first used                          | [Date and Time](contracts.md#date-and-time) |
| `last_seen`  | String | The exact date and time on which this session was last used                           | [Date and Time](contracts.md#date-and-time) |

## `terminate_session`

| Input         | Type   | Description                                        |
| ------------- | ------ | -------------------------------------------------- |
| `my_username` | String | Your username                                      |
| `token`       | String | Your authentication token                          |
| `session_id`  | String | The unique ID of the session you want to terminate |

## `get_timeline`

| Input           | Type   | Description                                                                     | Contract                                            |
| --------------- | ------ | ------------------------------------------------------------------------------- | --------------------------------------------------- |
| `my_username`   | String | Your username                                                                   |                                                     |
| `token`         | String | Your authentication token                                                       |                                                     |
| `pagination_id` | String | Either empty or the unique ID of a pagination that you got from your first call |                                                     |
| `max_count`     | Number | The maximum number of unique tweet IDs you want to be returned                  | [Default Max Count](contracts.md#default-max-count) |

| Output          | Type           | Description                                                                               |
| --------------- | -------------- | ----------------------------------------------------------------------------------------- |
| `pagination_id` | String         | The unique ID of the pagination that you can use in your next calls to get the next pages |
| `count`  | Number         | The actual number of unique tweet IDs returned                                            |
| `list`          | List of String | A list of unique IDs of tweets that ought to be shown in your timeline                    |

## `create_tweet`

| Input         | Type   | Description                                  |
| ------------- | ------ | -------------------------------------------- |
| `my_username` | String | Your username                                |
| `token`       | String | Your authentication token                    |
| `contents`    | String | The contents of the tweet you want to create |

## `delete_tweet`

| Input         | Type   | Description                                   |
| ------------- | ------ | --------------------------------------------- |
| `my_username` | String | Your username                                 |
| `token`       | String | Your authentication token                     |
| `tweet_id`    | String | The unique ID of the tweet you want to delete |

## `like_tweet`

| Input         | Type   | Description                                 |
| ------------- | ------ | ------------------------------------------- |
| `my_username` | String | Your username                               |
| `token`       | String | Your authentication token                   |
| `tweet_id`    | String | The unique ID of the tweet you want to like |

## `unlike_tweet`

| Input         | Type   | Description                                   |
| ------------- | ------ | --------------------------------------------- |
| `my_username` | String | Your username                                 |
| `token`       | String | Your authentication token                     |
| `tweet_id`    | String | The unique ID of the tweet you want to unlike |

## `follow_user`

| Input         | Type   | Description                                 |
| ------------- | ------ | ------------------------------------------- |
| `my_username` | String | Your username                               |
| `token`       | String | Your authentication token                   |
| `username`    | String | The username of the user you want to follow |

## `unfollow_user`

| Input         | Type   | Description                                   |
| ------------- | ------ | --------------------------------------------- |
| `my_username` | String | Your username                                 |
| `token`       | String | Your authentication token                     |
| `username`    | String | The username of the user you want to unfollow |

## `remove_follower`

| Input         | Type   | Description                                               |
| ------------- | ------ | --------------------------------------------------------- |
| `my_username` | String | Your username                                             |
| `token`       | String | Your authentication token                                 |
| `username`    | String | The username of the user you want to remove as a follower |
