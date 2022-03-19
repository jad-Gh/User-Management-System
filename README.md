# User-Management-System
User management system APIs using spring boot 

Functionalities:

-CRUD: Add / update / read / delete users
-Logging to files stored seperatley as severe(warn and error only) and default(all logs)
-Custom response class
-Custom reponses for exception handling
-User Roles (ADMIN, MANAGER, MODERATOR, USER)
-Authentication using JWT Access and Refresh tokens upon sign in
-Authorization using the previously mentioned roles
-Upload and store a profile photo
-Email verification upon adding a new user and before the user can sign in
-Forgot password with an email sent that contains a temporary random new password
-Caching for GET APIs to speed up retrieval time with their corresponding evicts once changed

