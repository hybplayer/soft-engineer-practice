# api 文档

通过 http 请求访问服务端 ip (可能会变动) 或者地址 (eth.jht213.com) + 端口 (设置为 20334) + api路径, http body 部分其他数据

命令行测试可以使用 `curl` 或者 `httpie`

## user 模块

入口: `/api/users`

1. 注册: `/register`

    发送: 类型 `Post`, `body` 部分只携带 `UserRequest`.

    返回: 状态码 `200`, `body` 部分返回注册 `User` 对象.

2. 登陆: `/login`

    发送: 类型 `Post`, `body` 部分只携带 `UserRequest`.

    返回:
    1. 登陆成功: 状态码 `200`, `body` 部分返回登陆 `UserID`, `String` 类型.
    2. 登陆失败: 状态码 `404`, `body` 部分为空.

3. 获取用户信息: `/{userID}`

    发送: 类型 `Get`, `body` 部分为空.

    返回:
    1. 获取成功: 状态码 `200`, `body` 部分返回用户 `User` 对象.
    2. 获取失败: 状态码 `404`, `body` 部分为空.

4. 修改用户信息: `/{userID}`

    发送: 类型 `Put`, `body` 部分携带完整的需要修改的 `User` 对象, 未修改的字段也需要携带.

    返回:
    1. 修改成功: 状态码 `200`, `body` 部分返回修改后的用户 `User` 对象.
    2. 修改失败: 状态码 `404`, `body` 部分为空. (用户不存在)
    3. 修改失败: 状态码 `400`, `body` 部分为空. (请求错误)

5. 增加和删除 `/{userID}/addTripPreference` 和 `/{userID}/removeTripPreference`:

    发送: 类型 `Patch`, `body` 部分携带 `TripPreference`, `String` 类型.

    返回:
    1. 增加成功: 状态码 `200`, `body` 部分返回修改后的用户 `User` 对象.
    2. 删除成功: 状态码 `200`, `body` 部分返回修改后的用户 `User` 对象.