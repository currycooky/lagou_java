## /hello/admin
> 仅admin用户有权限访问
> * http://localhost:8080/hello/admin?username=admin
> * http://localhost:8080/hello/admin?username=user

## /hello/user
> admin和user用户有权限访问
> * http://localhost:8080/hello/user?username=admin
> * http://localhost:8080/hello/user?username=user

## /hello/guest
> 所有用户均可访问，无需携带?username参数
> * http://localhost:8080/hello/guest